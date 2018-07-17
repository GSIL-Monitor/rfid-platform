package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.BusinessAccountDao;
import com.casesoft.dmc.model.sys.BusinessAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Administrator on 2018/7/11.
 */
@Service
@Transactional
public class BusinessAccountService implements IBaseService<BusinessAccount,String> {
    @Autowired
    private BusinessAccountDao businessAccountDao;
    @Override
    public Page<BusinessAccount> findPage(Page<BusinessAccount> page, List<PropertyFilter> filters) {
        String sql="select billdate,destunitname,destunitid,sum(saleprice) as saleprice,sum(salereturnprice) as salereturnprice,sum(saleqty) as saleqty,sum(salereturnqty) as salereturnqty,(select sum(payprice) from LOGISTICS_PAYMENTGATHERINGBILL l where l.customsid=m.destunitid and l.billtype=1 and TO_CHAR(l.billdate,'yyyy-MM-dd')=TO_CHAR(m.billdate,'yyyy-MM-dd')) as payprice,(select su.owingValue from sys_unit su where su.id=m.destunitid) as owingValue from (select t.billdate,t.actprice as saleprice,0 as salereturnprice,t.totqty as saleqty,0 as salereturnqty,t.destunitname, t.destunitid from logistics_saleorderbill t,sys_unit s where t.destunitid=s.id and t.status<>-1 and " +
                "s.groupid='JMS' " +
                "union all " +
                "select b.billdate,0 as saleprice,b.actprice as salereturnprice,0 as saleqty,b.totqty as salereturnqty,b.origunitname as destunitname,b.origunitid as destunitid from logistics_saleorderreturnbill b,sys_unit u where b.origunitid=u.id and b.status<>-1 and " +
                "u.groupid='JMS') m "+CommonUtil.sqlQueryCondition(filters)+"group by destunitid,destunitname,billdate order by billdate desc";

        page = this.businessAccountDao.findPageBySQl(page, BusinessAccount.class, sql, null);
        return page;
    }

    @Override
    public void save(BusinessAccount entity) {

    }

    @Override
    public BusinessAccount load(String id) {
        return null;
    }

    @Override
    public BusinessAccount get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<BusinessAccount> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<BusinessAccount> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(BusinessAccount entity) {

    }

    @Override
    public void delete(BusinessAccount entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<BusinessAccount> findBySql(List<PropertyFilter> filters){
        String sql="select billdate,destunitname,destunitid,sum(saleprice) as saleprice,sum(salereturnprice) as salereturnprice,sum(saleqty) as saleqty,sum(salereturnqty) as salereturnqty,(select sum(payprice) from LOGISTICS_PAYMENTGATHERINGBILL l where l.customsid=m.destunitid and l.billtype=1 and TO_CHAR(l.billdate,'yyyy-MM-dd')=TO_CHAR(m.billdate,'yyyy-MM-dd')) as payprice,(select su.owingValue from sys_unit su where su.id=m.destunitid) as owingValue from (select t.billdate,t.actprice as saleprice,0 as salereturnprice,t.totqty as saleqty,0 as salereturnqty,t.destunitname, t.destunitid from logistics_saleorderbill t,sys_unit s where t.destunitid=s.id and t.status<>-1 and " +
                "s.groupid='JMS' " +
                "union all " +
                "select b.billdate,0 as saleprice,b.actprice as salereturnprice,0 as saleqty,b.totqty as salereturnqty,b.origunitname as destunitname,b.origunitid as destunitid from logistics_saleorderreturnbill b,sys_unit u where b.origunitid=u.id and b.status<>-1 and " +
                "u.groupid='JMS') m "+CommonUtil.sqlQueryCondition(filters)+"group by destunitid,destunitname,billdate order by billdate desc";

        List<BusinessAccount> businessAccountList = this.businessAccountDao.findBySQl(BusinessAccount.class, sql, null);
        return businessAccountList;
    }

}
