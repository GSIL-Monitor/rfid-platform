package com.casesoft.dmc.extend.third.searcher.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.searcher.dao.SearchMainDao;
import com.casesoft.dmc.extend.third.searcher.model.SearchDtl;
import com.casesoft.dmc.extend.third.searcher.model.SearchMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017-04-18.
 */
@Service
@Transactional
public class SearchMainService extends BaseService<SearchMain, String> {
    @Autowired
    private SearchMainDao searchMainDao;

    @Override
    public DataResult find(RequestPageData<?> request) {
        return searchMainDao.find(request);
    }
    @Override
    public DataResult find(RequestPageData<?> request,Class<?> entityClass) {
        return searchMainDao.find(request,entityClass);
    }
    @Override
    public Page<SearchMain> findPage(Page<SearchMain> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(SearchMain entity) {
        if(CommonUtil.isNotBlank(entity)){
            this.searchMainDao.save(entity);
            if(CommonUtil.isNotBlank(entity.getSearchDtlList())){
                this.searchMainDao.doBatchInsert(entity.getSearchDtlList());
            }
        }
    }
    /**
     * @param entity
     * @param skus
     * 更新主单状态，更新
     * */
    public void updateStatus(SearchMain entity,String skus){
        StringBuffer hql=new StringBuffer("update SearchDtl s set s.searchSuccess=? where s.sku in (");
        hql.append(skus).append(") and s.mainId=?");
        this.searchMainDao.batchExecute(hql.toString(),new Object[]{true,entity.getId()});
        List<SearchDtl> searchDtls=this.searchMainDao.find("from SearchDtl dtl where dtl.mainId=? and dtl.searchSuccess=?",new Object[]{entity.getId(),true});

        entity.setSearchQty(searchDtls.size());
        entity.setLostQty(entity.getSkuQty() - searchDtls.size());
        if(entity.getSkuQty()==entity.getSearchQty()){
            entity.setStatus(SearchMain.Status.MAIN_STATUS_END);
        }else{
            entity.setStatus(SearchMain.Status.MAIN_STATUS_PARTON);
        }
        this.searchMainDao.update(entity);
    }
    @Override
    public SearchMain load(String id) {
        return this.searchMainDao.get(id);
    }
     public SearchMain findBillCount(String userCode,String type) {
        if(type.equals("1")){//获取未处理新单据数量
            String hql = "select count(*) from SearchMain f where f.toCode = ? and f.";

        }
        return this.searchMainDao.get("d");
    }
    @Override
    public SearchMain get(String propertyName, Object value) {
        return this.searchMainDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<SearchMain> find(List<PropertyFilter> filters) {
        return this.searchMainDao.find(filters);
    }

    @Override
    public List<SearchMain> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(SearchMain entity) {
        this.searchMainDao.update(entity);
        this.searchMainDao.batchExecute("update SearchDtl dtl set dtl.status=? where dtl.mainId=?",new Object[]{entity.getStatus(),entity.getId()});
    }

    @Override
    public void delete(SearchMain entity) {

    }

    @Override
    public void delete(String id) {

    }

    public String findMainId() {
        String date=CommonUtil.getDateString(new Date(),"yyyyMMdd");
        String hql = "select max(CAST(SUBSTRING(f.id," + (date.length()+1) + "),integer)) from SearchMain f where f.id like ?";
        Integer id = this.searchMainDao.findUnique(hql,new Object[] { date + '%' });
        return id == null ? (date + "0001") : date + CommonUtil.convertIntToString(id + 1, 4);
    }
}
