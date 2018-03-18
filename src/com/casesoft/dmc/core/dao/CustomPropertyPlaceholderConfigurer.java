package com.casesoft.dmc.core.dao;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer

{

  @Override
  protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
      throws BeansException {

    String isAuth = props.getProperty("jdbc.auth");
    if(!isAuth.equals("0")) {
      String userName = props.getProperty("jdbc.username");
      if (userName != null) {
        props.setProperty("jdbc.username", SecretUtil.decrypt(userName));
      }
    }
    String password = props.getProperty("jdbc.password");
    if (password != null) {
      props.setProperty("jdbc.password", SecretUtil.decrypt(password));
    }
    super.processProperties(beanFactory, props);

  }
}
