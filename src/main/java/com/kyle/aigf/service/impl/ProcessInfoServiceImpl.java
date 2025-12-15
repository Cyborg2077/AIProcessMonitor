package com.kyle.aigf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kyle.aigf.dao.ProcessInfoMapper;
import com.kyle.aigf.model.entity.ProcessInfo;
import com.kyle.aigf.service.ProcessInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessInfoServiceImpl implements ProcessInfoService {
    private final ProcessInfoMapper processInfoMapper;

    @Override
    public void updateProcessNickname(String processName, String nickname) {
        processInfoMapper.update(Wrappers.lambdaUpdate(ProcessInfo.class).eq(ProcessInfo::getProcessName, processName).set(ProcessInfo::getNickname, nickname));
    }
}
