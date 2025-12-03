package com.kyle.aigf.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FocusProcessRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String processName;
    private LocalDateTime startTime;
    private LocalDateTime updateTime;
    private Long durationSeconds;
    private String slotName;
}
