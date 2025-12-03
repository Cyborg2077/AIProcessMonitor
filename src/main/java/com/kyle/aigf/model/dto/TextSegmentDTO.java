package com.kyle.aigf.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextSegmentDTO {
    private int seq;
    private String text;
}
