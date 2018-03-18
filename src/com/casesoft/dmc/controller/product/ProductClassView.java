package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.cfg.PropertyType;

import java.util.List;

/**
 * Created by WingLi on 2016-01-21.
 */
public class ProductClassView {
    private String class1Visibility;
    private String class1Name;

    private String class2Visibility;
    private String class2Name;

    private String class3Visibility;
    private String class3Name;

    private String class4Visibility;
    private String class4Name;

    private String class5Visibility;
    private String class5Name;

    private String class6Visibility;
    private String class6Name;

    private String class7Visibility;
    private String class7Name;

    private String class8Visibility;
    private String class8Name;

    private String class9Visibility;
    private String class9Name;

    private String class10Visibility;
    private String class10Name;


    public String getClass6Name() {
        return class6Name;
    }

    public void setClass6Name(String class6Name) {
        this.class6Name = class6Name;
    }

    public String getClass6Visibility() {
        return class6Visibility;
    }

    public void setClass6Visibility(String class6Visibility) {
        this.class6Visibility = class6Visibility;
    }

    public String getClass5Name() {
        return class5Name;
    }

    public void setClass5Name(String class5Name) {
        this.class5Name = class5Name;
    }

    public String getClass5Visibility() {
        return class5Visibility;
    }

    public void setClass5Visibility(String class5Visibility) {
        this.class5Visibility = class5Visibility;
    }

    public String getClass4Name() {
        return class4Name;
    }

    public void setClass4Name(String class4Name) {
        this.class4Name = class4Name;
    }

    public String getClass4Visibility() {
        return class4Visibility;
    }

    public void setClass4Visibility(String class4Visibility) {
        this.class4Visibility = class4Visibility;
    }

    public String getClass3Name() {
        return class3Name;
    }

    public void setClass3Name(String class3Name) {
        this.class3Name = class3Name;
    }

    public String getClass3Visibility() {
        return class3Visibility;
    }

    public void setClass3Visibility(String class3Visibility) {
        this.class3Visibility = class3Visibility;
    }

    public String getClass2Name() {
        return class2Name;
    }

    public void setClass2Name(String class2Name) {
        this.class2Name = class2Name;
    }

    public String getClass2Visibility() {
        return class2Visibility;
    }

    public void setClass2Visibility(String class2Visibility) {
        this.class2Visibility = class2Visibility;
    }

    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    public String getClass1Visibility() {
        return class1Visibility;
    }

    public void setClass1Visibility(String class1Visibility) {
        this.class1Visibility = class1Visibility;
    }

    public String getClass8Name() {
        return class8Name;
    }

    public void setClass8Name(String class8Name) {
        this.class8Name = class8Name;
    }

    public String getClass8Visibility() {
        return class8Visibility;
    }

    public void setClass8Visibility(String class8Visibility) {
        this.class8Visibility = class8Visibility;
    }

    public String getClass7Name() {
        return class7Name;
    }

    public void setClass7Name(String class7Name) {
        this.class7Name = class7Name;
    }

    public String getClass7Visibility() {
        return class7Visibility;
    }

    public void setClass7Visibility(String class7Visibility) {
        this.class7Visibility = class7Visibility;
    }

    public String getClass10Name() {
        return class10Name;
    }

    public void setClass10Name(String class10Name) {
        this.class10Name = class10Name;
    }

    public String getClass10Visibility() {
        return class10Visibility;
    }

    public void setClass10Visibility(String class10Visibility) {
        this.class10Visibility = class10Visibility;
    }

    public String getClass9Name() {
        return class9Name;
    }

    public void setClass9Name(String class9Name) {
        this.class9Name = class9Name;
    }

    public String getClass9Visibility() {
        return class9Visibility;
    }

    public void setClass9Visibility(String class9Visibility) {
        this.class9Visibility = class9Visibility;
    }

    public void setProductClass(List<PropertyType> codeTypeList) {

        if(CommonUtil.isBlank(codeTypeList)) {
            return;
        }
        for(PropertyType codeType : codeTypeList) {
            switch (codeType.getKeyId()) {
                case "C1":
                    setClass1Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass1Name(codeType.getValue());
                    break;
                case "C2":
                    setClass2Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass2Name(codeType.getValue());
                    break;
                case "C3":
                    setClass3Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass3Name(codeType.getValue());
                    break;
                case "C4":
                    setClass4Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass4Name(codeType.getValue());
                    break;
                case "C5":
                    setClass5Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass5Name(codeType.getValue());
                    break;
                case "C6":
                    setClass6Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass6Name(codeType.getValue());
                    break;
                case "C7":
                    setClass7Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass7Name(codeType.getValue());
                    break;
                case "C8":
                    setClass8Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass8Name(codeType.getValue());
                    break;
                case "C9":
                    setClass9Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass9Name(codeType.getValue());
                    break;
                case "C10":
                    setClass10Visibility(!"N".equals(codeType.getIsUse())?"visible":"hidden");
                    setClass10Name(codeType.getValue());
                    break;
            }
        }
    }
}
