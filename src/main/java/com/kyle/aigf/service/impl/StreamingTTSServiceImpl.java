package com.kyle.aigf.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kyle.aigf.client.TTSClient;
import com.kyle.aigf.model.dto.AudioSegmentDTO;
import com.kyle.aigf.model.dto.TTSRequestDTO;
import com.kyle.aigf.model.dto.TextSegmentDTO;
import com.kyle.aigf.service.StreamingTTSService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.util.Comparator;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamingTTSServiceImpl implements StreamingTTSService {

    private final TTSClient ttsClient;

    private final BlockingQueue<TextSegmentDTO> textQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<AudioSegmentDTO> audioQueue = new PriorityBlockingQueue<>(100, Comparator.comparingInt(AudioSegmentDTO::getSeq));

    private final ExecutorService ttsExecutor = Executors.newFixedThreadPool(5); // TTS 并发生成线程
    private volatile boolean running = true;

    @PostConstruct
    public void init() {
        new Thread(this::start).start();
        new Thread(this::playAudioQueue).start();
    }

    public void submitTextSegment(TextSegmentDTO textSegmentDTO) {
        log.info("添加音频任务：{}", JSON.toJSONString(textSegmentDTO));
        textQueue.add(textSegmentDTO);
    }

    private void stop() {
        running = false;
    }

    private void start() {
        ttsExecutor.submit(this::processTextQueue);
    }

    private void processTextQueue() {
        while (running) {
            try {
                TextSegmentDTO segmentDTO = textQueue.take();
                byte[] audioBytes = ttsClient.text2Audio(new TTSRequestDTO()
                        .setText(segmentDTO.getText()));
                audioQueue.offer(new AudioSegmentDTO()
                        .setSeq(segmentDTO.getSeq())
                        .setText(segmentDTO.getText())
                        .setAudioBytes(audioBytes));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void playAudioQueue() {
        int expectedSeq = 0;
        while (running) {
            try {
                AudioSegmentDTO segment = audioQueue.take();
                if (segment.getSeq() == expectedSeq) {
                    processAudio(segment);
                    expectedSeq++;
                } else {
                    audioQueue.offer(segment);
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processAudio(AudioSegmentDTO segment) {
        byte[] audioBytes = segment.getAudioBytes();
        // 保存为本地文件 xx.wav

        AudioFormat format = new AudioFormat(22050, 16, 1, true, false); // 根据实际 WAV 调整
        try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
            line.open(format);
            line.start();
            line.write(audioBytes, 0, audioBytes.length);
            line.drain();
            line.stop();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        log.info("播放音频:{}", segment.getText());
    }
}
