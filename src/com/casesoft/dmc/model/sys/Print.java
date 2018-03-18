package com.casesoft.dmc.model.sys;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/23.
 */
@Entity
@Table(name = "SYS_PRINT")
public class Print {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator = "S_PRINT")
    @SequenceGenerator(name = "S_PRINT", allocationSize = 1, initialValue = 1, sequenceName = "S_PRINT")
    private Long id;
    @Column(columnDefinition="CLOB")
    private String printCont;//lodop代码的内容
    @Column( columnDefinition="CLOB")
    private  String  printCode;//存储要显示name的字段
    @Column()
    private  String name;//对应的套的名字
    @Column( columnDefinition="CLOB")
    private String  printtabCode;//存储要显示tabname的字段
    @Column( columnDefinition="CLOB")
    private String printhtml;//存储html
    @Column()
    private String type;//单据类型
    @Column()
    private String isFranchisee;//是否是加盟商
    @Column()
    private String saveownerid;//存储ownerid;

    public String getIsFranchisee() {
        return isFranchisee;
    }

    public void setIsFranchisee(String isFranchisee) {
        this.isFranchisee = isFranchisee;
    }

    public String getSaveownerid() {
        return saveownerid;
    }

    public void setSaveownerid(String saveownerid) {
        this.saveownerid = saveownerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrinthtml() {
        return printhtml;
    }

    public void setPrinthtml(String printhtml) {
        this.printhtml = printhtml;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrintCont() {
        return printCont;
    }

    public void setPrintCont(String printCont) {
        this.printCont = printCont;
    }

    public String getPrintCode() {
        return printCode;
    }

    public void setPrintCode(String printCode) {
        this.printCode = printCode;
    }

    public String getPrinttabCode() {
        return printtabCode;
    }

    public void setPrinttabCode(String printtabCode) {
        this.printtabCode = printtabCode;
    }
}
