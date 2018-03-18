package com.casesoft.dmc.extend.api.wechat.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Alvin on 2018/3/5.
 * 类名: SNSUserInfo </br>
 * 描述: 通过网页授权获取的用户信息 </br>
 * 开发人员： Alvin </br>
 * 发布版本：V1.0  </br>
 */
@Entity
@Table(name = "LOGISTICS_SNSUserInfo")
public class SNSUserInfo {
    // 用户标识
    @Column()
    private String openId;
    // 用户昵称
    @Column()
    private String nickname;
    // 性别（1是男性，2是女性，0是未知）
    @Column()
    private int sex;
    // 国家
    @Column()
    private String country;
    // 省份
    @Column()
    private String province;
    // 城市
    @Column()
    private String city;
    // 用户头像链接
    @Column()
    private String headImgUrl;
    //用户唯一编码
    @Id
    @Column()
    private String unionid;
    // 用户特权信息
    @Transient
    private List<String> privilegeList;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public List<String> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<String> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
