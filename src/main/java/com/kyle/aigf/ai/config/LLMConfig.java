package com.kyle.aigf.ai.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LLMConfig {

    @Bean
    public StreamingChatModel streamingChatModelQwenPlus() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("qwen-api"))
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen-plus")
                .build();
    }

    @Bean
    public ChatAssistant streamingChatAssistant(StreamingChatModel streamingChatModelQwenPlus) {
        return AiServices.builder(ChatAssistant.class)
                .chatMemoryProvider(userId -> MessageWindowChatMemory.withMaxMessages(100))
                .streamingChatModel(streamingChatModelQwenPlus)
                .build();
    }
}
