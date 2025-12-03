package com.kyle.aigf.service;

import com.kyle.aigf.model.dto.TextSegmentDTO;

public interface StreamingTTSService {
    void submitTextSegment(TextSegmentDTO textSegmentDTO);
}
