package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.controller.shop.FittingSkuVo;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.ScoreDao;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.shop.Score;
import com.casesoft.dmc.model.shop.ShowRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ScoreService extends AbstractBaseService<Score, String> {

  @Autowired
  private ScoreDao scoreDao;

  @Override
  @Transactional(readOnly = true)
  public Page<Score> findPage(Page<Score> page, List<PropertyFilter> filters) {
    return this.scoreDao.findPage(page, filters);
  }

  @Override
  public void save(Score entity) {
    this.scoreDao.save(entity);

  }

  public void saveScoreList(List<Score> scoreList) {
    for (Score s : scoreList) {
      this.save(s);
    }
  }

  public void saveScoreList(List<Score> scoreList, List<FittingRecord> records) {
    this.scoreDao.doBatchInsert(scoreList);
    if (records.size() > 0)
      this.scoreDao.doBatchInsert(records);
  }
  public void saveShowRecord(List<ShowRecord> records) {
	  if (records.size() > 0){
		  this.scoreDao.doBatchInsert(records);
	  }	      
  }

  @Override
  @Transactional(readOnly = true)
  public Score load(String id) {
    return this.scoreDao.load(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Score get(String propertyName, Object value) {
    return this.scoreDao.findUniqueBy(propertyName, value);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Score> find(List<PropertyFilter> filters) {
    return this.scoreDao.find(filters);
  }

  @Transactional(readOnly = true)
  public List<Object> findFitting(String ownerId, int length, boolean asc, Date startTime,
      Date endTime) {

    String hql = "select s.code,s.styleNo,s.colorNo,s.sizeNo,sum(s.count) as sum_count,"
        + "sum(s.styleScore),sum(s.qualityScore),sum(s.priceScore) from Score s "
        + "where s.count>0 and ownerId=?";
    String bthql = "and s.scoreTime between ? and ? ";
    String endhql = " group by s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by sum_count";

    if (startTime == null || endTime == null) {
      hql += endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] { ownerId });
    } else {
      hql += bthql + endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] { ownerId, startTime, endTime });
    }

    // this.scoreDao.findInLength(hql, length, new Object[] { startTime,
    // endTime });

  }
  @Transactional(readOnly = true)
  public List<Object> findFittingRecord(String ownerId, int length, boolean asc, Date startTime,
      Date endTime) {

    String hql = "select s.code,s.styleNo,s.colorNo,s.sizeNo,sum(s.count) as sum_count,"
        + "sum(s.styleScore),sum(s.qualityScore),sum(s.priceScore) from Score s "
        + "where s.count>0 and ownerId in("+ownerId+") ";
    String bthql = "and s.scoreTime between ? and ? ";
    String endhql = " group by s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by sum_count";

    if (startTime == null || endTime == null) {
      hql += endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] {});
    } else {
      hql += bthql + endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] {startTime, endTime });
    }

    // this.scoreDao.findInLength(hql, length, new Object[] { startTime,
    // endTime });

  }
  @Transactional(readOnly = true)
  public List<Object> findFittingStyleRecord(String ownerId, int length, boolean asc, Date startTime,
      Date endTime) {

    String hql = "select s.styleNo,s.ownerId,sum(s.count) as sum_count,"
        + "sum(s.styleScore),sum(s.qualityScore),sum(s.priceScore) from Score s "
        + "where s.count>0 and s.ownerId in("+ownerId+") ";
    String bthql = "and s.scoreTime between ? and ? ";
    String endhql = " group by s.styleNo,s.ownerId" + " order by sum_count";

    if (startTime == null || endTime == null) {
      hql += endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] {});
    } else {
      hql += bthql + endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] {startTime, endTime });
    }

  }
  @Transactional(readOnly = true)
  public List<Object> findFitting2(String ownerId, int length, boolean asc, Date startTime,
      Date endTime) {

    String hql = "select s.code,s.styleNo,s.colorNo,s.sizeNo,sum(s.count) as sum_count,"
        + "sum(s.styleScore),sum(s.qualityScore),sum(s.priceScore) from Score s "
        + "where s.count>0 and ownerId=?";
    String bthql = "and s.scoreTime between ? and ? ";
    String endhql = " group by s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by sum_count";

    if (startTime == null || endTime == null) {
      hql += endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] { ownerId });
    } else {
      hql += bthql + endhql;
      if (!asc) {
        hql = hql + " desc";
      }
      return this.scoreDao.findInLength(hql, length, new Object[] { ownerId, startTime, endTime });
    }

    // this.scoreDao.findInLength(hql, length, new Object[] { startTime,
    // endTime });

  }
  @Transactional(readOnly = true)
  public List<Object> findScore(String ownerId, int length, boolean asc, Date startTime,
      Date endTime, String order) {
    String hql = "select s.code,s.styleNo,s.colorNo,s.sizeNo,sum(s.count) as sum_count,"
        + "sum(s.styleScore) as sScore,sum(s.qualityScore) as qScore,sum(s.priceScore) as pScore"
        + " from Score s " + "where s.count=0 and s.scoreTime between ?"
        + "and ? group by s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by " + order;
    if (!asc)
      hql = hql + " desc";
    return this.scoreDao.findInLength(hql, length, new Object[] { startTime, endTime });
  }

  @Override
  public List<Score> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <X> List<X> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(Score entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Score entity) {
    this.scoreDao.delete(entity);
  }

  @Override
  public void delete(String id) {
    if (CommonUtil.isBlank(id)) {
      this.scoreDao.batchExecute("delete Score s", new Object[] {});
      this.scoreDao.batchExecute("delete FittingRecord f", new Object[] {});
    }
  }

  public void delete(List<Score> scoreList, boolean isDelRecords) {
    for (Score s : scoreList) {
      this.delete(s, isDelRecords);
    }
  }

  public void delete(Score entity, boolean isDelRecords) {
    this.delete(entity);
    if (isDelRecords)
      this.scoreDao.batchExecute("delete FittingRecord r where r.taskId=?",
          new Object[] { entity.getId() });
  }

    /**
     this.fittingTime = fittingTime;
     this.ownerId = ownerId;
     this.styleId = styleId;
     this.colorId = colorId;
     this.sizeId = sizeId;
     this.qty = qty;
     * @param ownerIds
     * @param startTime
     * @param endTime
     * @return
     */
    @Transactional(readOnly = true)
    public List<FittingSkuVo> findFittingSkuList(String ownerIds,String styleIds, Date startTime,
                                    Date endTime) {

        String hql = "select " +"new com.casesoft.dmc.controller.shop.FittingSkuVo(s.styleNo,s.colorNo,s.sizeNo,sum(s.count) as qty)"
                + " from Score s "
                + "where s.count>0 ";//
        if(CommonUtil.isNotBlank(ownerIds)) {
            hql = hql + " and s.ownerId in ("+ownerIds +")";
        }
        if(CommonUtil.isNotBlank(styleIds)) {
            hql = hql + " and s.styleNo in ("+styleIds +")";
        }
        String bthql = " and s.scoreTime between ? and ? ";
        String endhql = " group by s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by qty";

        if (startTime == null || endTime == null) {
            hql += endhql;
            hql = hql + " desc";
            return this.scoreDao.find(hql, new Object[]{});
        } else {
            hql += bthql + endhql;
            hql = hql + " desc";

            return this.scoreDao.find(hql, new Object[]{startTime, endTime});
        }

    }
    @Transactional(readOnly = true)
    public List<FittingSkuVo> findFittingSkuList2(String ownerIds, String styleIds, Date startTime,
                                                 Date endTime) {

        String hql = "select " +"new com.casesoft.dmc.controller.shop.FittingSkuVo(s.ownerId,s.styleNo,s.colorNo,s.sizeNo,sum(s.count))"
                + " from Score s "
                + "where s.count>0 ";//
        if(CommonUtil.isNotBlank(ownerIds)) {
            hql = hql + " and s.ownerId in ("+ownerIds +")";
        }
        if(CommonUtil.isNotBlank(styleIds)) {
            hql = hql + " and s.styleNo in ("+styleIds +")";
        }
        String bthql = " and s.scoreTime between ? and ? ";
        String endhql = " group by s.ownerId,s.code,s.styleNo,s.colorNo,s.sizeNo" + " order by code";

        if (startTime == null || endTime == null) {
            hql += endhql;
            hql = hql + " desc";
            return this.scoreDao.find(hql,new Object[]{});
        } else {
            hql += bthql + endhql;
            hql = hql + " desc";

            return this.scoreDao.find(hql, new Object[]{startTime, endTime});
        }

    }

  public ScoreDao getScoreDao() {
    return scoreDao;
  }

  public void setScoreDao(ScoreDao scoreDao) {
    this.scoreDao = scoreDao;
  }



}