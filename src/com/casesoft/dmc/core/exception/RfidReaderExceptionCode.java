package com.casesoft.dmc.core.exception;

/**
 * Created by yushen on 2018/5/31.
 */
public class RfidReaderExceptionCode {
    public static final Integer NoReturn = 500;  //HTTP请求无返回数据
    public static final Integer RFIDReaderAccessError = 501;  //访问RFID读卡器错误
    public static final Integer NoResultData = 502;  //有返回数据，返回值的结果为空
    public static final Integer ErrorDataHeader = 600;  //数据报头错误
    public static final Integer SumCheckError = 601;  //校验和错误
    public static final Integer ReturnError = 609; //返回错误结果
}
