package com.kyle.aigf.controller;

import com.kyle.aigf.common.result.ErrorCodeEnum;
import com.kyle.aigf.common.result.R;
import com.kyle.aigf.model.entity.AllowedProcess;
import com.kyle.aigf.model.entity.TimeTable;
import com.kyle.aigf.model.vo.TimeSlotListVO;
import com.kyle.aigf.service.ProcessMonitorService;
import com.kyle.aigf.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/time-slot")
@RequiredArgsConstructor
@Tag(name = "时间段管理", description = "时间表查询、新增/修改等接口")
public class TimeSlotController {

    private final TimeTableService timeTableService;
    private final ProcessMonitorService processMonitorService;

    @PostMapping("/insertWhiteListProcess")
    @Operation(
            summary = "新增白名单进程",
            description = "新增一个白名单进程，用于排除FocusProcessRecordService记录的进程",
            operationId = "insertWhiteListProcess"
    )
    public R<Void> insertWhiteListProcess(@RequestBody AllowedProcess process) {
        int count = processMonitorService.insertWhiteListProcess(process);
        if (count == 0) {
            return R.Fail(ErrorCodeEnum.PROCESS_ALREADY_EXIST);
        } else {
            return R.Success();
        }
    }

    /**
     * 根据星期几查询时间表
     * @param weekDay 星期几（1=周一，7=周日）
     * @return 对应星期的时间表列表
     */
    @GetMapping("/getTimeTableByWeekday")
    @Operation(
            summary = "按星期查询时间表",
            description = "传入星期数（1-7），查询该天的所有时间段配置",
            operationId = "getTimeTableByWeekday"
    )
    @Parameters({
            @Parameter(
                    name = "weekDay",
                    in = ParameterIn.QUERY,
                    description = "星期几（1=周一，2=周二...7=周日）",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "7")
            )
    })
    public R<List<TimeTable>> getTimeTableByWeekday(int weekDay) {
        return R.Success(timeTableService.getTimeTableByWeekday(weekDay));
    }

    /**
     * 查询整周的时间表
     * @return Key=星期数（1-7），Value=对应星期的时间表列表
     */
    @GetMapping("/getWeeklyTimeTable")
    @Operation(
            summary = "查询整周时间表",
            description = "查询一周（1-7）所有天的时间段配置，返回按星期分组的Map结构",
            operationId = "getWeeklyTimeTable"
    )
    public R<Map<Integer, List<TimeTable>>> getWeeklyTimeTable() {
        return R.Success(timeTableService.getWeeklyTimeTable());
    }

    /**
     * 保存/更新时间段配置
     * @return 操作结果提示（如"保存成功"）
     */
    @PostMapping("/saveTimeTable")
    @Operation(
            summary = "保存时间段配置",
            description = "批量保存/更新时间段列表，支持新增和修改",
            operationId = "saveTimeTable"
    )
    public R<Void> saveTimeTable(@RequestBody TimeSlotListVO slotListPojo) {
        timeTableService.saveTimeTable(slotListPojo);
        return R.Success();
    }
}
