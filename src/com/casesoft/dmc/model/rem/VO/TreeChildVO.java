package com.casesoft.dmc.model.rem.VO;


import com.casesoft.dmc.model.cfg.VO.State;

/**
 * Created by lly on 2018/7/7.
 * 用于jsTree懒加载
 */
public class TreeChildVO {
    private String id;
    private String text;//jstree节点文本显示
    private String icon;//节点图标
    private boolean children;//是否有子节点
    private State state;//是否展开
    private String img;//预留
    private String tinyImg;//预留
    private String fontColor;//字体颜色
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

    public boolean getChildren() {
        return children;
    }

    public void setChildren(boolean children) {
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
