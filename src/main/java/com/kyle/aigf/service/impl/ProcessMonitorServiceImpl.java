package com.kyle.aigf.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kyle.aigf.common.constant.AllowedProcessConstants;
import com.kyle.aigf.dao.AllowedProcessMapper;
import com.kyle.aigf.dao.ProcessInfoMapper;
import com.kyle.aigf.model.dto.FocusProcessDTO;
import com.kyle.aigf.model.entity.AllowedProcess;
import com.kyle.aigf.monitor.ForegroundTracker;
import com.kyle.aigf.service.FocusProcessRecordService;
import com.kyle.aigf.service.ProcessMonitorService;
import com.kyle.aigf.service.TimeTableService;
import com.kyle.aigf.utils.EventBus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessMonitorServiceImpl implements ProcessMonitorService {

    private final FocusProcessRecordService focusProcessRecordService;
    private final TimeTableService timeTableService;
    private final EventBus eventBus;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private List<AllowedProcess> WHITE_LIST_PROCESSES;
    private final AllowedProcessMapper allowedProcessMapper;
    private final ProcessInfoMapper processInfoMapper;

    private String lastForegroundProcess;

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::monitor, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void monitor() {
        try {
            String fg = ForegroundTracker.focusProcess();
            if (!fg.equals(lastForegroundProcess)) {
                refreshWhiteList();
                if (!WHITE_LIST_PROCESSES.stream().map(AllowedProcess::getProcessName).toList().contains(fg)) {
                    log.info("当前聚焦进程：{}", fg);
                    processInfoMapper.insertProcessInfo(fg);
                    focusProcessRecordService.recordProcessSwitch(fg);
                    eventBus.pushMessage(EventBus.FOCUS_PROCESS_EVENT,
                            new FocusProcessDTO()
                                    .setProcessName(fg)
                                    .setTimeTable(timeTableService.getCurrentTimeSlot()));
                }
                lastForegroundProcess = fg; // 更新状态
            }
        } catch (Exception e) {
            log.error("进程监听异常", e);
        }
    }

    @Override
    public int insertWhiteListProcess(AllowedProcess allowedProcess) {
        int count = allowedProcessMapper.insert(allowedProcess);
        WHITE_LIST_PROCESSES.clear();
        return count;
    }

    public void refreshWhiteList() {
        if (WHITE_LIST_PROCESSES == null || WHITE_LIST_PROCESSES.isEmpty()) {
            WHITE_LIST_PROCESSES = allowedProcessMapper.selectList(Wrappers.<AllowedProcess>lambdaQuery()
                    .eq(AllowedProcess::getType, AllowedProcessConstants.WHITE_LIST));
        }
    }
}
