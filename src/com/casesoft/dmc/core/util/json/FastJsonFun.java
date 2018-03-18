package com.casesoft.dmc.core.util.json;

import com.alibaba.fastjson.JSONAware;

/**
 * Created by WingLi on 2014/10/12 0012.
 */
public class FastJsonFun implements JSONAware{
    private String functionString;

    public FastJsonFun() {
    }

    public FastJsonFun(String functionString) {
        this.functionString = functionString;
    }

    @Override
    public String toJSONString() {
        return this.functionString;
    }

    public String getFunctionString() {
        return functionString;
    }

    public void setFunctionString(String functionString) {
        this.functionString = functionString;
    }
}
