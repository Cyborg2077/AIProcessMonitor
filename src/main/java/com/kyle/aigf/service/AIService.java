package com.kyle.aigf.service;

import com.kyle.aigf.model.dto.FocusProcessDTO;

public interface AIService {
    void handleFocusProcessEvent(FocusProcessDTO focusDTO);
}
