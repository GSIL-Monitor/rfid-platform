package com.casesoft.dmc.core.dao;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.DriverManagerConnectionProvider;

import java.util.Properties;

public class CustomDriverManagerConnectionProvider extends
		DriverManagerConnectionProvider {

    public CustomDriverManagerConnectionProvider() {    
        super();    
    }    
        
    @Override   
    public void configure(Properties props) throws HibernateException{    
        String user = props.getProperty(Environment.USER);    
        String password = props.getProperty(Environment.PASS);
        System.out.println(SecretUtil.decrypt(user)+"===="+SecretUtil.decrypt(password));
        props.setProperty(Environment.USER, SecretUtil.decrypt(user));    
        props.setProperty(Environment.PASS, SecretUtil.decrypt(password));    
        super.configure(props);    
    } 
}
