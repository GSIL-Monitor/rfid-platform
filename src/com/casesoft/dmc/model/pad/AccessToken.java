package com.casesoft.dmc.model.pad;

/**
 * @author ltc
 * @create 2018-06-07
 * @desc 封装AccessToken的实体
 **/
public class AccessToken {
    private String token;
    private int expireIn;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getExpireIn() {
        return expireIn;
    }
    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}
