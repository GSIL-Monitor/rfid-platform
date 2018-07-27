package com.casesoft.dmc.model.cfg.VO;

import java.util.List;

/**
 * Created by yushen on 2018/5/25.
 * 用于jsTree控件
 */
public class TreeVO {
    private String id;
    private String text;//树节点显示的内容
    private String icon;//树节点显示的图标
    private List<TreeVO> children;//子节点
    private State state;//节点是否展开
    private String img;//预留
    private String tinyImg;//预留
    private String fontColor;//节点字体颜色
    private String deep;//深度

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

    public String getDeep() {
        return deep;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }
}
