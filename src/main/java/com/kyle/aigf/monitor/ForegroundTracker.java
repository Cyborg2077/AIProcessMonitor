package com.kyle.aigf.monitor;


import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class ForegroundTracker {
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = Native.load("user32", User32.class);

        WinDef.HWND GetForegroundWindow();

        int GetWindowThreadProcessId(WinDef.HWND hWnd, int[] lpdwProcessId);
    }

    public static String focusProcess() {
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        int[] pid = new int[1];
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        OSProcess process = os.getProcess(pid[0]);
        return process.getName();
    }
}
