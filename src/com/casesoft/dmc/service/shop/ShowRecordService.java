package com.casesoft.dmc.service.shop;

import java.util.Date;
import java.util.List;

import com.casesoft.dmc.core.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.controller.shop.ShowSkuVo;
import com.casesoft.dmc.controller.shop.TrendVo;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.ShowRecordDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.ShowRecord;

@Service
@Transactional
public class ShowRecordService extends AbstractBaseService<ShowRecord, String> {

  @Autowired
  private ShowRecordDao showRecordDao;

  @Override
  @Transactional(readOnly = true)
  public Page<ShowRecord> findPage(Page<ShowRecord> page, List<PropertyFilter> filters) {
    return this.showRecordDao.findPage(page, filters);
  }

  @Override
  public void save(ShowRecord entity) {
    // TODO Auto-generated method stub

  }
  @Transactional(readOnly = true)
  public List<ShowRecord> find(String hql, final Object... values) {
    return this.showRecordDao.find(hql, values);
  }

  @Override
  public ShowRecord load(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ShowRecord get(String propertyName, Object value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ShowRecord> find(List<PropertyFilter> filters) {
    return this.showRecordDao.find(filters);
  }
  @Transactional(readOnly = true)
  public List<ShowRecord> findOrderedRecord(Date beginDate, Date endDate) {
    String hql = "select fr from ShowRecord fr where fr.scanTime between ? and ? order by fr.scanTime";
     return this.find(hql,new Object[]{beginDate,endDate});
  }

  @Override
  public List<ShowRecord> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <X> List<X> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(ShowRecord entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(ShowRecord entity) {
    this.showRecordDao.delete(entity);

  }

  public void delete(List<ShowRecord> entityList) {
    for (ShowRecord r : entityList) {
      delete(r);
    }
  }

  @Transactional(readOnly = true)
  public List<TrendVo> findTrendDate() {
    String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime),sum(s.count))  from Score s group by ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime) ";
    return this.showRecordDao.find(hql, new Object[] {});
  }

