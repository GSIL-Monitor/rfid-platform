package com.casesoft.dmc.core.vo;

import java.io.Serializable;

public class MapData implements Serializable{
    private String name;
    private long value;
    public MapData(){
    	
    }
    public MapData(String name ,long value){
    	this.setName(name);
    	this.setValue(value);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
