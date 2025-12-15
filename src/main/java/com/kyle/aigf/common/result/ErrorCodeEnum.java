package com.kyle.aigf.common.result;

import lombok.Getter;

/**
 * 全局错误码枚举
 * 错误码规则：
 * 1. 200：成功
 * 2. 4xx：客户端错误（400=参数错误，401=未授权，403=禁止访问，404=资源不存在）
 * 3. 5xx：服务端错误（500=系统异常，501=接口未实现，503=服务不可用）
 * 4. 1xxx：业务模块错误（1001=用户模块，1101=订单模块，1201=时间表模块，1301=专注日志模块）
 */
@Getter
public enum ErrorCodeEnum {

    // ===================== 通用状态码 =====================
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // ===================== 客户端错误 =====================
    PARAM_ERROR(400, "参数格式错误"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    FORBIDDEN(403, "无权限访问"),
    RESOURCE_NOT_FOUND(404, "请求资源不存在"),

    // ===================== 服务端错误 =====================
    SYSTEM_EXCEPTION(500, "系统内部异常，请稍后重试"),
    INTERFACE_UNIMPLEMENTED(501, "接口未实现"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // ===================== 业务模块错误（示例） =====================
    // 时间表模块
    TIME_TABLE_WEEKDAY_ERROR(1201, "星期数超出范围（1-7）"),
    TIME_TABLE_SAVE_ERROR(1202, "时间表保存失败"),
    TIME_TABLE_EMPTY_ERROR(1203, "时间表数据为空"),

    // 专注日志模块
    FOCUS_LOG_TIME_FORMAT_ERROR(1301, "时间格式错误（正确格式：yyyy-MM-dd HH:mm:ss）"),
    FOCUS_LOG_TIME_RANGE_ERROR(1302, "开始时间不能晚于结束时间"),
    FOCUS_LOG_TOPK_ERROR(1303, "TopK值必须大于0且小于等于100"),
    PROCESS_ALREADY_EXIST(1304, "该进程已存在")
    ;



    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误提示信息
     */
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}