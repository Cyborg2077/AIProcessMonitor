package com.kyle.aigf.service;

import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;

import java.time.LocalDateTime;
import java.util.List;

public interface FocusProcessRecordService {
    void recordProcessSwitch(String newProcess);

    List<FocusProcessRecord> findRange(LocalDateTime parse, LocalDateTime parse1);

    List<FocusProcessRecord> findToday();

    List<FocusProcessRecord> topKLongestFocusProcesses(int topK, String start, String end);

    List<ProcessDurationAggregateVO> groupedFocusDurations(int topK, String start, String end);
}
