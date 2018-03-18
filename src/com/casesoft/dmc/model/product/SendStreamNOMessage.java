package com.casesoft.dmc.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/1/17.
 */
@Entity
@Table(name = "PRODUCT_SEND_SendStreamNO")
public class SendStreamNOMessage {
    @Id
    @Column()
    private String id;
    @Column()
    private String billNo;
    @Column()
    private String customName;
    @Column()
    private String phone;
    @Column()
    private String address;
    @Column()
    private String streamNO;
    @Column()
    private String pushsuccess;
    @Column()
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreamNO() {
        return streamNO;
    }

    public void setStreamNO(String streamNO) {
        this.streamNO = streamNO;
    }

    public String getPushsuccess() {
        return pushsuccess;
    }

    public void setPushsuccess(String pushsuccess) {
        this.pushsuccess = pushsuccess;
    }
}
