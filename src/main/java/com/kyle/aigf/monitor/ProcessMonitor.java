package com.kyle.aigf.monitor;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessMonitor {

    public static Set<String> getCurrentProcessList() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        List<OSProcess> processes = os.getProcesses();
        return processes.stream().map(OSProcess::getName).collect(Collectors.toSet());
    }

}
