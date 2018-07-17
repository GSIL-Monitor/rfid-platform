package com.casesoft.dmc.service.pad;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.pad.WeiXinUserDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.pad.WeiXinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信公众号实体service
 * Created by ltc on 2018/6/7.
 */
@Service
@Transactional
public class WeiXinUserService extends BaseService<WeiXinUser, String> {
    @Autowired
    private WeiXinUserDao weiXinUserDao;

    public WeiXinUser getByPhone(String phone){
        String hql ="from WeiXinUser where phone =?";
        return this.weiXinUserDao.findUnique(hql,new Object[]{phone});
    }

    @Override
    public Page<WeiXinUser> findPage(Page<WeiXinUser> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(WeiXinUser entity) {
        this.weiXinUserDao.saveOrUpdate(entity);
    }

    @Override
    public WeiXinUser load(String id) {
        return null;
    }

    @Override
    public WeiXinUser get(String propertyName, Object value) {
        return this.weiXinUserDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<WeiXinUser> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<WeiXinUser> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(WeiXinUser entity) {

    }

    @Override
    public void delete(WeiXinUser entity) {

    }

    @Override
    public void delete(String id) {

    }
    public void updatePhoneByopenId(String openId,String phone){
        this.weiXinUserDao.batchExecute("update WeiXinUser set phone='" +phone+"' where openId = ?",openId);
    }
}