  @Transactional(readOnly = true)
  public List<TrendVo> findTrendDate(String ownerIds) {
    String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime),sum(s.count))  from Score s where ownerId in ("
        + ownerIds + ") group by ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime) ";
    return this.showRecordDao.find(hql, new Object[] {});
  }

    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate(String ownerId, String parentId) {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime),sum(s.count))  from Score s" +
                " where parentId='"+parentId+"'";
        if(!CommonUtil.isBlank(ownerId))
            hql += " and ownerId='"+ownerId+"'";
        hql += " group by ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime) ";
        return this.showRecordDao.find(hql, new Object[] {});
    }
    @Transactional(readOnly = true)
    public List<TrendVo> findTrendDate2(String ownerId, String parentId,Date beginTime,Date endTime) {
        String hql = "select new com.casesoft.dmc.controller.shop.TrendVo(s.ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime),sum(s.count))  from Score s" +
                " where parentId='"+parentId+"'";
        String endHql=" group by ownerId,year(s.scanTime),month(s.scanTime),day(s.scanTime)";
        if(!CommonUtil.isBlank(ownerId)){
            hql += " and ownerId in("+ownerId+")";
          }
        if (beginTime == null || endTime == null) {
            hql += endHql;
            return this.showRecordDao.find(hql,new Object[] {});
        }else{
            hql += " and s.scanTime between ? and ? "+endHql;
            return this.showRecordDao.find(hql, new Object[] {beginTime,endTime});
        }
       // return this.ShowRecordDao.find(hql, new Object[] {});
    }
  @Override
  public void delete(String id) {
    // TODO Auto-generated method stub

  }

  public ShowRecordDao getShowRecordDao() {
    return showRecordDao;
  }

  public void setShowRecordDao(ShowRecordDao showRecordDao) {
    this.showRecordDao = showRecordDao;
  }

 

  public List<ShowSkuVo> findSku(String ownerIds, Date startTime,
		Date endTime) {
    String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(s.styleId,count(*) as qty)"
              + " from ShowRecord s ";   
    String bthql ="";
    if(CommonUtil.isNotBlank(ownerIds)) {
        hql = hql + "where s.ownerId in ("+ownerIds +")";
        bthql = " and s.scanTime between ? and ? ";
    } 
    else{
    	bthql = " where s.scanTime between ? and ? ";
    }
   
    String endhql = " group by s.styleId " + " order by qty";

    if (startTime == null || endTime == null) {
        hql += endhql;
        hql = hql + " desc";
        return this.showRecordDao.find(hql, new Object[]{});
    } else {
        hql += bthql + endhql;
        hql = hql + " desc";

        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
    }
  }
  public List<ShowSkuVo> findAreaTop10Sku(Date startTime, Date endTime ,String styleIds) {
	  String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(s.styleId,count(*) as qty,substring(p.name,0,2))"
              + " from ShowRecord as s,Unit as u,PropertyKey p where s.styleId in ("+styleIds +") and s.ownerId=u.code"
			  +" and u.areaId=p.code and p.type='AR'";   
	  String bthql = " and s.scanTime between ? and ? ";
	  String endhql = " group by s.styleId,substring(p.name,0,2)"+" order by qty";
	  if (startTime == null || endTime == null) {
	        hql += endhql;
	        hql = hql + " desc";
	        return this.showRecordDao.find(hql, new Object[]{});
	   } else {
	        hql += bthql + endhql;
	        hql = hql + " desc";

	        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
	   }
		
  }
  public List<ShowSkuVo> findAreaOtherSku(Date startTime, Date endTime ,String styleIds) {
	  String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(substring(p.name,0,2),count(*) as qty)"
              + " from ShowRecord as s,Unit as u,PropertyKey p where s.styleId not in ("+styleIds +") and s.ownerId=u.code"
			  +" and u.areaId=p.code and p.type='AR'";   
	  String bthql = " and s.scanTime between ? and ? ";
	  String endhql = " group by substring(p.name,0,2)"+" order by qty";
	  if (startTime == null || endTime == null) {
	        hql += endhql;
	        hql = hql + " desc";
	        return this.showRecordDao.find(hql, new Object[]{});
	   } else {
	        hql += bthql + endhql;
	        hql = hql + " desc";

	        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
	   }
		
  }
  public List<ShowSkuVo> findTop10ShowTrend(String ownerIds, String styleIds, Date startTime,Date endTime) {
	String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(s.styleId,year(s.scanTime),month(s.scanTime),day(s.scanTime),count(*) as qty)"
              + " from ShowRecord s where s.styleId in ("+styleIds +")";             
    if(CommonUtil.isNotBlank(ownerIds)) {
        hql = hql + " and s.ownerId in ("+ownerIds +")";
    }    
    String bthql = " and s.scanTime between ? and ? ";
    String endhql = " group by s.styleId,year(s.scanTime),month(s.scanTime),day(s.scanTime)";

    if (startTime == null || endTime == null) {
        hql += endhql;        
        return this.showRecordDao.find(hql, new Object[]{});
    } else {
        hql += bthql + endhql;       

        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
    }
  }
  public List<ShowSkuVo> findOthersShowTrend(String ownerIds, String styleIds,
			Date startTime, Date endTime) {
	String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(year(s.scanTime),month(s.scanTime),day(s.scanTime),count(*) as qty)"
              + " from ShowRecord s where s.styleId in ("+styleIds +")";             
    if(CommonUtil.isNotBlank(ownerIds)) {
        hql = hql + " and s.ownerId in ("+ownerIds +")";
    }    
    String bthql = " and s.scanTime between ? and ? ";
    String endhql = " group by year(s.scanTime),month(s.scanTime),day(s.scanTime)";

    if (startTime == null || endTime == null) {
        hql += endhql;        
        return this.showRecordDao.find(hql, new Object[]{});
    } else {
        hql += bthql + endhql;       

        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
    }
  }
  public List<ShowSkuVo> findShopCount(Date startTime, Date endTime) {
	String hql = "select " +" new com.casesoft.dmc.controller.shop.ShowSkuVo(u.name,count(u.name) as qty,u.longitude,u.latitude,u.province)"
              + " from ShowRecord s, Unit u where s.ownerId = u.id and u.address is not null ";             
      
    String bthql = " and s.scanTime between ? and ? ";
    String endhql = " group by u.name,u.longitude,u.latitude,u.province order by qty desc";

    if (startTime == null || endTime == null) {
        hql += endhql;        
        return this.showRecordDao.find(hql, new Object[]{});
    } else {
        hql += bthql + endhql;       

        return this.showRecordDao.find(hql, new Object[]{startTime, endTime});
    }
  }
  
  

public List<PropertyKey> findPropertyKeyByName(String name) {
	String hql = "from PropertyKey p where p.name like '%" + name + "%'";;
	return this.showRecordDao.find(hql);	
}











}
