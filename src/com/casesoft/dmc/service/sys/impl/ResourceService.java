package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.dao.sys.ResourceDao;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.service.sys.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
// 默认将类中的所有函数纳入事务管理.
@Transactional
public class ResourceService implements IResourceService {
  @Autowired
  private ResourceDao resourceDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#save(com.casesoft.dms.model.sys.Menu)
   */
  @Override
  public void save(Resource res) {
    this.resourceDao.saveOrUpdate(res);
  }

  public Resource load(String id) {
    return this.resourceDao.load(id);
  }

  public Resource get(String propertyName,String value){
    return this.resourceDao.findUniqueBy(propertyName,value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#getMenusByRole(com.casesoft.dms.model.sys.Role)
   */
  // @Override
  // @Transactional(readOnly = true)
  // public Map<Resource,List<Resource>> getResourceByRole(Role role) {
  // return getResourceByRole(role.getId());
  // }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#getMenusByRole(long)
   */

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
   * @see com.casesoft.dms.service.sys.IMenuService#getAllMenus()
   */
  @Override
  @Transactional(readOnly = true)
  public Map<Resource, List<Resource>> getResourceMap() {
    return countMenu(this.resourceDao.getAll());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#getMenuList()
   */
  @Override
  @Transactional(readOnly = true)
  public List<Resource> getResourceList() {
    return this.resourceDao.getAll();
  }
  @Transactional(readOnly = true)
  public List<Resource> getSelectedResourceList() {
    return this.resourceDao.findBy("status",1);
  }

  private Map<Resource, List<Resource>> countMenu(List<Resource> menuList) {
    Map<Resource, List<Resource>> menuMap = new HashMap<Resource, List<Resource>>();
    List<Resource> childrenMenuList = null;
    for (Resource m : menuList) {
      // 根节点编码为01
      if (m.getOwnerId().length() <= Constant.Sys.Menu_Code_Min_Length) {// <= 2
        childrenMenuList = new ArrayList<Resource>();
        for (Resource c : menuList) {
          if (c.getOwnerId().equals(m.getCode())) {// 如果某节点父节点为该节点编码，则为其孩子节点
            childrenMenuList.add(c);// 将其添加到孩子列表
          }
        }
        menuMap.put(m, childrenMenuList);
      }
    }

    return menuMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#getMenuDao()
   */
  @Override
  public ResourceDao getResourceDao() {
    return resourceDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IMenuService#setMenuDao(com.casesoft.dms.dao.sys.MenuDao)
   */
  @Override
  public void setResourceDao(ResourceDao resourceDao) {
    this.resourceDao = resourceDao;
  }

  public List<Resource> getWXResourceByRole(String roleId) {
    List<Resource> authMenuList = this.resourceDao.find(
            "select m from RoleRes auth,Resource m where auth.roleId=? and auth.resId=m.code and m.wxUrl is not null",
            new Object[] { roleId });
    return authMenuList;
  }

  @Transactional(readOnly = true)
  public List<Resource> getResourceKeyByOwnerId(String ownerId){
    String hql = "from Resource r where r.ownerId=?";
    return this.resourceDao.find(hql,new Object[]{ownerId});
  }

    public String findMaxByCode(String ownerId){
    String hql = "select max(code)from Resource where ownerId =?";
    int maxCode = Integer.parseInt(this.resourceDao.findUnique(hql,ownerId))+1;
    return "0"+String.valueOf(maxCode);
  }

  public int findMaxBySeqNo(String ownerId){
    String hql = "select max(seqNo)from Resource where ownerId =?";
    int maxSeqNo = this.resourceDao.findUnique(hql,ownerId);
    return maxSeqNo+1;
  }

  public void deleteById(String code){
    this.resourceDao.delete(code);
  }

  public void deleteAndSave(String code,Resource res){
    deleteById(code);
    save(res);
  }

}
