package com.qqb.maidshop.common;

import com.qqb.maidshop.exception.BusinessException;

public class ResultUtils {

    /***
     * 成功
     * @return
     * @param <T>
     */
    public static <T> BaseResponse <T> success (T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    public static <T> BaseResponse <T> success () {
        return new BaseResponse<>(0, null);
    }

    public static <T> BaseResponse <T> error (ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public static <T> BaseResponse <T> error (BusinessException e) {
        return new BaseResponse<>(e.getCode(), null, e.getMessage(), e.getDescription());
    }
}
