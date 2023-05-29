package com.quick.start.demo.framework.exception;


import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Data
public class ApiError {

    private static String LOCALE_CHINESE = "zh-Ch";
    private static String LOCALE_ENGLISH = "en";

    private  String errorName;
    private  String errorMsg;
    private  int errorCode;

    public ApiError(String errorName, String errorMsg, int errorCode) {
        this.errorName = errorName;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

//    public static ApiError from(String errorName, String errorMsg, int errorCode) {
//        return new ApiError(errorName,errorMsg,errorCode);
//    }

    public static ApiError from(ApiErrorEnum apiErrorEnum){
        Locale locale = LocaleContextHolder.getLocale();
        String errorMsg = LOCALE_ENGLISH.equalsIgnoreCase(locale.getLanguage())?apiErrorEnum.errorMsg:apiErrorEnum.errorMsgZh;
        return new ApiError(apiErrorEnum.errorName, errorMsg, apiErrorEnum.errorCode);
    }

}
