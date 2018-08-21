package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserUtil {

	protected static Logger logger = LoggerFactory.getLogger(UserUtil.class);

	public static SinglePage<Role> countRoles(List<Role> roleList) {
		SinglePage<Role> sp = new SinglePage<Role>(roleList);
		sp.setTotal(roleList.size());
		return sp;
	}

	public static void convertToVo(List<User> userList) {
		// List<User> userList = beansPage.getRows();
		for (User user : userList) {
			user.setRoleName(CacheManager.getRoleNameById(user.getRoleId()));
			Unit u = CacheManager.getUnitById(user.getOwnerId());
			if (null != u)
				user.setUnitName(u.getName());
		}
	}


	/**
	 * 权限字符串实例:Section1~weather1,weather.news1,news|Section2~ie1,ie.video1,video
	 * |Select3~task1,task.
	 * search~foutbound1,foutbound.ainventory1,ainventory.sinventory1
	 * ,sinventory| chart~map1,map.fitting1,fitting.supply1,supply|
	 * stock~inventory1,inventory.check1,check|
	 * tag~birth1,birth.detect1,detect.receive1
	 * ,receive.send1,send.feedback1,feedback|
	 * task~inbound1,inbound.outbound1,outbound| rfid~box1,box.epc1,epc|
	 * 
	 * @param menuList
	 * @return
	 */
	public static String countAuth(List<Resource> menuList) {
		Map<String, String> resMap = new HashMap<String, String>();
		for (Resource res : menuList) {
			if (res.getSeqNo() == 0) {// 父节点
				// if(! resMap.containsKey(res.getCode())) {//不包含主键
				// resMap.put(res.getCode(), "");
				// }
				// 包含主键
			} else {
				String ownerId = res.getOwnerId();
				if (!resMap.containsKey(ownerId)) {// map key不包含父节点
					String value = res.getEname() + "1," + res.getEname();
					resMap.put(ownerId, value);
				} else {
					String value = resMap.get(ownerId);
					value = value + "." + res.getEname() + "1,"
							+ res.getEname();
					resMap.put(ownerId, value);// 相同的key重新put可使值改变
				}
			}

		}
		String authStr = "";
		for (java.util.Map.Entry<String, String> entry : resMap.entrySet()) {
			String selection = CacheManager.getResourceById(entry.getKey())
					.getEname();
			authStr += selection + "~" + entry.getValue() + "|";
		}
		return authStr;
	}

	/**
	 * 转化成只有一个section的权限str Section1~weather1,weather.news1,news
	 * 
	 * @param menuList
	 * @return
	 */
	public static String countAuth2(List<Resource> menuList) {
		String authStr = "";
		for (Resource res : menuList) {
			if (res.getSeqNo() == 0) {
			} else {
				authStr = authStr + res.getEname() + "1," + res.getEname()
						+ ".";
			}
		}
		return "selection1~" + authStr;
	}
	/**
	 * 根据第一级别菜单模块分块
	 * @param menuList
	 * @return
	 */
	public static String countAuth3(List<Resource> menuList) {
		Map<String,String> authMap = new HashMap<String,String>();
		for(Resource res : menuList) {
			if (res.getSeqNo() == 0) {
				if(! authMap.containsKey(res.getCode())) {
					authMap.put(res.getCode(), "");
				}
			} else {
				if(authMap.containsKey(res.getOwnerId())) {//Map中包含父节点
					String value = authMap.get(res.getOwnerId());
					value = value + res.getEname() + "1," + res.getEname()
							+ ".";
					authMap.put(res.getOwnerId(), value);
				} else {
					authMap.put(res.getOwnerId(), res.getEname() + "1," + res.getEname()
							+ ".");
				}
			}
		}
		String authStr = "";
		//连接成字符串
		for(String parentId: authMap.keySet()) {
			authStr = authStr +"selection"+parentId+"~"+authMap.get(parentId)+"|";
		}
		return authStr;
	}

	public static void copyProperty(User sessionUser, User user) {
		sessionUser.setName(user.getName());
		sessionUser.setPassword(user.getPassword());
		sessionUser.setEmail(user.getEmail());
		sessionUser.setPhone(user.getPhone());
	}

	// 获取入库token
	public static Integer getIToken(Unit unit) {
		switch (unit.getType()) {
		case 1:
			return 8;
		case 2:
			return 11;
			// case 3:return 14;
		case 4:
			return 14;
		case 5:
			return 21;
		}
		return null;
	}

	// 获取出库token
	public static Integer getOToken(Unit unit) {
		switch (unit.getType()) {
		case 1:
			return 10;
		case 2:
			return 13;
		case 3:
			return 7;
		case 4:
			return 15;
		case 5:
			return 22;
		}
		return null;
	}

	// 获取盘点token
	public static Integer getCToken(Unit unit) {
		switch (unit.getType()) {
		case 1:
			return 9;
		case 2:
			return 12;
			// case 3:return 7;
		case 4:
			return 16;
			// case 5: return 22;
		}
		return null;
	}

	public static void convertToTreeVo(List<Unit> rows) {
		// String id = "1";//品牌商初始化时Id为1
		for (Unit u : rows) {
			/*// if(u.getType() != 1) {
			// u.set_parentId(id);
			// u.setState("closed");
			// }
			if (!CommonUtil.isBlank(CacheManager.getProvinceById(u.getProvinceId()))) {
				u.setProvince(CacheManager.getProvinceById(u.getProvinceId()));
			}
			if (!CommonUtil.isBlank(CacheManager.getCityById(u.getCityId()))) {
				u.setCity(CacheManager.getCityById(u.getCityId()));
			}
			if(!CommonUtil.isBlank(CacheManager.getUnitById(u.getOwnerId()))){
				u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());// wing
			}
			if(!CommonUtil.isBlank(u.getGroupId())&!CommonUtil.isBlank(CacheManager.getPropertyKey(u.getGroupId()))){
				u.setGroupName(CacheManager.getPropertyKey(u.getGroupId()).getName());
            }	*/																// 20140607
																				// 父组织名称
		}

	}

	/**
	 * 3:工厂4:代理商5:门店
	 * 
	 * @param unit
	 * @return
	 */
	public static User getNewUser(Unit unit) {
		User user = new User();
		user.setId(new GuidCreator().toString());
		user.setCode(unit.getCode());// 与组织编码相同
		user.setPassword(Constant.Password.User_Init);
		user.setRoleId(unit.getType().toString());// 组织类型对应Role ID
		user.setOwnerId(unit.getId());
		user.setName(unit.getName());
		user.setCreateDate(new Date());
		user.setCreatorId("0");// 管理员ID
		user.setIsAdmin(0);
		user.setLocked(0);

		user.setPhone(unit.getTel());

		return user;
	}

	public static void convertToUnitVo(List<Unit> unitList) {
		for (Unit u : unitList) {
			convertToUnitVo(u);
		}

	}

	public static void convertToUnitVo(Unit u) {
		if (!CommonUtil.isBlank(u.getProvinceId())
				&& !CommonUtil.isBlank(u.getCityId())) {
//			if (CommonUtil.isBlank(u.getProvince())) {
//				u.setProvince(CacheManager.getProvinceById(u.getProvinceId()));
//			}
//			if (CommonUtil.isBlank(u.getCity())) {
//				u.setCity(CacheManager.getCityById(u.getCityId()));
//			}

		}
	}

}
