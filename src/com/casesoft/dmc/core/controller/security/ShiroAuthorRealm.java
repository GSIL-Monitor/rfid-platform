package com.casesoft.dmc.core.controller.security;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.sys.UserUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.ProductClassView;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pc on 2016/3/22.
 */
public class ShiroAuthorRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private PropertyKeyService propertyKeyService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		System.out.println("principalCollection");

		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken)
			throws AuthenticationException {
		String usercode = (String) authenticationToken.getPrincipal(); // 得到用户名
		String password = new String(
				(char[]) authenticationToken.getCredentials()); // 得到密码
		User user = this.userService.getUserByCode(usercode);
		if (CommonUtil.isNotBlank(user)) {
			if (user.getPassword().equals(password)) {
				setSession(user);
				Subject currentUser = SecurityUtils.getSubject();
				Session session = currentUser.getSession();

				List<Resource> menuList = this.resourceService
						.getResourceByRole(user.getRoleId());
				// String authStr = UserUtil.countAuth(menuList);
				String authStr = UserUtil.countAuth2(menuList);

				session.setAttribute(Constant.Sys.User_Menu_Session, authStr);

				// 动态写入顶部菜单
				//createTopMenu(menuList, session);
				//
				session.removeAttribute("message");
				setProductClassSession();
				return new SimpleAuthenticationInfo(usercode, password,
						getName());
			} else {
				throw new IncorrectCredentialsException("密码错误！");
			}
		} else {
			throw new UnknownAccountException("用户名不存在！");
		}
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 *
	 */
	private void setSession(User user) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			System.out
					.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
			if (null != session) {
				/*Unit unit = CacheManager.getUnitById(user.getOwnerId());
				session.setAttribute("Session_Unit", unit);*/

			/*	String storageId = "";
				String ownerId = unit.getId();
				int storeType = Constant.StoreType.Storage;// 默认是仓库
				if (unit.getType() == Constant.UnitType.Warehouse
						|| unit.getType() == Constant.UnitType.Shop
						|| unit.getType() == Constant.UnitType.NetShop) {
					storageId = unit.getId();
					ownerId = unit.getOwnerId();
					storeType = Constant.StoreType.Shop;
					if (unit.getType() == Constant.UnitType.Warehouse) {
						unit = CacheManager.getUnitById(unit.getOwnerId());
						storeType = Constant.StoreType.Storage;
					}

				}*/
			/*	Integer iToken = UserUtil.getIToken(unit);
				Integer oToken = UserUtil.getOToken(unit);
				Integer cToken = UserUtil.getCToken(unit);
				session.setAttribute("iToken", iToken);
				session.setAttribute("oToken", oToken);
				session.setAttribute("cToken", cToken);
				session.setAttribute("storageId", storageId);*/
				session.setAttribute("ownerId", user.getOwnerId());/*
				session.setAttribute("storeType", storeType);*/
				user.setRoleName(CacheManager.getRoleNameById(user.getRoleId()));
				session.setAttribute(Constant.Session.User_Session, user);
				session.setAttribute("loginUserName", user.getName());
			}
		}
	}

	private void setProductClassSession() {
		List<PropertyType> list = this.propertyKeyService.findPrpertyByType();
		ProductClassView classView = new ProductClassView();
		classView.setProductClass(list);
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			session.setAttribute("classView", classView);
		}
	}

	 @Override
	  public String getName() {
	    return getClass().getName();
	  }
}
