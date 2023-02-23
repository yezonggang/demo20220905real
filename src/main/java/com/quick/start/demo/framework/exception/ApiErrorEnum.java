package com.quick.start.demo.framework.exception;

public enum ApiErrorEnum {


    CHECK_DATABASE_WRONG("CHECK_DATABASE_WRONG","get data from database wrong",202200),
    TOKEN_EXPIRED("Token expired","get data from database wrong",50014),
    HAVE_NO_TOKEN ("have no token","have no token or token expired",50021),
    ACCESS_EXCEPTION("interface access exception","interface access exception",200);


    ApiErrorEnum(String errorName, String errorMsg, int errorCode){
        this.errorName = errorName;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public String errorName;
    public String errorMsg;
    public int errorCode;

}
