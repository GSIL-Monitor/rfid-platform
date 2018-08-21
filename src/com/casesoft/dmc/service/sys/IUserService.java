package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.UserDao;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.User;

import java.util.List;

public interface IUserService {

  public abstract void setUserDao(UserDao userDao);

  public abstract User getUser(String id) throws ServiceException;

  public abstract List<User> getAllUser();

  public abstract Page<User> findPage(Page<User> page, List<PropertyFilter> filters);

  public abstract User getUser(String userCode, String password) throws ServiceException;

  public abstract String saveUser(User user) throws ServiceException;

  public abstract List<User> find(List<PropertyFilter> filters);

  /**
   * 获取最大编号
   */
  public abstract String getMaxNo();
  public String getMaxNo(String prefix);

  /**
   * 获取最大编号
   */
  public abstract String getMaxNo(int type, String deviceId) throws Exception;

  /**
   * 不改变用户密码
   * 
   * @param user
   * @throws ServiceException
   */
  public abstract String updateUser(User user) throws ServiceException;

  public abstract String saveRole(Role role);

  public abstract Role getRole(String roleName);

  public abstract Role findRole(String roleId);

  public abstract String delete(String id);

  /**
   * 删除角色，并删除关联的用户和权限
   * 
   * @return
   */
  public abstract String deleteRole(String roleId);

public abstract User load(String id);

public abstract String findMaxEmployeerIndex();

}