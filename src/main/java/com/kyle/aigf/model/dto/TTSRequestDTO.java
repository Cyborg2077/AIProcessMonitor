package com.kyle.aigf.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TTSRequestDTO {
    private String text;
    private String language = "zh";
    private double temperature = 0.3;
    private double top_p = 0.9;
    private double gpt_cond_len = 4;
}
