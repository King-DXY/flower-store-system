package com.flowershop.common;

/**
 * 统一错误码枚举
 * <p>
 * 所有 API 响应都使用这里定义的错误码，方便前端统一处理。
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "未登录或 token 已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数校验失败"),
    BUSINESS_ERROR(500, "业务异常"),
    SYSTEM_ERROR(501, "系统内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
