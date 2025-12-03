package com.kyle.aigf.service.impl;


import com.alibaba.fastjson2.JSON;
import com.kyle.aigf.constant.AllowedProcessConstants;
import com.kyle.aigf.constant.TestConstant;
import com.kyle.aigf.model.entity.TimeSlot;
import com.kyle.aigf.model.entity.TimeTable;
import com.kyle.aigf.monitor.ForegroundTracker;
import com.kyle.aigf.monitor.ProcessMonitor;
import com.kyle.aigf.service.FocusProcessRecordService;
import com.kyle.aigf.service.ProcessMonitorService;
import com.kyle.aigf.utils.EventBus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessMonitorServiceImpl implements ProcessMonitorService {

    private final FocusProcessRecordService focusProcessRecordService;

    private final EventBus eventBus;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private TimeTable timeTable;
    private Set<String> lastProcesses;
    private String lastForegroundProcess;

    @PostConstruct
    public void init() {
        List<TimeSlot> slots = JSON.parseArray(TestConstant.TEST_TIME_TABLE_JSON, TimeSlot.class);
        this.timeTable = new TimeTable().setSlots(slots);

        this.lastProcesses = ProcessMonitor.getCurrentProcessList();

        scheduler.scheduleAtFixedRate(this::monitor, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void monitor() {
        try {
            Set<String> current = ProcessMonitor.getCurrentProcessList();
            Set<String> newProcesses = new HashSet<>(current);
            newProcesses.removeAll(lastProcesses);

            TimeSlot slot = timeTable.currentSlot();
            if (slot != null) {
                // 检查新启动的进程
                for (String process : newProcesses) {
                    if (!slot.getAllowedProcesses().contains(process)) {
                        eventBus.pushMessage("alert",
                                String.format("当前作息 [%s] 不允许启动进程 [%s]", slot.getName(), process));
                    }
                }

                // 前台进程

            }
            String fg = ForegroundTracker.focusProcess();
            if (!fg.equals(lastForegroundProcess)) {  // 只有发生变化才提醒
                if (!AllowedProcessConstants.ALLOWED_PROCESS_LIST.contains(fg)) {
                    log.info("当前聚焦进程：{}", fg);
                    focusProcessRecordService.recordProcessSwitch(fg);
//                        eventBus.pushMessage(EventBus.FOCUS_PROCESS_EVENT,
//                                new FocusProcessDTO()
//                                        .setProcessName(fg)
//                                        .setTimeTable(timeTable));
                }
                lastForegroundProcess = fg; // 更新状态
            }

            lastProcesses = current;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
