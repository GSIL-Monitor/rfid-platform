package com.casesoft.dmc.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Alvin on 2018/1/19.
 * 省市县行政区信息
 */
@Entity
@Table(name = "LOCATION_AdministrativeRegion")
public class AdministrativeRegion {

    @Id
    @Column(length=50)
    private String Id; //编号

    @Column(length = 50)
    private String parentId;//父级Id

    @Column(length = 50)
    private String name;//名称

    @Column()
    private String mergerName;//国家，省，市，县（区）拼接

    @Column()
    private String shortName;
    @Column()
    private String mergerShortName;

    @Column()
    private Integer levelType;//类型 0国家，1省，2市，3县（区）

    @Column()
    private String cityCode;//电话区号

    @Column()
    private String zipCode;//邮编

    @Column()
    private String pinYin;//拼音

    @Column()
    private String jianPin;//简拼

    @Column()
    private String ing;//经度

    @Column()
    private String lat;//纬度

    @Column()
    private String remarks; //备注信息

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMergerShortName() {
        return mergerShortName;
    }

    public void setMergerShortName(String mergerShortName) {
        this.mergerShortName = mergerShortName;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getJianPin() {
        return jianPin;
    }

    public void setJianPin(String jianPin) {
        this.jianPin = jianPin;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
