package com.casesoft.dmc.model.factory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="factory_category") 
public class FactoryCategory implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="code",length=30)
	private String code;
	@Column(name="name",unique=true,length=30)
	private String name;
	public FactoryCategory(){}
	public FactoryCategory(String code, String name) {
		
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
