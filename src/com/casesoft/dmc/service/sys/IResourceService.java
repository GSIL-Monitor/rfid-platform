package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.dao.sys.ResourceDao;
import com.casesoft.dmc.model.sys.Resource;

import java.util.List;
import java.util.Map;

public interface IResourceService {

	public abstract void save(Resource resource);

	/**
	 * 以后将数据放到缓存中
	 * @param roleId
	 * @return
	 */
	public abstract Map<Resource, List<Resource>> getResourceMap();
	
	public List<Resource> getResourceByRole(String roleId);

	public abstract List<Resource> getResourceList();

	public abstract ResourceDao getResourceDao();

	public abstract void setResourceDao(ResourceDao menuDao);

}