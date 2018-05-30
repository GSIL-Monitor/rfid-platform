package com.casesoft.dmc.model.cfg.VO;

/**
 * Created by yushen on 2018/5/25.
 */
public class State {
    private Boolean opened;

    public State(){}

    public State(Boolean opened){
        this.opened = opened;
    }

    public Boolean isOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
}
