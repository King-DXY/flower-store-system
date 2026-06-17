package com.flowershop.common;

/**
 * 统一响应体
 * <p>
 * 所有 Controller 的返回值都用 Result<T> 包装，
 * 前端收到的 JSON 格式始终是 {code, message, data, timestamp}。
 *
 * @param <T> 响应数据的类型
 */
public class Result<T> {

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功响应 ====================

    /** 成功（有数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /** 成功（无数据） */
    public static <T> Result<T> success() {
        return success(null);
    }

    /** 成功（自定义消息） */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    // ==================== 失败响应 ====================

    /** 失败（使用预定义错误码） */
    public static <T> Result<T> error(ResultCode code) {
        return new Result<>(code.getCode(), code.getMessage(), null);
    }

    /** 失败（自定义错误信息） */
    public static <T> Result<T> error(ResultCode code, String message) {
        return new Result<>(code.getCode(), message, null);
    }

    /** 失败（完全自定义） */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    // ==================== Getter / Setter ====================

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
