package com.casesoft.dmc.extend.third.descriptor;

/**
 * Created by john on 2017-02-22.
 */
public enum OperatorType {
    EQ, NEQ,//=,!=
    GT, GTE,//>,>=
    LT, LTE,//<,<=
    STARTSWITH,ENDSWITH,CONTAINS,DOESNOTCONTAIN,//
    ISNULL,ISNOTNULL,ISEMPTY,ISNOTEMPTY,
    IN,
    NOTIN
}
