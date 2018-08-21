package com.casesoft.dmc.service.cfg;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.cfg.DeviceDao;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.cfg.DeviceConfig;
import com.casesoft.dmc.model.cfg.DeviceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeviceService extends AbstractBaseService<Device, String> {

  @Autowired
  private DeviceDao deviceDao;

  @Override
  @Transactional(readOnly = true)
  public Page<Device> findPage(Page<Device> page, List<PropertyFilter> filters) {
    return this.deviceDao.findPage(page, filters);
  }

  public List<DeviceLog> getDeviceLogByDeviceId(String deviceId){
	  String hql="from DeviceLog d where d.deviceId=? order by d.date desc ";
	  return this.deviceDao.find(hql, new Object[]{deviceId});
  }
  
  public Device findUniqueByCode(String code){
	  String hql="from Device d where d.code=?";
	  return this.deviceDao.findUnique(hql,new Object[]{code});
  }
  
  public void saveDeviceLog(DeviceLog deviceLog){
	  this.deviceDao.saveOrUpdateX(deviceLog);
  }
  @Override
  public void save(Device entity) {
    this.deviceDao.saveOrUpdate(entity);
  }

  @Override
  public void update(Device entity) {
    this.deviceDao.update(entity);
  }

  @Override
  public void delete(Device entity) {
    this.deviceDao.delete(entity);
  }

  @Override
  public void delete(String id) {
    this.deviceDao.delete(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Device load(String id) {
    return this.deviceDao.load(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Device get(String propertyName, Object value) {
    return this.deviceDao.findUniqueBy(propertyName,value);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Device> find(List<PropertyFilter> filters) {
    return this.deviceDao.find(filters);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Device> getAll() {
    return this.deviceDao.getAll();
  }

  public DeviceDao getDeviceDao() {
    return deviceDao;
  }

  public void setDeviceDao(DeviceDao deviceDao) {
    this.deviceDao = deviceDao;
  }

  @Override
  public <X> List<X> findAll() {
    return null;
  }

    public List<DeviceConfig> getDeviceConfig(String deviceId) {
        return this.deviceDao.find("from DeviceConfig dc where dc.deviceId=?",new Object[]{deviceId});
    }
    public void saveConfig(DeviceConfig deviceConfig) {
        this.deviceDao.saveOrUpdateX(deviceConfig);
    }
}
