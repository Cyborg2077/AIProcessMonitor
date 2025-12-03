package com.kyle.aigf.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Accessors(chain = true)
public class TimeSlot {
    private Integer id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private Set<String> allowedProcesses;
}
