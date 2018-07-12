package com.casesoft.dmc.model.pad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ltc
 * @create 2018-06-18
 * @desc 对于微信用户本身存在的信息的一个javabean
 **/
@Entity
@Table(name = "WEIXIN_USER")
public class WeiXinUser {
    // 用户的标识
    @Id
    @Column(nullable = false,length = 50)
    private String openId;
    // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    @Column(length = 1)
    private int subscribe;
    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    @Column(length = 10)
    private String subscribeTime;
    // 昵称
    @Column(length = 50)
    private String nickname;
    // 用户的性别（1是男性，2是女性，0是未知）
    @Column(length = 1)
    private int sex;
    // 用户所在国家
    @Column(length = 50)
    private String country;
    // 用户所在省份
    @Column(length = 50)
    private String province;
    // 用户所在城市
    @Column(length = 50)
    private String city;
    // 用户的语言，简体中文为zh_CN
    @Column(length = 50)
    private String language;
    //微信用户id
    @Column(length = 50)
    private String unionId;
    //微信绑定手机号
    @Column(length = 11)
    private String phone;

    public String getUnionId() {
        return unionId;
    }
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public int getSubscribe() {
        return subscribe;
    }
    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }
    public String getSubscribeTime() {
        return subscribeTime;
    }
    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
