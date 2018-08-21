package com.casesoft.dmc.service.exchange;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.exchange.DataSourceDao;
import com.casesoft.dmc.model.exchange.DataSrcInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017-06-06.
 */
@Service
@Transactional
public class DataSourceService extends AbstractBaseService<DataSrcInfo, String> {

    @Autowired
    private DataSourceDao dataSourceDao;

    @Override
    public Page<DataSrcInfo> findPage(Page<DataSrcInfo> page, List<PropertyFilter> filters) {
        return this.dataSourceDao.findPage(page, filters);
    }

    @Override
    public void save(DataSrcInfo entity) {
        this.dataSourceDao.saveOrUpdate(entity);
    }

    @Override
    public DataSrcInfo load(String id) {
        return null;
    }

    @Override
    public DataSrcInfo get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List getAll() {
        return this.dataSourceDao.getAll();
    }

    @Override
    public void update(DataSrcInfo entity) {

    }

    @Override
    public void delete(DataSrcInfo entity) {
        this.dataSourceDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.dataSourceDao.delete(id);
    }


    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List find(List list) {
        return this.dataSourceDao.find(list);
    }

    @Transactional(readOnly = true)
    public String findMaxId(String type) {
        String dataFlag = Constant.DataSource.ORACLE;
        switch (type) {
            case "S":
                dataFlag = Constant.DataSource.SQL_Server;
                break;
            case "O":
                dataFlag = Constant.DataSource.ORACLE;
                break;
            case "M":
                dataFlag = Constant.DataSource.MYSQL;
                break;
        }
        String hql = "select max(CAST(SUBSTRING(id," + (dataFlag.length() + 1) + "),integer))from DataSrcInfo";
        Integer id = this.dataSourceDao.findUnique(hql);
        return id == null ? (dataFlag + "0001") : dataFlag
                + CommonUtil.convertIntToString(id + 1, 4);
    }

    public DataSrcInfo findById(String id) {
        String hql = "from DataSrcInfo where id=?";
        return this.dataSourceDao.findUnique(hql, new Object[]{id});
    }
}
