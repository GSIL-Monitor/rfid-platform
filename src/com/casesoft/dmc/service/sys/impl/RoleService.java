package com.casesoft.dmc.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import com.casesoft.dmc.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.ResourceDao;
import com.casesoft.dmc.dao.sys.RoleDao;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.RoleRes;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.IRoleService;

@Service
@Transactional
public class RoleService implements IRoleService {
  @Autowired
  private RoleDao roleDao;
  

  @Autowired
  private ResourceDao resourceDao;
  
  
  public Page<Role> findPage(Page<Role> page, List<PropertyFilter> filters) {
    return this.roleDao.findPage(page, filters);
  }

  
  public void saveRoleRes(RoleRes roleRes) {
      this.roleDao.saveOrUpdateX(roleRes);
  }
    public void deleteRoleRes(RoleRes roleRes) {
        this.roleDao.batchExecute("delete RoleRes rr where rr.roleId=? and rr.resId=?",
                roleRes.getRoleId(),roleRes.getResId());
    }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#getRoleByUser(com.casesoft.dms.model.sys.User)
   */
  @Override
  @Transactional(readOnly = true)
  public Role getRoleByUser(User user) {
    String hql = "select role from Role role,User u where role.id=u.type and u.id=?";
    Role role = this.roleDao.findUnique(hql, new Object[] { user.getId() });
    return role;
  }
  
 

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#getAllRoles()
   */
  @Override
  @Transactional(readOnly = true)
  public List<Role> getAllRoles() {
    return this.roleDao.getAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#saveOrUpdate(com.casesoft.dms.model.sys.Role)
   */
  @Override
  public String saveOrUpdate(Role role) {
    this.roleDao.saveOrUpdate(role);
      if(CommonUtil.isNotBlank(role.getAuthIds())) {
          List<RoleRes> rrList = new ArrayList<RoleRes>();
          String[] authIds = role.getAuthIds().split(",");
          for (String str : authIds) {
              RoleRes rr = new RoleRes(role.getId(), str);
              rr.setId(role.getId() + "-" + str);//rr.setId(new GuidCreator().toString());
              rrList.add(rr);
          }
          // wing 2014-04-04
          this.roleDao.batchExecute("delete RoleRes rr where rr.roleId=?", role.getId());
          this.roleDao.doBatchInsert(rrList);
      }
    return role.getCode();
  }

  public void update(Role entity) {
    this.roleDao.saveOrUpdate(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public Role getRole(String roleId) {
    return this.roleDao.get(roleId);
  }

  @Transactional(readOnly = true)
  public List<RoleRes> findAllRoleRes() {
    return roleDao.find("from RoleRes rr", new Object[] {});
  }

  @Transactional(readOnly = true)
  public List<RoleRes> findAllRoleRes(String roleId) {
    return roleDao.find("from RoleRes rr where rr.roleId", new Object[] { roleId });
  }

    @Transactional(readOnly = true)
    public List<Resource> getResourceByRole(String roleId) {
        List<Resource> authMenuList = this.resourceDao.find(
                "select m from RoleRes auth,Resource m where auth.roleId=? and auth.resId=m.code",
                new Object[] { roleId });
        return authMenuList;
    }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#deleteRole(com.casesoft.dms.model.sys.Role)
   */
  @Override
  public String deleteRole(Role role) {
    this.roleDao.delete(role.getId());
    return role.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#getRoleDao()
   */
  @Override
  public RoleDao getRoleDao() {
    return roleDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IRoleService#setRoleDao(com.casesoft.dms.dao.sys.RoleDao)
   */
  @Override
  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  @Override
  public Role load(String id) {
    return this.roleDao.load(id);
  }

    public Role findUnique(String code) {
        return this.roleDao.findUniqueBy("code",code);
    }

    public List<Role> find(List<PropertyFilter> filter){
        return this.roleDao.find(filter);
    }
}

