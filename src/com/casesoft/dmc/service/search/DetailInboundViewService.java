package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailInboundViewDao;
import com.casesoft.dmc.model.search.DetailInboundView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DetailInboundViewService {
    @Autowired
    private DetailInboundViewDao inboundViewDao;

    @Transactional(readOnly = true)
    public Page<DetailInboundView> findPage(Page<DetailInboundView> page, List<PropertyFilter> filters) {
        return this.inboundViewDao.findPage(page, filters);
    }

	public List<DetailInboundView> findByIds(String ids) {
		// TODO Auto-generated method stub
		return this.inboundViewDao.find("from DetailInboundView  d where d.id in ("+ids+")");
	}
	
	public List<DetailInboundView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.inboundViewDao.find(filters);
	}
	
}
