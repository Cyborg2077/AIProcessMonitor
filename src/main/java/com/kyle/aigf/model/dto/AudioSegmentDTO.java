package com.kyle.aigf.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AudioSegmentDTO {
    private int seq;
    private String text;
    private byte[] audioBytes;
}
