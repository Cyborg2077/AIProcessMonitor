package com.kyle.aigf.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TimeSlotListVO {
    private Integer weekDay;
    private List<TimeSlotVO> timeSlotVOList;
}
