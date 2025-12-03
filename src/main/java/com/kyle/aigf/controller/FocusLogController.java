package com.kyle.aigf.controller;

import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import com.kyle.aigf.service.FocusProcessRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/focus-log")
@RequiredArgsConstructor
public class FocusLogController {

    private final FocusProcessRecordService recordService;

    @GetMapping("/today")
    public List<FocusProcessRecord> today() {
        return recordService.findToday();
    }

    @GetMapping("/range")
    public List<FocusProcessRecord> range(@RequestParam String start,
                                @RequestParam String end) {
        return recordService.findRange(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
    }

    @GetMapping("/topKLongestFocusProcesses")
    public List<FocusProcessRecord> topKLongestFocusProcesses(@RequestParam int topK,
                                                              @RequestParam String start,
                                                              @RequestParam String end) {
        return recordService.topKLongestFocusProcesses(topK, start, end);
    }

    @GetMapping("groupedFocusDurations")
    public List<ProcessDurationAggregateVO> groupedFocusDurations(@RequestParam int topK,
                                                                  @RequestParam String start,
                                                                  @RequestParam String end) {
        return recordService.groupedFocusDurations(topK, start, end);
    }
}
