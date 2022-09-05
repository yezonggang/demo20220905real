package com.quick.start.demo.framework.exception;


import lombok.Data;

@Data
public class ApiError {
    private  String errorName;
    private  String errorMsg;
    private  int errorCode;

    public ApiError(String errorName, String errorMsg, int errorCode) {
        this.errorName = errorName;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public static ApiError from(String errorName, String errorMsg, int errorCode) {
        return new ApiError(errorName,errorMsg,errorCode);
    }

    public static ApiError from(ApiErrorEnum apiErrorEnum){
        return new ApiError(apiErrorEnum.errorName, apiErrorEnum.errorMsg, apiErrorEnum.errorCode);
    }

}
