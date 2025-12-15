package com.kyle.aigf.service;

import com.kyle.aigf.common.result.R;
import com.kyle.aigf.model.entity.AllowedProcess;

public interface ProcessMonitorService {
    void monitor();
    int insertWhiteListProcess(AllowedProcess allowedProcess);
}
