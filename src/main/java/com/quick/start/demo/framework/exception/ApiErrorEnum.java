package com.quick.start.demo.framework.exception;

public enum ApiErrorEnum {


    CHECK_DATABASE_WRONG("CHECK_DATABASE_WRONG","get data from database wrong","数据库查询错误",202200),
    TOKEN_EXPIRED("Token expired","token expired","token过期",50014),
    HAVE_NO_TOKEN_OR_TOKEN_EXPIRED ("have no token","have no token or token expired","没有token或者token过期",50021),
    ACCESS_EXCEPTION("interface access exception","interface access exception","权限错误",20212);


    ApiErrorEnum(String errorName, String errorMsg,String errorMsgZh, int errorCode){
        this.errorName = errorName;
        this.errorMsg = errorMsg;
        this.errorMsgZh = errorMsgZh;
        this.errorCode = errorCode;
    }

    public String errorName;
    public String errorMsg;
    public String errorMsgZh;
    public int errorCode;

}
