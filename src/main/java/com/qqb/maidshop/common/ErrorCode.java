package com.qqb.maidshop.common;

public enum ErrorCode {

    SUCCESS(0,"ok",""),
    NULL_ERROR(40002,"数据为空",""),
    NULL_PARAMS(40001,"参数为空","");

    private Integer code;

    private String message;

    private String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
