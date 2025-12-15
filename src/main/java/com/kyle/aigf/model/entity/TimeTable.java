package com.kyle.aigf.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "time_table", autoResultMap = true)
public class TimeTable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer dayOfWeek;

    private String name;

    @Schema(type = "string", example = "08:00:00")
    private LocalTime startTime;

    @Schema(type = "string", example = "12:00:00")
    private LocalTime endTime;


    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> allowedProcessIds;
}