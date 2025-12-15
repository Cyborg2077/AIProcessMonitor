package com.kyle.aigf.controller;

import com.kyle.aigf.common.result.R;
import com.kyle.aigf.model.vo.ProcessDurationAggregateVO;
import com.kyle.aigf.model.vo.ProcessDurationVO;
import com.kyle.aigf.service.FocusProcessRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专注日志管理控制器
 */
@RestController
@RequestMapping("/api/focus-log")
@RequiredArgsConstructor
@Tag(name = "专注日志管理", description = "专注记录查询、时长统计等接口")
public class FocusLogController {

    private final FocusProcessRecordService recordService;

    /**
     * 按时间范围查询专注记录
     *
     * @param start 开始时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @param end   结束时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @return 时间范围内的专注记录列表
     */
    @GetMapping("/range")
    @Operation(
            summary = "按时间范围查询专注记录",
            description = "传入开始/结束时间（格式：yyyy-MM-ddTHH:mm:ss），查询该时间段内的所有专注记录",
            operationId = "getFocusLogsByRange"
    )
    @Parameters({
            @Parameter(
                    name = "start",
                    in = ParameterIn.QUERY,
                    description = "开始时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-01T09:00:00",
                    schema = @Schema(type = "string")
            ),
            @Parameter(
                    name = "end",
                    in = ParameterIn.QUERY,
                    description = "结束时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-06T18:00:00",
                    schema = @Schema(type = "string")
            )
    })
    public R<List<ProcessDurationVO>> range(
            @Parameter(hidden = true) @RequestParam String start,
            @Parameter(hidden = true) @RequestParam String end) {
        return R.Success(recordService.findRange(start, end));
    }

    /**
     * 查询指定时间范围内专注时长最长的TopK记录
     *
     * @param topK  取前N条（如topK=5，返回最长的5条）
     * @param start 开始时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @param end   结束时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @return 按专注时长降序排列的TopK记录列表
     */
    @GetMapping("/topKLongestFocusProcesses")
    @Operation(
            summary = "查询TopK最长专注记录",
            description = "按专注时长降序，返回指定时间范围内最长的N条专注记录",
            operationId = "getTopKLongestFocusLogs"
    )
    @Parameters({
            @Parameter(
                    name = "topK",
                    in = ParameterIn.QUERY,
                    description = "要查询的条数（如5表示取最长的5条）",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "100")
            ),
            @Parameter(
                    name = "start",
                    in = ParameterIn.QUERY,
                    description = "开始时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-01T00:00:00",
                    schema = @Schema(type = "string")
            ),
            @Parameter(
                    name = "end",
                    in = ParameterIn.QUERY,
                    description = "结束时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-06T23:59:59",
                    schema = @Schema(type = "string")
            )
    })
    public R<List<ProcessDurationVO>> topKLongestFocusProcesses(
            @Parameter(hidden = true) @RequestParam int topK,
            @Parameter(hidden = true) @RequestParam String start,
            @Parameter(hidden = true) @RequestParam String end) {
        return R.Success(recordService.topKLongestFocusProcesses(topK, start, end));
    }

    /**
     * 按进程分组统计指定时间范围内的专注时长
     *
     * @param minDurationSeconds  最短持续时间
     * @param start 开始时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @param end   结束时间（格式：yyyy-MM-ddTHH:mm:ss）
     * @return 按专注时长降序的进程时长聚合列表
     */
    @GetMapping("/groupedFocusDurations")
    @Operation(
            summary = "按进程分组统计专注时长",
            description = "统计指定时间范围内各进程的总专注时长，返回TopK最长时长的进程聚合数据",
            operationId = "getGroupedFocusDurations"
    )
    @Parameters({
            @Parameter(
                    name = "minDurationSeconds",
                    in = ParameterIn.QUERY,
                    description = "要查询的进程数（如3表示取时长前三的进程）",
                    required = true,
                    example = "300",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "999999")
            ),
            @Parameter(
                    name = "start",
                    in = ParameterIn.QUERY,
                    description = "开始时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-01T00:00:00",
                    schema = @Schema(type = "string")
            ),
            @Parameter(
                    name = "end",
                    in = ParameterIn.QUERY,
                    description = "结束时间（格式：yyyy-MM-ddTHH:mm:ss）",
                    required = true,
                    example = "2025-12-06T23:59:59",
                    schema = @Schema(type = "string")
            )
    })
    public R<List<ProcessDurationAggregateVO>> groupedFocusDurations(
            @Parameter(hidden = true) @RequestParam int minDurationSeconds,
            @Parameter(hidden = true) @RequestParam String start,
            @Parameter(hidden = true) @RequestParam String end) {
        return R.Success(recordService.groupedFocusDurations(minDurationSeconds, start, end));
    }
}