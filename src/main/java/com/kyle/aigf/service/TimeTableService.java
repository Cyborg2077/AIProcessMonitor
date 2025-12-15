package com.kyle.aigf.service;

import com.kyle.aigf.model.entity.TimeTable;
import com.kyle.aigf.model.vo.TimeSlotListVO;

import java.util.List;
import java.util.Map;

public interface TimeTableService {

    TimeTable getCurrentTimeSlot();

    List<TimeTable> getTimeTableByWeekday(int weekDay);

    Map<Integer, List<TimeTable>> getWeeklyTimeTable();

    String save(List<TimeTable> slotList);

    void saveTimeTable(TimeSlotListVO timeSlotList);
}
