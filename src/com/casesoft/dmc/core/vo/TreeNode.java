package com.casesoft.dmc.core.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeNode {

	private String id;
	private String text;
	private String iconCls;//小图标
	private boolean checked;
	
	private String state = "open";
	
	private Map<String, Object> attributes;//其他参数
	
	private List<TreeNode> children;
	
	public TreeNode() {
		super();
	}

	public TreeNode(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

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

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public void addChild(TreeNode child) {
		if(null == children) {
			children = new ArrayList<TreeNode>();
		}
		children.add(child);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	
			
}
