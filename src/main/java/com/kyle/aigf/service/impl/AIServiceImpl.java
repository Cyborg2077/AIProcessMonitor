package com.kyle.aigf.service.impl;

import com.kyle.aigf.ai.config.ChatAssistant;
import com.kyle.aigf.model.entity.TimeSlot;
import com.kyle.aigf.model.entity.TimeTable;
import com.kyle.aigf.model.dto.FocusProcessDTO;
import com.kyle.aigf.model.dto.TextSegmentDTO;
import com.kyle.aigf.service.AIService;
import com.kyle.aigf.service.StreamingTTSService;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final StreamingTTSService ttsService;

    private final ChatAssistant streamingChatAssistant;
    private final AtomicInteger seq = new AtomicInteger(-1);

    @Override
    public void handleFocusProcessEvent(FocusProcessDTO focusDTO) {
        LocalDateTime now = LocalDateTime.now();
        TimeTable timeTable = focusDTO.getTimeTable();
        // 遍历时间片
        for (TimeSlot slot : timeTable.getSlots()) {
            // 找到当前所在的时间片
            if (now.isAfter(slot.getStart()) && now.isBefore(slot.getEnd())) {
                String processName = focusDTO.getProcessName();
                String prompt = String.format("现在是%s时间，用户聚焦于%s进程，如果此时不应该启动%s进程，请给出简短的回复", slot.getName(), processName, processName);
                String userId = "Kyle";
                TokenStream stream = streamingChatAssistant.chat(userId, prompt);
                StringBuilder sbBuffer = new StringBuilder();
                stream.onPartialResponse((text) -> {
                            System.out.print(text);  // 控制台实时显示
                            sbBuffer.append(text);

                            // 按中文标点或英文句号拆分
                            String bufferStr = sbBuffer.toString();
                            String[] parts = bufferStr.split("(?<=[。！？.!?,，])"); // 保留标点

                            // 遍历除了最后一个的完整句子
                            for (int i = 0; i < parts.length - 1; i++) {
                                String sentence = parts[i].trim();
                                if (!sentence.isEmpty()) {
                                    ttsService.submitTextSegment(new TextSegmentDTO()
                                            .setText(sentence)
                                            .setSeq(seq.incrementAndGet())
                                    );
                                }
                            }

                            // 最后一个可能不完整，保留在缓冲
                            sbBuffer.setLength(0);
                            sbBuffer.append(parts[parts.length - 1]);

                        })
                        .onError(Throwable::printStackTrace)
                        .onCompleteResponse((finalText) -> {
                            // 最后一段文字也要处理
                            String remaining = sbBuffer.toString().trim();
                            if (!remaining.isEmpty()) {
                                ttsService.submitTextSegment(new TextSegmentDTO()
                                        .setText(remaining)
                                        .setSeq(seq.incrementAndGet())
                                );
                            }
                            System.out.println(finalText);
                        })
                        .start();
            }
        }
    }
}
