package com.casesoft.dmc.model.logistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/11/9.
 */
@Entity
@Table(name = "LOGISTICS_RecordReturnScore")
public class RecordReturnScore {
    @Id
    @Column()
    private String id;
    @Column()
    private String retrunDetailid;
    @Column()
    private String code;
    @Column()
    private String billno;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetrunDetailid() {
        return retrunDetailid;
    }

    public void setRetrunDetailid(String retrunDetailid) {
        this.retrunDetailid = retrunDetailid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }
}
