package com.kyle.aigf.controller;

import com.kyle.aigf.model.entity.TimeSlot;
import com.kyle.aigf.service.ProcessMonitorService;
import com.kyle.aigf.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-slot")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final ProcessMonitorService processMonitorService;

//    @GetMapping("/list")
//    public List<TimeSlot> list() {
//        return timeSlotService.listAll();
//    }
//
//    @PostMapping("/save")
//    public String save(@RequestBody TimeSlot slot) {
//        timeSlotService.save(slot);
//        return "ok";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable Integer id) {
//        timeSlotService.delete(id);
//        return "ok";
//    }
//
//    @PostMapping("/reload")
//    public String reload() {
//        processMonitorService.reloadTimeTable();
//        return "Reloaded";
//    }
}
