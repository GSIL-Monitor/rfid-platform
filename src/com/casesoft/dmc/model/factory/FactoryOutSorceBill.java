package com.casesoft.dmc.model.factory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Factory_OutSorce_Bill") 
public class FactoryOutSorceBill extends FactoryTask implements java.io.Serializable{
   
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="isIn",length=1)
	private String isIn;
    
	@Column(name="in_TaskId",length=50)
	private String inTaskId;
	
    public FactoryOutSorceBill() {
		
	}
    public FactoryOutSorceBill(FactoryTask task) {
    	super();
		this.setTaskId(task.getTaskId());
		this.setDeviceId(task.getDeviceId());
		this.setFactory(task.getFactory());
		this.setTaskId(task.getTaskId());
		this.setTaskTime(task.getTaskTime());
		this.setToken(task.getToken());
		this.setQty(task.getQty());
		this.setType(task.getType());
		this.setOperator(task.getOperator());
		this.setOutTaskId(task.getTaskId());
	}

	public FactoryOutSorceBill(String isIn) {
		super();
		this.isIn = isIn;
	}
	public FactoryOutSorceBill(String isIn, String inTaskId) {
			super();
			this.isIn = isIn;
			this.inTaskId = inTaskId;
	}
	public String getIsIn() {
		return isIn;
	}

	public void setIsIn(String isIn) {
		this.isIn = isIn;
	}
	
	public String getInTaskId() {
		return inTaskId;
	}

	public void setInTaskId(String inTaskId) {
		this.inTaskId = inTaskId;
	}

    @Transient
    private String billNos;

    public String getBillNos() {
        return billNos;
    }

    public void setBillNos(String billNos) {
        this.billNos = billNos;
    }
}
