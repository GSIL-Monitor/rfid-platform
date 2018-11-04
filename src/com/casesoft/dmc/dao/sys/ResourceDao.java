package com.casesoft.dmc.dao.sys;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.sys.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceDao extends BaseHibernateDao<Resource, String> {

//	public List<Components> findAllComponents() {
//		return this.find("from Components c");
//	}
//	
//	public List<Components> findAllComponentsByRole(Role role) {
//		String hql = "select c from Components as c,ComponentAuthorize as compAuth where c.partId=compAuth.partId and "+
//		    "compAuth.roleId=?";
//		return this.find(hql,new Object[]{role.getRoleId()});
//	}
//
//	public List<String> getComponentsByRoleAndMenu(long roleId, String menuId) {
//		String hql = "select c.componentId from Components as c,ComponentAuthorize as compAuth where c.menuId=compAuth.menuId and compAuth.partId=c.partId and compAuth.roleId=? and compAuth.menuId=?";
//		return this.find(hql,new Object[]{roleId,menuId});
//	}
}
