package com.casesoft.dmc.service.trace;

import java.util.List;

import com.casesoft.dmc.model.logistics.BillRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.trace.RecordDao;
import com.casesoft.dmc.model.task.Record;

@Service
@Transactional
public class RecordService extends AbstractBaseService<Record, String> {

  @Autowired
  private RecordDao recordDao;

  @Transactional(readOnly = true)
  @Override
  public Page<Record> findPage(Page<Record> page, List<PropertyFilter> filters) {
    return this.recordDao.findPage(page, filters);
  }

  @Override
  public void save(Record entity) {
    // TODO Auto-generated method stub

  }
  
  public void saveList(List<Record> recordList) {
	  this.recordDao.doBatchInsert(recordList);
  }

  @Override
  public Record load(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record get(String propertyName, Object value) {
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public List<Record> find(List<PropertyFilter> filters) {
    return this.recordDao.find(filters);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Record> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

 
  @Override
  public <X> List<X> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(Record entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Record entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(String id) {
    // TODO Auto-generated method stub

  }

  public Record getRecordByCode(String code){
    return this.recordDao.findUnique("from Record t where t.code =?",new Object[]{code});
  }

  public List<BillRecord> getBillRecordlistBybillNo(String billNo){
    return this.recordDao.findUnique("from BillRecord t where t.billNo =?",new Object[]{billNo});
  }

  public List<Record> getRecordsByCode(String code){
    return this.recordDao.find("from Record where code = ? and token <> 9 and token <> 16 order by taskId desc", code);
  }

  public RecordDao getRecordDao() {
    return recordDao;
  }

  public void setRecordDao(RecordDao recordDao) {
    this.recordDao = recordDao;
  }






  


}
