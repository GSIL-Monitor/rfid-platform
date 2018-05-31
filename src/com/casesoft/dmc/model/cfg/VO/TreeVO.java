package com.casesoft.dmc.model.cfg.VO;

import java.util.List;

/**
 * Created by yushen on 2018/5/25.
 * 用于jsTree控件
 */
public class TreeVO {
    private String id;
    private String text;
    private String icon;
    private List<TreeVO> children;
    private State state;
    private String img;
    private String tinyImg;
    private String fontColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<TreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeVO> children) {
        this.children = children;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTinyImg() {
        return tinyImg;
    }

    public void setTinyImg(String tinyImg) {
        this.tinyImg = tinyImg;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
