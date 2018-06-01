package com.casesoft.dmc.core.exception;

/**
 * Created by yushen on 2018/5/31.
 */
public class RfidReaderException extends Exception {
    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    public RfidReaderException(Integer errorCode, String message, Throwable cause){
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RfidReaderException(Integer errorCode, String message){
        this(errorCode, message, null);
    }

    public RfidReaderException(Integer errorCode, Throwable cause){
        this(errorCode, null, cause);
    }
}
