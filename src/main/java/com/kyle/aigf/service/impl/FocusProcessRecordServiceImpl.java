package com.kyle.aigf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kyle.aigf.dao.FocusProcessRecordMapper;
import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import com.kyle.aigf.model.vo.ProcessDurationVO;
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
    public List<ProcessDurationVO> findRange(String start, String end) {
        return focusProcessRecordMapper.findRange(start, end);
    }

    @Override
    public List<ProcessDurationVO> topKLongestFocusProcesses(int topK, String start, String end) {
        return focusProcessRecordMapper.selectProcessDurationWithNickname(start, end, topK);
    }

    @Override
    public List<ProcessDurationAggregateVO> groupedFocusDurations(int minDurationSeconds, String start, String end) {
        return focusProcessRecordMapper.getTopKGroupedDuration(start, end, minDurationSeconds);
    }
}
