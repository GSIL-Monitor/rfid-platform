package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.ShopTurnoverDao;
import com.casesoft.dmc.dao.shop.payDetailDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.shop.ShopTurnOver;
import com.casesoft.dmc.model.shop.payDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lly on 2018/8/19.
 */
@Service
@Transactional
public class payDetailService extends BaseService<payDetail, String> {
    @Autowired
    private payDetailDao payDetailDao;
    @Autowired
    private ShopTurnoverDao shopTurnoverDao;

    @Override
    public Page<payDetail> findPage(Page<payDetail> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(payDetail entity) {
        payDetailDao.saveOrUpdate(entity);
    }

    @Override
    public payDetail load(String id) {
        return null;
    }

    @Override
    public payDetail get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<payDetail> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<payDetail> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(payDetail entity) {

    }

    @Override
    public void delete(payDetail entity) {

    }

    @Override
    public void delete(String id) {

    }
    public Page<ShopTurnOver> getPriceCount(Page<ShopTurnOver> page){
        StringBuilder sql =new StringBuilder();
        sql.append("SELECT\n" +
                "  s.shop,\n" +
                "  s.payType,\n" +
                "\ts.PAYDATE,\n" +
                "  (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='0') as recivePrice,\n" +
                "  (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='1') as savePrice,\n" +
                "  (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='2') as returnPrice,\n" +
                "  SUM (ACTPAYPRICE) AS totPrice\n" +
                "FROM\n" +
                "  SHOP_PAYDETAIL s\n" +
                "\n" +
                "GROUP BY\n" +
                "  s.shop,\n" +
                "  s.payType,\n" +
                "\ts.PAYDATE");
        page = this.shopTurnoverDao.findPageBySQl(page, ShopTurnOver.class, sql.toString(), null);
        return page;
    }
}
