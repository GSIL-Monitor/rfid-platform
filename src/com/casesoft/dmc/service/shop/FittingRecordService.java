package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.controller.shop.TrendVo;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.FittingRecordDao;
import com.casesoft.dmc.model.shop.FittingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FittingRecordService extends AbstractBaseService<FittingRecord, String> {

    @Autowired
    private FittingRecordDao fittingRecordDao;

    @Override
    @Transactional(readOnly = true)
    public Page<FittingRecord> findPage(Page<FittingRecord> page, List<PropertyFilter> filters) {
        return this.fittingRecordDao.findPage(page, filters);
    }

    @Override
    public void save(FittingRecord entity) {
        // TODO Auto-generated method stub

    }

    public void saveAll(List<FittingRecord> entitys) {
        this.fittingRecordDao.doBatchInsert(entitys);
    }

    @Transactional(readOnly = true)
    public List<FittingRecord> find(String hql, final Object... values) {
        return this.fittingRecordDao.find(hql, values);
    }

    //按月统计
    public List<Object[]> findMonthTrend(String warehouseCode, String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select dat.dat,case when trendData.qty is null then 0 else trendData.qty end as qty  from  (select to_char(dt, 'yyyy-MM') as dat " +
                "   from (select date '").append(beginDate)
                .append("' + (rownum - 1) dt from dual " +
                        " connect by rownum <= (date '").append(endDate).append("' - date '")
                .append(beginDate).append("'+ 1)) " +
                " group by to_char(dt, 'yyyy-MM') order by dat) dat left join " +
                "（SELECT  TO_CHAR(sf.SCANTIME, 'yyyy-mm') AS trendDate, " +
                "    COUNT(*) qty" +
                "  FROM SHOP_FITTINGRECORD sf ");
        if (CommonUtil.isNotBlank(warehouseCode)) {
            sql.append(" where sf.ownerId='").append(warehouseCode).append("' ");
        }
        sql.append("  GROUP BY TO_CHAR(sf.SCANTIME, 'yyyy-mm')" +
                "     ）trendData " +
                "    on trendData.trendDate=dat.dat order by dat.dat asc");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }

    //按天统计
    public List<Object[]> findDayTrend(String warehouseCode, String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select dat.dat,case when trendData.qty is null then 0 else trendData.qty end as qty  from  (select to_char(dt, 'yyyy-MM-dd') as dat " +
                "   from (select date '").append(beginDate)
                .append("' + (rownum - 1) dt from dual " +
                        " connect by rownum <= (date '").append(endDate).append("' - date '")
                .append(beginDate).append("'+ 1)) " +
                " group by to_char(dt, 'yyyy-MM-dd') order by dat) dat left join " +
                "（SELECT  TO_CHAR(sf.SCANTIME, 'yyyy-MM-dd') AS trendDate, " +
                "    COUNT(*) qty" +
                "  FROM SHOP_FITTINGRECORD sf ");
        if (CommonUtil.isNotBlank(warehouseCode)) {
            sql.append(" where sf.ownerId='").append(warehouseCode).append("' ");
        }
        sql.append("  GROUP BY TO_CHAR(sf.SCANTIME, 'yyyy-MM-dd')" +
                "     ）trendData " +
                "    on trendData.trendDate=dat.dat order by dat.dat asc");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }

    //按小时统计
    public List<Object[]> findHourTrend(String warehouseCode, String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select dat.dat,case when trendData.qty is null then 0 else trendData.qty end as qty " +
                "   from (select to_char(to_date( '").append(beginDate)
                .append("','yyyy-mm-dd hh24' ) +  (rownum - 1) / 24,'yyyy-mm-dd hh24' ) dat from dual " +
                        " connect by rownum <= (date '").append(endDate).append("' - date '")
                .append(beginDate).append("'+ 1)*24   order by dat) dat left join " +
                "（SELECT  TO_CHAR(sf.SCANTIME, 'yyyy-MM-dd hh24') AS trendDate, " +
                "    COUNT(*) qty" +
                "  FROM SHOP_FITTINGRECORD sf ");
        if (CommonUtil.isNotBlank(warehouseCode)) {
            sql.append(" where sf.ownerId='").append(warehouseCode).append("' ");
        }
        sql.append("  GROUP BY TO_CHAR(sf.SCANTIME, 'yyyy-MM-dd hh24')" +
                "     ）trendData " +
                "    on trendData.trendDate=dat.dat order by dat.dat asc");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }

    //按门店统计
    public List<Object[]> findShopTrend(String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select* from (select trendData.name,case when trendData.qty is null then 0 else trendData.qty end as qty from" +
                "（select sys_unit.name ,trendData.qty from (SELECT  ownerId, " +
                "    COUNT(*) qty" +
                "  FROM SHOP_FITTINGRECORD sf where to_char(sf.SCANTIME,'yyyy-MM-dd')>='")
                .append(beginDate).append("' and ")
                .append("to_char(sf.SCANTIME,'yyyy-MM-dd')<='").append(endDate).append("'");
        sql.append(" GROUP BY ownerId" +
                "     ）trendData " +
                " right join (select * from sys_unit where type=4) sys_unit on sys_unit.id=trendData.OWNERID ) trendData)trendData order by   trendData.qty desc");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }

    //按sku统计
    public List<Object[]> findSkuTrend(String warehouseCode, String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from (select trendData.sku,case when trendData.qty is null then 0 else trendData.qty end as qty from" +
                " (SELECT  sku, " +
                "     case when COUNT(*) is null then 0 else COUNT(*) end as qty" +
                "  FROM SHOP_FITTINGRECORD sf where to_char(sf.SCANTIME,'yyyy-MM-dd')>='")
                .append(beginDate).append("' and ")
                .append("to_char(sf.SCANTIME,'yyyy-MM-dd')<='").append(endDate).append("'").append(" ");
        if (CommonUtil.isNotBlank(warehouseCode)) {
            sql.append(" and sf.ownerId='").append(warehouseCode).append("' ");
        }
        sql.append(" GROUP BY sf.sku" +
                "     ）trendData   order by trendData.qty desc) where rownum<=10  ");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }
    //按sku统计
    public List<Object[]> findYoYSkuTrend(String warehouseCode, String beginDate, String endDate,String codes) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select product.code,case when trendData.qty is null then 0 else trendData.qty end as qty from (" +
                "select * from (select trendData.sku,case when trendData.qty is null then 0 else trendData.qty end as qty from" +
                " (SELECT  sku, " +
                "     case when COUNT(*) is null then 0 else COUNT(*) end as qty" +
                "  FROM SHOP_FITTINGRECORD sf where to_char(sf.SCANTIME,'yyyy-MM-dd')>='")
                .append(beginDate).append("' and ")
                .append("to_char(sf.SCANTIME,'yyyy-MM-dd')<='").append(endDate).append("'")
                .append("and sku in ( ").append(codes).append(") ");
        if (CommonUtil.isNotBlank(warehouseCode)) {
            sql.append(" and sf.ownerId='").append(warehouseCode).append("' ");
        }
        sql.append(" GROUP BY sf.sku" +
                "     ）trendData   order by trendData.qty desc)) trendData " +
                "right join (select code from product_product where code in( ").append(codes)
                .append(")) product on product.code=trendData.sku ");
        return this.fittingRecordDao.findBySQL(sql.toString(), new Object[]{});
    }
    @Override
    public FittingRecord load(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FittingRecord get(String propertyName, Object value) {
        return this.fittingRecordDao.findUniqueBy(propertyName, value);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FittingRecord> find(List<PropertyFilter> filters) {
        return this.fittingRecordDao.find(filters);
    }

    @Transactional(readOnly = true)
    public List<FittingRecord> findOrderedRecord(Date beginDate, Date endDate) {
        String hql = "select fr from FittingRecord fr where fr.scanTime between ? and ? order by fr.scanTime";
        return this.find(hql, beginDate, endDate);
    }

    public List<FittingRecord> findCountRecord(Date beginDate, Date endDate) {
        String hql = "select new com.casesoft.dmc.model.shop.FittingRecord(r.scanTime,r.ownerId,r.styleId,r.colorId,"
                + "r.sizeId,count(*)) from FittingRecord r where r.scanTime between ? and ? "
                + " group by r.scanTime,r.ownerId,r.styleId,r.colorId,"
                + "	r.sizeId";
        return this.fittingRecordDao.find(hql, new Object[]{beginDate, endDate});
    }

    @Override
    public List<FittingRecord> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(FittingRecord entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(FittingRecord entity) {
        this.fittingRecordDao.delete(entity);

    }

    public void delete(List<FittingRecord> entityList) {
        for (FittingRecord r : entityList) {
            delete(r);
        }
    }

    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate() {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime),sum(s.count))  from Score s group by ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime) ";
        return this.fittingRecordDao.find(hql, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate(String ownerIds) {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime),sum(s.count))  from Score s where ownerId in ("
                + ownerIds + ") group by ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime) ";
        return this.fittingRecordDao.find(hql, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate(String ownerId, String parentId) {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime),sum(s.count))  from Score s" +
                " where parentId='" + parentId + "'";
        if (!CommonUtil.isBlank(ownerId))
            hql += " and ownerId='" + ownerId + "'";
        hql += " group by ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime) ";
        return this.fittingRecordDao.find(hql, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate2(String ownerId, String parentId, Date beginTime, Date endTime) {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime),sum(s.count))  from Score s" +
                " where parentId='" + parentId + "'";
        String endHql = " group by ownerId,year(s.scoreTime),month(s.scoreTime),day(s.scoreTime)";
        if (!CommonUtil.isBlank(ownerId)) {
            hql += " and ownerId in(" + ownerId + ")";
        }
        if (beginTime == null || endTime == null) {
            hql += endHql;
            return this.fittingRecordDao.find(hql, new Object[]{});
        } else {
            hql += " and s.scoreTime between ? and ? " + endHql;
            return this.fittingRecordDao.find(hql, new Object[]{beginTime, endTime});
        }
        // return this.fittingRecordDao.find(hql, new Object[] {});
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    public FittingRecordDao getFittingRecordDao() {
        return fittingRecordDao;
    }

    public void setFittingRecordDao(FittingRecordDao fittingRecordDao) {
        this.fittingRecordDao = fittingRecordDao;
    }

}
