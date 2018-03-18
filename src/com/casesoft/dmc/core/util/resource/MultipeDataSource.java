package com.casesoft.dmc.core.util.resource;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipeDataSource extends AbstractRoutingDataSource {
	static Logger log=Logger.getLogger("MultipeDataSource");
	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		// TODO Auto-generated method stub
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		// TODO Auto-generated method stub
		super.setTargetDataSources(targetDataSources);
	}
	 
	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		return CustomerContextHolder.getCustomerType();
	}

}
