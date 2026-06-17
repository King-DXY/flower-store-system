package com.flowershop.common;

/**
 * 业务异常
 * <p>
 * 在 Service 层遇到业务规则不满足时抛出此异常，
 * 由 GlobalExceptionHandler 统一捕获并返回友好的错误信息。
 * <p>
 * 使用示例：
 * <pre>
 *   if (flower.getStock() < quantity) {
 *       throw new BusinessException(ResultCode.BUSINESS_ERROR, "库存不足");
 *   }
 * </pre>
 */
public class BusinessException extends RuntimeException {

    private final ResultCode code;

    public BusinessException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR;
    }

    public ResultCode getCode() {
        return code;
    }
}
