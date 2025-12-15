package com.kyle.aigf.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TimeSlotVO {
    private String name;
    @Schema(type = "string", example = "08:00:00")
    private LocalTime startTime;

    @Schema(type = "string", example = "12:00:00")
    private LocalTime endTime;

    private List<Integer> allowedProcessIds;
}
