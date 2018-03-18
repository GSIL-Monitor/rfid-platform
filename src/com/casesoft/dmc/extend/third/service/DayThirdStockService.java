package com.casesoft.dmc.extend.third.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.dao.DayThirdStockDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by john on 2017-02-27.
 */
@Service
@Transactional
public class DayThirdStockService extends BaseService<DayThirdStock, String> {
    @Autowired
    private DayThirdStockDao dayThirdStockDao;

    public DataResult find(RequestPageData<?> request) {
        return this.dayThirdStockDao.find(request);
    }

    @Override
    public Page<DayThirdStock> findPage(Page<DayThirdStock> page, List<PropertyFilter> filters) {
        return this.dayThirdStockDao.findPage(page, filters);
    }

    @Override
    public void save(DayThirdStock entity) {

    }

    @Override
    public DayThirdStock load(String id) {
        return null;
    }

    @Override
    public DayThirdStock get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DayThirdStock> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DayThirdStock> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DayThirdStock entity) {

    }

    @Override
    public void delete(DayThirdStock entity) {

    }

    public void doBatchInsert(List<DayThirdStock> entities,String days) {
        if (CommonUtil.isNotBlank(entities)) {
            this.dayThirdStockDao.batchExecute("delete DayThirdStock dayThirdStock",new Object[]{});
            this.dayThirdStockDao.doBatchInsert(entities);
/*
            this.dayThirdStockDao.batchExecute("delete DayThirdStock dayThirdStock where dayThirdStock.day not in("+days+");");
*/
        }
    }
    public void freshMaterializedView(){
        this.dayThirdStockDao.creatSQLQuery(" begin" +
                "        DBMS_MVIEW.REFRESH(list=>'THIRD_PL_FITTINGANALYSIS_VIEW', method=>'COMPLETE',atomic_refresh=>true);" +
                " DBMS_MVIEW.REFRESH(list=>'THIRD_SaleStockThird_VIEW', method=>'COMPLETE',atomic_refresh=>true);" +
                "        end;").executeUpdate();


    }

    @Override
    public void delete(String id) {

    }
}
