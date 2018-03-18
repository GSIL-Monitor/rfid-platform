package com.casesoft.dmc.core.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Service层的抽象父类，定义基本的错误日志记录函数
 * 
 * @author Administrator
 * 
 * @param <T>
 * @param <PK>
 */
public abstract class AbstractBaseService<T, PK extends Serializable> implements
    IBaseService<T, PK> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected void throwExceptionAndLogger(String message, Throwable throwable)
      throws ServiceException {
    logger.error(message);
    throw new ServiceException(message, throwable);
  }

  public <X> List<X> findAll(PropertyFilter filter) {
    return null;
  }
}
