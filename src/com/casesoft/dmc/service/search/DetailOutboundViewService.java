package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailOutboundViewDao;
import com.casesoft.dmc.model.search.DetailOutboundView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DetailOutboundViewService {
    @Autowired
    private DetailOutboundViewDao outboundViewDao;

    @Transactional(readOnly = true)
    public Page<DetailOutboundView> findPage(Page<DetailOutboundView> page, List<PropertyFilter> filters) {
        return this.outboundViewDao.findPage(page, filters);
    }

	public List<DetailOutboundView> findByIds(String ids) {
		// TODO Auto-generated method stub
		return this.outboundViewDao.find("from DetailOutboundView  d where d.id in ("+ids+")");
	}
	
	public List<DetailOutboundView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.outboundViewDao.find(filters);
	}
	
}
