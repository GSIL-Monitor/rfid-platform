package com.casesoft.dmc.model.syn;

/**
 * Created by pc on 2016-12-19.
 * 配置状态
 */
public class Config {
    public static class ConfigState{
        public static final Integer Ready=0;
        public static final Integer Running=1;
    }
    public static class ConfigType{
        public static final Integer BasicInfo=0;
        public static final Integer Timer=1;
    }
}
