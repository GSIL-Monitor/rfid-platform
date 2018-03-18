package com.casesoft.dmc.extend.third.request;

import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.extend.third.descriptor.DataResult;

import java.io.Serializable;

/**
 * Created by john on 2017-02-22.
 */
public abstract class BaseService<T, PK extends Serializable> extends AbstractBaseService<T, PK> {
    public DataResult find(RequestPageData<?> request){
        return this.find(request,null);
    }
    public DataResult find(RequestPageData<?> request,Class<?> entityClass){
        return null;
    }
}
