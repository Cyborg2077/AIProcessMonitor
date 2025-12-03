package com.kyle.aigf.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AllowedProcess {
    private Integer id;
    private String processName;
}
