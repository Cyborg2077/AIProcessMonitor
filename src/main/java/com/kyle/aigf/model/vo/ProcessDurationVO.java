package com.kyle.aigf.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ProcessDurationVO {
    private String processName;
    private LocalDateTime startTime;
    private LocalDateTime updateTime;
    private Long durationSeconds;
    private String nickname;
}
