package com.casesoft.dmc.core.vo;

/**
 * ComboBox VO实体类，对应Web UI的ComboBox
 * @author Administrator
 *
 */
public class ComboBoxVo {

	private long id;
	private String text;
	private boolean selected;	
	
	public ComboBoxVo() {
		super();
	}
	public ComboBoxVo(long id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	public ComboBoxVo(long id, String text, boolean selected) {
		super();
		this.id = id;
		this.text = text;
		this.selected = selected;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
