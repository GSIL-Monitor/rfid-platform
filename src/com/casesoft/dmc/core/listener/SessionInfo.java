package com.casesoft.dmc.core.listener;


/**
 * sessionInfo模型
 * 
 * @author 
 * 
 */
@SuppressWarnings("serial")
public class SessionInfo implements java.io.Serializable {

	private String userId;// 用户ID
	private String loginName;// 用户登录名称
	private String loginPassword;// 登录密码
	private String ip;// IP地址
	//private List<Auth> auths;// 用户拥有的权限

//	public List<Auth> getAuths() {
//		return auths;
//	}
//
//	public void setAuths(List<Auth> auths) {
//		this.auths = auths;
//	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return loginName;
	}

}
