package com.qqb.maidshop.exception;

import com.qqb.maidshop.common.BaseResponse;
import com.qqb.maidshop.common.ErrorCode;
import com.qqb.maidshop.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e) {
        log.info("业务异常");
        return ResultUtils.error(e);
    }
}
