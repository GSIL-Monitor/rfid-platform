package com.casesoft.dmc.model.sys;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/5/31.
 */
@Entity
@Table(name = "SYS_PRINTSET")
public class PrintSet {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator = "S_PRINTSET")
    @SequenceGenerator(name = "S_PRINTSET", allocationSize = 1, initialValue = 1, sequenceName = "S_PRINTSET")
    private Long id;
    @Column(columnDefinition="CLOB")
    private String printCont;//lodop代码的内容
    @Column( columnDefinition="CLOB")
    private  String  printCode;//存储要显示name的字段
    @Column()
    private  String name;//对应的套的名字
    @Column()
    private String type;//单据类型
    @Column()
    private String ownerId;//打印模块的所属发

    @Column( columnDefinition="CLOB")
    private String  printFootExtend;//记录扩展打印的内容
    @Column()
    private String ruleReceipt;//小票规格
    @Column()
    private Integer commonType;//公共类型(0 公共  1 非公共)

    public Integer getCommonType() {
        return commonType;
    }

    public void setCommonType(Integer commonType) {
        this.commonType = commonType;
    }

    public String getPrintFootExtend() {
        return printFootExtend;
    }

    public void setPrintFootExtend(String printFootExtend) {
        this.printFootExtend = printFootExtend;
    }

    public String getRuleReceipt() {
        return ruleReceipt;
    }

    public void setRuleReceipt(String ruleReceipt) {
        this.ruleReceipt = ruleReceipt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
