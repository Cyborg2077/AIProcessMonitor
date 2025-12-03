package com.kyle.aigf.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessDurationAggregateVO {
    private String processName;
    private int durationSeconds;
}
