package com.casesoft.dmc.extend.api.web.epay.fepay.base;

import com.casesoft.dmc.core.dao.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EPayInfoService {

	@Autowired
	private  EPayInfoDao epayInfoDao;
	public void save(EPayInfo ePayInfo) {
		 this.epayInfoDao.save(ePayInfo);
	}

	public EPayInfo load(Long id) {
		return this.epayInfoDao.load(id);
	}
	
	public List<EPayInfo> find(List<PropertyFilter> filters) {
		return this.epayInfoDao.find(filters);
	}
}
