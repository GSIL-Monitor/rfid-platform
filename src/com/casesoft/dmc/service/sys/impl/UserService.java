package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.RoleDao;
import com.casesoft.dmc.dao.sys.StaffInfoDao;
import com.casesoft.dmc.dao.sys.UserDao;
import com.casesoft.dmc.extend.api.wechat.model.SNSUserInfo;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.StaffInfo;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
// 默认将类中的所有函数纳入事务管理.
@Transactional
public class UserService implements IUserService {
  @Autowired
  private UserDao userDao;
  @Autowired
  private RoleDao roleDao;
  @Autowired
  private StaffInfoDao staffInfoDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#setUserDao(com.casesoft.dms.dao.sys.UserDao)
   */
  @Override
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public User getUserByCode(String code){
    return this.userDao.findUnique("from User u where u.code=?",new Object[]{code});
  }
  
  public List<User> getCashier()
  {
	return  this.userDao.find("from User u where u.roleId=?","SH0P01");
  }

  public List<User> getFranchisee()
  {
    return  this.userDao.find("from User u where u.roleId=?","JMSJS");
  }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#getUser(long)
   */
  @Override
  @Transactional(readOnly = true)
  public User getUser(String id) throws ServiceException {
    return this.userDao.get(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#getAllUser()
   */
  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUser() {
    return this.userDao.getAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#findPage(com.casesoft.dms.core.util.page.Page,
   * java.util.List)
   */
  @Override
  @Transactional(readOnly = true)
  public Page<User> findPage(Page<User> page, List<PropertyFilter> filters) {
    if (null == filters)
      return this.userDao.findPage(page, "from User");
    return this.userDao.findPage(page, filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#getUser(java.lang.String)
   */
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#getUser(java.lang.String, java.lang.String)
   */
  @Override
  @Transactional(readOnly = true)
  public User getUser(String userCode, String password) throws ServiceException {
    User user = null;
    try {
      user = this.userDao.findUnique("from User where code=? and password=?", userCode, password);
      if (null == user)
        throw new Exception();
    } catch (Exception ex) {
      final String message = "用户名和密码不匹配!";
      this.throwExceptionAndLogger(message, ex);
    }
    return user;
  }

    public void save(User user) throws ServiceException{
        this.userDao.saveOrUpdate(user);
    }
  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#saveUser(com.casesoft.dms.model.sys.User)
   */
  @Override
  public String saveUser(User user) throws ServiceException {
    this.userDao.saveOrUpdate(user);
    return user.getCode();
  }
  public void saveList(List<User> userList) throws ServiceException {
    this.userDao.doBatchInsert(userList);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#updateUser(com.casesoft.dms.model.sys.User)
   */
  @Override
  public String updateUser(User user) throws ServiceException {
    this.userDao.saveOrUpdate(user);
    return user.getCode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#saveRole(com.casesoft.dms.model.sys.Role)
   */
  @Override
  public String saveRole(Role role) {
    this.roleDao.saveOrUpdate(role);
    return role.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#getRole(java.lang.String)
   */
  @Override
  @Transactional(readOnly = true)
  public Role getRole(String roleName) {
    return this.userDao.findUnique("from Role where roleName=?", new Object[] { roleName });
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#findRole(long)
   */
  @Override
  @Transactional(readOnly = true)
  public Role findRole(String roleId) {
    return this.userDao.findUnique("from Role where roleId=?", new Object[] { roleId });
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#delete(long)
   */
  @Override
  public String delete(String id) {
    this.userDao.delete(id);
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.casesoft.dms.service.sys.IUserService#deleteRole(long)
   */
  @Override
  public String deleteRole(String roleId) {
    this.userDao.batchExecute("delete Role where roleId=?", roleId);
    this.userDao.batchExecute("delete User where userType=?", roleId);// 删除关联User
    this.userDao.batchExecute("delete RoleAuthorize where roleId=?", roleId);// 删除关联权限
    this.userDao.batchExecute("delete ComponentAuthorize where roleId=?", roleId);

    return roleId;
  }

  public void doBatchInsert(List<SNSUserInfo> list){
    this.userDao.doBatchInsert(list);
  }

  protected void throwExceptionAndLogger(String message, Throwable throwable)
      throws ServiceException {
    throw new ServiceException(message, throwable);
  }
  
  /**
   * 根据unit id 获取最大编号
   * @param prefix
   * @return
   */
  @Override
  public String getMaxNo(String prefix) {
	  String hql = "select max(CAST(SUBSTRING(u.id,"
                         +(prefix.length()+1)+"),integer))"
		        + " from User as u where u.id like ?";
		    Integer code = this.userDao.findUnique(hql, new Object[] { prefix+'%' });
		    return code == null ? (prefix + "001") : prefix
		        + CommonUtil.convertIntToString(code + 1, 3);
  }

  @Override
  @Transactional(readOnly = true)
  public String getMaxNo() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 根据设备号查询最大的用户编号
   */
  @Override
  @Transactional(readOnly = true)
  public String getMaxNo(int type, String deviceId) throws Exception {
    String unitFlag = "";
    String deviceFlag = deviceId.substring(4);// 4 char
    switch (type) {
    case Constant.UserType.Employeer:
      unitFlag = "" + Constant.UnitCodePrefix.Department + deviceFlag;// J不能用
      break;
    case Constant.UserType.User:
       unitFlag = ""+Constant.UnitCodePrefix.SampleRoom+deviceFlag;
      break;
    }
    String hql = "select max(CAST(SUBSTRING(user.code,"+(unitFlag.length()+1)+"),integer))"
        + " from User as user where user.isAdmin=?";
    Integer code = this.userDao.findUnique(hql, new Object[] { type });
//    String sql = "select max(cast(substr(cd, "+(unitFlag.length()+1)+") as number(10,0))) from tsyus where ia=?";
//    Integer code = Integer.parseInt(""+this.userDao.findUniqueBySQL(sql, new Object[]{type}));
    int codeLength = PropertyUtil.getIntValue("customer_code_length");
    return code == null ? (unitFlag + "001") : unitFlag
        + CommonUtil.convertIntToString(code + 1, codeLength - 5);
  }
  @Override
  @Transactional(readOnly = true)
  public String findMaxEmployeerIndex(){	 
	    String hql = "select max(CAST(user.code,integer))"
	        + " from User as user where user.isAdmin="+Constant.UserType.Employeer;
	    Integer code = this.userDao.findUnique(hql);
	    return code == null ? ("000001") : CommonUtil.convertIntToString(code + 1,6);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> find(List<PropertyFilter> filters) {
    return this.userDao.find(filters);
  }

  @Override
  @Transactional(readOnly = true)
  public User load(String id) {
    return this.userDao.load(id);
  }

  public User findUserByPhone(String phone){
    return this.userDao.findUniqueBy("phone", phone);
  }

  public void saveStaffInfo(StaffInfo entity){
    this.staffInfoDao.saveOrUpdate(entity);
  }

  public StaffInfo findStaffByOpenId(String openId){
    return this.staffInfoDao.findUniqueBy("openId", openId);
  }
}
