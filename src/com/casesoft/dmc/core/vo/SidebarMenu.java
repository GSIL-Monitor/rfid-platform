package com.casesoft.dmc.core.vo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by WingLi on 2016-12-09.
 */
public class SidebarMenu implements java.io.Serializable {

    private String id;
    private String text;

    private String icon;//小图标
    private String url;
    private String wxUrl;//小程序链接
    private String image;//图片

    private List<SidebarMenu> menus;

    public List<SidebarMenu> getMenus() {
        return menus;
    }

    public void addChild(SidebarMenu menu) {
        if(null == menus) {
            menus = new ArrayList<>();
        }
        menus.add(menu);
    }

    public void setMenus(List<SidebarMenu> menus) {
        this.menus = menus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
