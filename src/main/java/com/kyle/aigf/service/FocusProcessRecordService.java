package com.kyle.aigf.service;

import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import com.kyle.aigf.model.vo.ProcessDurationVO;

import java.time.LocalDateTime;
import java.util.List;

public interface FocusProcessRecordService {
    void recordProcessSwitch(String newProcess);

    List<ProcessDurationVO> findRange(String start, String end);

    List<ProcessDurationVO> topKLongestFocusProcesses(int topK, String start, String end);

    List<ProcessDurationAggregateVO> groupedFocusDurations(int topK, String start, String end);
}
