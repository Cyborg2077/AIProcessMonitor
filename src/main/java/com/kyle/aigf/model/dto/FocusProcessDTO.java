package com.kyle.aigf.model.dto;

import com.kyle.aigf.model.entity.TimeTable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FocusProcessDTO {
    private String processName;
    private TimeTable timeTable;
}
