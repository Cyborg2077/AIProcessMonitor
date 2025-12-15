package com.kyle.aigf.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessInfo {
    private Integer id;
    private String processName;
    private String nickname;
}
