package com.kyle.aigf.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局统一返回结果类
 *
 * @param <T> 数据体类型
 */
@Data
@Schema(name = "ApiResponse", description = "接口统一返回结果")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码（200=成功，其他=失败）
     */
    @Schema(description = "响应码（200=成功，其他=失败）", example = "200")
    private int code;

    /**
     * 响应提示信息
     */
    @Schema(description = "响应提示信息", example = "操作成功")
    private String msg;

    /**
     * 响应数据体
     */
    @Schema(description = "响应数据体")
    private T data;

    /**
     * 私有化构造器，禁止外部直接创建
     */
    private R() {}

    /**
     * 通用构造方法
     */
    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ===================== 快速构建方法 =====================

    /**
     * 成功返回（无数据）
     */
    public static <T> R<T> Success() {
        return new R<>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> R<T> Success(T data) {
        return new R<>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功返回（自定义提示语 + 数据）
     */
    public static <T> R<T> Success(String msg, T data) {
        return new R<>(ErrorCodeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败返回（基于错误枚举）
     */
    public static <T> R<T> Fail(ErrorCodeEnum errorCodeEnum) {
        return new R<>(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), null);
    }

    /**
     * 失败返回（自定义错误码 + 提示语）
     */
    public static <T> R<T> Fail(int code, String msg) {
        return new R<>(code, msg, null);
    }

    /**
     * 失败返回（仅自定义提示语，使用默认失败码）
     */
    public static <T> R<T> Fail(String msg) {
        return new R<>(ErrorCodeEnum.FAIL.getCode(), msg, null);
    }

    // ===================== 链式调用方法（可选） =====================
    public R<T> code(int code) {
        this.code = code;
        return this;
    }

    public R<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public R<T> data(T data) {
        this.data = data;
        return this;
    }
}