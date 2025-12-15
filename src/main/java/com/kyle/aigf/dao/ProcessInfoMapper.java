package com.kyle.aigf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyle.aigf.model.entity.ProcessInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessInfoMapper extends BaseMapper<ProcessInfo> {
    void insertProcessInfo(String processName);
}
