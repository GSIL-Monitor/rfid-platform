package com.casesoft.dmc.service.log;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.log.SysLogDao;
import com.casesoft.dmc.model.log.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class SysLogService extends AbstractBaseService<SysLog,String>{

	@Autowired
	private SysLogDao sysLogDao;
	@Override
	public Page<SysLog> findPage(Page<SysLog> page,
			List<PropertyFilter> filters) {
		return this.sysLogDao.findPage(page, filters);
	}

	@Override
	public void save(SysLog entity) {
		this.sysLogDao.save(entity);
	}
	
	public void saveUpdateVersion(String tableName, long version) {
		 SysLog sysLog = new SysLog();
         sysLog.setTableName(tableName);
         sysLog.setLogDate(new Date());
         sysLog.setVersion(version);
         this.sysLogDao.saveOrUpdateX(sysLog);
	}
 
	@Override
	public SysLog load(String id) {
		return null;
	}

	public long getVersionByTable(String tableName){
		Long v=this.sysLogDao.findUnique("select max(l.version) from "+tableName+" l )", new Object[]{});
		return v==null?0:v;
	}
	
	public String getGEVersions(String tableName, long version) {
		List<SysLog> sysLogList = this.getGEVersionList(tableName, version);
		StringBuffer versions = new StringBuffer();
		for(SysLog sysLog : sysLogList) {
			versions.append(",").append(sysLog.getVersion());
		}
		return versions.toString().substring(1);
	}
	public List<SysLog> getGEVersionList(String tableName, long version) {
		List<SysLog> sysLogList = this.sysLogDao.find("from SysLog l where l.tableName=? and l.version>?",
				new Object[]{tableName,version});
		
		return sysLogList;
	}
	
	@Override
	public SysLog get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysLog> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysLog> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(SysLog entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SysLog entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
