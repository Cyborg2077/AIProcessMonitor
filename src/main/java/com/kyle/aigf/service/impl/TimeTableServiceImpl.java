package com.kyle.aigf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kyle.aigf.dao.TimeTableMapper;
import com.kyle.aigf.model.entity.TimeTable;
import com.kyle.aigf.model.vo.TimeSlotListVO;
import com.kyle.aigf.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeTableServiceImpl implements TimeTableService {
    private final TimeTableMapper timeTableMapper;

    @Override
    public TimeTable getCurrentTimeSlot() {
        int weekday = LocalDate.now().getDayOfWeek().getValue();
        LocalTime now = LocalTime.now();
        return timeTableMapper.selectOne(Wrappers.lambdaQuery(TimeTable.class)
                .eq(TimeTable::getDayOfWeek, weekday)
                .lt(TimeTable::getStartTime, now)
                .gt(TimeTable::getEndTime, now)
        );
    }

    @Override
    public List<TimeTable> getTimeTableByWeekday(int weekDay) {
        return timeTableMapper.selectList(Wrappers.lambdaQuery(TimeTable.class)
                .eq(TimeTable::getDayOfWeek, weekDay)
                .orderByAsc(TimeTable::getStartTime)
        );
    }

    @Override
    public Map<Integer, List<TimeTable>> getWeeklyTimeTable() {
        List<TimeTable> timeTableList = timeTableMapper.selectList(Wrappers.lambdaQuery(TimeTable.class)
                .orderByAsc(TimeTable::getDayOfWeek)
                .orderByAsc(TimeTable::getStartTime)
        );
        return timeTableList.stream().collect(Collectors.groupingBy(TimeTable::getDayOfWeek));
    }

    @Override
    public String save(List<TimeTable> slotList) {
        return "";
    }

    @Override
    public void saveTimeTable(TimeSlotListVO timeSlotList) {
        timeTableMapper.delete(Wrappers.lambdaQuery(TimeTable.class).eq(TimeTable::getDayOfWeek, timeSlotList.getWeekDay()));
        timeSlotList.getTimeSlotVOList().forEach(timeSlotVO -> timeTableMapper.insert(new TimeTable()
                .setDayOfWeek(timeSlotList.getWeekDay())
                .setName(timeSlotVO.getName())
                .setStartTime(timeSlotVO.getStartTime())
                .setEndTime(timeSlotVO.getEndTime())
                .setAllowedProcessIds(timeSlotVO.getAllowedProcessIds()))
        );
    }


}
