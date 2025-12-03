package com.kyle.aigf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyle.aigf.model.entity.FocusProcessRecord;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface FocusProcessRecordMapper extends BaseMapper<FocusProcessRecord> {
    /**
     * 根据时间范围和进程名分组，并按总时长降序排列，返回 Top K 结果。
     * @param startTime 开始时间字符串 (e.g., '2025-11-29T21:00:00')
     * @param endTime 结束时间字符串 (e.g., '2025-11-30T00:00:00')
     * @param topK 限制返回的记录数
     * @return 聚合后的进程时长列表
     */
    List<ProcessDurationAggregateVO> getTopKGroupedDuration(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("topK") int topK
    );
}
