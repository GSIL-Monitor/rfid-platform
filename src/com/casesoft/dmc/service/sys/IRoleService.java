package com.casesoft.dmc.service.sys;

import java.util.List;

import com.casesoft.dmc.dao.sys.RoleDao;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.User;

public interface IRoleService {

	public abstract Role getRoleByUser(User user);

	public abstract List<Role> getAllRoles();

	public abstract String saveOrUpdate(Role role);

	public abstract Role getRole(String roleId);

	public abstract String deleteRole(Role role);


	public abstract RoleDao getRoleDao();

	public abstract void setRoleDao(RoleDao roleDao);

	Role load(String id);

}