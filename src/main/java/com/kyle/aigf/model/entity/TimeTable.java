package com.kyle.aigf.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TimeTable {
    private List<TimeSlot> slots;

    public TimeSlot currentSlot() {
        LocalDateTime now = LocalDateTime.now();
        return slots.stream()
                .filter(slot -> slot.getStart().isBefore(now) && slot.getEnd().isAfter(now))
                .findFirst()
                .orElse(null);
    }
}
