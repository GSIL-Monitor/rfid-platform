package com.casesoft.dmc.core.controller.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MonitorRealm extends AuthorizingRealm {

  /**
   * 授权实现
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
    return null;
  }

  /**
   * 认证实现
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0)
      throws AuthenticationException {
    return null;
  }

}
