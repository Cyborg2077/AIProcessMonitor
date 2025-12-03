package com.kyle.aigf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kyle.aigf.dao.FocusProcessRecordMapper;
import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import com.kyle.aigf.service.FocusProcessRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FocusProcessRecordServiceImpl implements FocusProcessRecordService {
    private final FocusProcessRecordMapper focusProcessRecordMapper;

    private FocusProcessRecord lastRecord;

    @Override
    public void recordProcessSwitch(String newProcess) {
        LocalDateTime now = LocalDateTime.now();
        if (lastRecord != null) {
            lastRecord.setUpdateTime(now);
            lastRecord.setDurationSeconds(Duration.between(lastRecord.getStartTime(), lastRecord.getUpdateTime()).getSeconds());
            focusProcessRecordMapper.updateById(lastRecord);
        }

        FocusProcessRecord newRecord = new FocusProcessRecord()
                .setProcessName(newProcess)
                .setStartTime(now)
                .setUpdateTime(now);
        focusProcessRecordMapper.insert(newRecord);
        lastRecord = newRecord;
    }

    @Override
    public List<FocusProcessRecord> findRange(LocalDateTime start, LocalDateTime end) {
        return focusProcessRecordMapper.selectList(Wrappers.lambdaQuery(FocusProcessRecord.class)
                .gt(FocusProcessRecord::getStartTime, start)
                .lt(FocusProcessRecord::getUpdateTime, end)
        );
    }

    @Override
    public List<FocusProcessRecord> findToday() {
        LocalDateTime now = LocalDateTime.now();
        return findRange(now.withHour(0).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    @Override
    public List<FocusProcessRecord> topKLongestFocusProcesses(int topK, String start, String end) {
        return focusProcessRecordMapper.selectList(Wrappers.lambdaQuery(FocusProcessRecord.class)
                .gt(FocusProcessRecord::getStartTime, start)
                .lt(FocusProcessRecord::getUpdateTime, end)
                .orderByDesc(FocusProcessRecord::getDurationSeconds)
                .last("LIMIT " + topK)
        );
    }

    @Override
    public List<ProcessDurationAggregateVO> groupedFocusDurations(int topK, String start, String end) {
        return focusProcessRecordMapper.getTopKGroupedDuration(start, end, topK);
    }
}
