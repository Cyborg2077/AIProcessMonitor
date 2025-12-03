package com.kyle.aigf.utils;

import com.kyle.aigf.model.dto.FocusProcessDTO;
import com.kyle.aigf.service.AIService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventBus {
    private final Map<String, List<Consumer<?>>> map = new ConcurrentHashMap<>();

    private final AIService aiService;


    public static final String CREATE_PROCESS_EVENT = "CREATE_PROCESS_EVENT";
    public static final String DESTROY_PROCESS_EVENT = "DESTROY_PROCESS_EVENT";
    public static final String FOCUS_PROCESS_EVENT = "FOCUS_PROCESS_EVENT";

    @PostConstruct
    public void init() {
//        this.subscribe(CREATE_PROCESS_EVENT, msg -> System.out.println("[ALERT] " + msg));
//        this.subscribe(DESTROY_PROCESS_EVENT, msg -> System.out.println("[ALERT] " + msg));
        this.subscribe(FOCUS_PROCESS_EVENT, focusDTO -> aiService.handleFocusProcessEvent((FocusProcessDTO) focusDTO));
    }

    public <T> void subscribe(String event, Consumer<T> consumer) {
        map.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>()).add(consumer);
    }

    @SuppressWarnings("unchecked")
    public <T> void pushMessage(String event, T data) {
        List<Consumer<?>> consumerList = map.get(event);
        if (consumerList != null) {
            for (Consumer<?> consumer : consumerList) {
                try {
                    ((Consumer<T>) consumer).accept(data);
                } catch (Exception e) {
                    log.error("处理事件 {} 时出现异常：{}", event, e.getMessage(), e);
                }
            }
        }
    }

}
