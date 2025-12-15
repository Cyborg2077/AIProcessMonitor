package com.kyle.aigf.controller;

import com.kyle.aigf.common.result.R;
import com.kyle.aigf.service.ProcessInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/process-manager")
@Tag(name = "进程管理", description = "进程管理接口")
public class ProcessManagerController {

    private final ProcessInfoService processInfoService;

    @PostMapping("/updateProcessNickname")
    @Operation(
            summary = "更新进程昵称",
            description = "更新进程的昵称，用于显示在时间表和进程列表中",
            operationId = "updateProcessNickname"
    )
    @Parameters({
            @Parameter(
                    name = "processName",
                    in = ParameterIn.QUERY,
                    description = "进程名称",
                    required = true,
                    example = "chrome",
                    schema = @Schema(type = "string")
            ),
            @Parameter(
                    name = "nickname",
                    in = ParameterIn.QUERY,
                    description = "进程昵称",
                    required = true,
                    example = "Chrome",
                    schema = @Schema(type = "string")
            )
    })
    public R<Void> updateProcessNickname(
            String processName, String nickname) {
        processInfoService.updateProcessNickname(processName, nickname);
        return R.Success();
    }
}
