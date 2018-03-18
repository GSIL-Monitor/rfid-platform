package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryTokenDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.factory.FactoryCategory;
import com.casesoft.dmc.model.factory.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-04.
 */
@Service
@Transactional
public class FactoryTokenService extends BaseService<Token,String> {

    @Autowired
    private FactoryTokenDao factoryTokenDao;

    public List<Token> findAllToken() {
        return this.factoryTokenDao.find("from Token where locked=0 order by sortIndex asc");
    }

    @Override
    public Page<Token> findPage(Page<Token> page, List<PropertyFilter> filters) {
        return this.factoryTokenDao.findPage(page,filters);
    }

    public Token findToken(Integer token){
        String hql="from Token t where t.token=?";
        return this.factoryTokenDao.findUnique(hql,new Object[]{token});
    }

    public void startUsing(Integer token){
        String hql="update Token t set t.locked=0 where t.token=?";
        this.factoryTokenDao.batchExecute(hql,new Object[]{token});
    }

    public void stopUsing(Integer token){
        String hql="update Token t set t.locked=1 where t.token=?";
        this.factoryTokenDao.batchExecute(hql,new Object[]{token});
    }

    public void save2Token(Token firstToken,Token secondToken){
        this.factoryTokenDao.saveOrUpdate(firstToken);
        this.factoryTokenDao.saveOrUpdate(secondToken);
    }

    public Integer findMaxToken(){
        String hql="select max(token) from Token";
        Object obj=this.factoryTokenDao.findUnique(hql);
        if (CommonUtil.isBlank(obj)){
            return  0;
        }else {
            return (Integer)obj;
        }
    }

    public Integer findMaxSortIndex(){
        String hql="select max(sortIndex) from Token ";
        Object obj=this.factoryTokenDao.findUnique(hql);
        if (CommonUtil.isBlank(obj)){
            return  0;
        }else {
            return (Integer)obj;
        }
    }
    public List<FactoryCategory> findAllCategory() {
        return this.factoryTokenDao.find("from FactoryCategory");
    }
    @Override
    public void save(Token token) {
         this.factoryTokenDao.saveOrUpdate(token);
    }

    @Override
    public Token load(String id) {
        return null;
    }

    @Override
    public Token get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Token> find(List<PropertyFilter> filters) {
        return this.factoryTokenDao.find(filters);
    }

    @Override
    public List<Token> getAll() {
        return this.factoryTokenDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Token entity) {

    }

    @Override
    public void delete(Token entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Token findByName(String name) {
        String hql="from Token where name=?";
        return this.factoryTokenDao.findUnique(hql,new Object[]{name});
    }
}
