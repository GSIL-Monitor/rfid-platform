package com.casesoft.dmc.model.product;




public class CountResult implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long count;
	private Double average;
	public CountResult(Long count , Double average){
		this.count = count;
		this.average = average;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	

}