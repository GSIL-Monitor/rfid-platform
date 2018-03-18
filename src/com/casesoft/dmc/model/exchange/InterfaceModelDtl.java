package com.casesoft.dmc.model.exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by WinLi on 2017-06-05.
 */
@Entity
@Table(name = "EXCHANGE_InterfaceModelDtl")
public class InterfaceModelDtl implements java.io.Serializable {
    @Id
    @Column(nullable = false, length = 50)
    private String id;
    @Column(nullable = false, length = 20)
    private String modelCode;
    @Column(nullable = false, length = 20)
    private String origField;
    @Column(nullable = false, length = 50)
    private String origDescribe;
    @Column(nullable = false, length = 20)
    private String destField;
    @Column(nullable = false, length = 50)
    private String destDescribe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getOrigField() {
        return origField;
    }

    public void setOrigField(String origField) {
        this.origField = origField;
    }

    public String getDestField() {
        return destField;
    }

    public void setDestField(String destField) {
        this.destField = destField;
    }

    public String getOrigDescribe() {
        return origDescribe;
    }

    public void setOrigDescribe(String origDescribe) {
        this.origDescribe = origDescribe;
    }

    public String getDestDescribe() {
        return destDescribe;
    }

    public void setDestDescribe(String destDescribe) {
        this.destDescribe = destDescribe;
    }
}
