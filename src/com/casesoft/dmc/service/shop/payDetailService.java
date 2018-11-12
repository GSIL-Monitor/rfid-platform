package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.ShopTurnoverDao;
import com.casesoft.dmc.dao.shop.payDetailDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.shop.ShopTurnOver;
import com.casesoft.dmc.model.shop.PayDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lly on 2018/8/19.
 */
@Service
@Transactional
public class payDetailService extends BaseService<PayDetail, String> {
    @Autowired
    private payDetailDao payDetailDao;
    @Autowired
    private ShopTurnoverDao shopTurnoverDao;

    @Override
    public Page<PayDetail> findPage(Page<PayDetail> page, List<PropertyFilter> filters) {
        return payDetailDao.findPage(page,filters);
    }

    @Override
    public void save(PayDetail entity) {
        payDetailDao.saveOrUpdate(entity);
    }

    @Override
    public PayDetail load(String id) {
        return null;
    }

    @Override
    public PayDetail get(String propertyName, Object value) {
        return payDetailDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<PayDetail> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PayDetail> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PayDetail entity) {

    }

    @Override
    public void delete(PayDetail entity) {

    }

    @Override
    public void delete(String id) {

    }
    public Page<ShopTurnOver> getPriceCount(Page<ShopTurnOver> page,String GED_billDate,String LED_billDate,String shopId,String payType){
        StringBuilder sql =new StringBuilder();
        sql.append("SELECT\n" +
                "  s.shop,\n" +
                "  s.payType,\n" +
                "  s.PAYDATE,\n" +
                "  u.name as shopName,\n" +
                "  (case when (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='0' and t.PAYDATE = s.PAYDATE)\n" +
                "   is null then 0\n" +
                "   else (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='0' and t.PAYDATE = s.PAYDATE) end) as recivePrice,\n" +
                "  (case when (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='1' and t.PAYDATE = s.PAYDATE)\n" +
                "   is null then 0\n" +
                "   else (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='1' and t.PAYDATE = s.PAYDATE) end) as savePrice,\n" +
                "  (case when (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='2' and t.PAYDATE = s.PAYDATE)\n" +
                "   is null then 0\n" +
                "   else (SELECT SUM (PAYPRICE) from  SHOP_PAYDETAIL  t where t.shop=s.shop and t.payType=s.payType and t.billtype='2' and t.PAYDATE = s.PAYDATE) end) as returnPrice,\n" +
                "  SUM (ACTPAYPRICE) AS totPrice\n" +
                "FROM\n" +
                "  SHOP_PAYDETAIL s\n");
        sql.append("LEFT JOIN SYS_UNIT u on s.SHOP = u.id\n" +
                "where s.STATUS = '1'\n");
        if(CommonUtil.isNotBlank(GED_billDate)){
            sql.append("and s.PAYDATE >= to_char(to_date('"+GED_billDate+"','yyyy-mm-dd'),'yyyy-mm-dd')\n");
        }
        if (CommonUtil.isNotBlank(LED_billDate)){
            sql.append("and s.PAYDATE <= to_char(to_date('"+LED_billDate+"','yyyy-mm-dd'),'yyyy-mm-dd')\n");
        }
        if (CommonUtil.isNotBlank(shopId)){
            sql.append("and s.shop='"+shopId+"'\n");
        }
        if (CommonUtil.isNotBlank(payType)){
            sql.append("and s.payType='"+payType+"'\n");
        }
        sql.append("GROUP BY\n" +
                "  s.shop,\n" +
                "  s.payType,\n" +
                "  u.name,\n" +
                "  s.PAYDATE");
        page = this.shopTurnoverDao.findPageBySQl(page, ShopTurnOver.class, sql.toString(), null);
        return page;
    }
}
