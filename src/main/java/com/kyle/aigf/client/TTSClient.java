package com.kyle.aigf.client;

import com.alibaba.fastjson2.JSON;
import com.kyle.aigf.model.dto.TTSRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TTSClient {
    @Value("${tts.text2Audio}")
    private String TEXT_TO_AUDIO;

    private final BaseClient baseClient;

    public byte[] text2Audio(TTSRequestDTO requestDTO) {

        Request request = new Request.Builder()
                .url(TEXT_TO_AUDIO)
                .post(RequestBody.create(JSON.toJSONBytes(requestDTO))).build();

        try (Response response = baseClient.getClient().newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().bytes();
            } else {
                log.error("请求失败，状态码：{}", response.code());
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
