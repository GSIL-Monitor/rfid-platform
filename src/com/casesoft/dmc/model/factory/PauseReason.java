package com.casesoft.dmc.model.factory;

import javax.persistence.*;

/**
 * Created by Alvin-PC on 2017/4/14 0014.
 */
@Entity
@Table(name="factory_PauseReason")
public class PauseReason implements java.io.Serializable{
    @Id
    @Column(length=50)
    private String id;
    @Column()
    private Integer token;

    @Column(nullable=true,length = 2000)
    private String reason;
    @Column(length=20)
    private String upDateTime;

    @Transient
    private String tokenName;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }
}
