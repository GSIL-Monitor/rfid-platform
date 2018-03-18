package com.casesoft.dmc.model.location;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/1/19.
 */
@Entity
@Table(name = "LOCATION_Regional")
public class Regional {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator = "S_REGIONAL")
    @SequenceGenerator(name = "S_REGIONAL", allocationSize = 1, initialValue = 1, sequenceName = "S_REGIONAL")
    private Long id;
    @Column()
    private String name;
    @Column()
    private String remark;
    @Column()
    private String province;
    @Column()
    private String provinceName;
    @Column()
    private String cityName;
    @Column()
    private String areaName;
    @Column()
    private String ownerid;

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column()
    private String city;
    @Column()
    private String area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
