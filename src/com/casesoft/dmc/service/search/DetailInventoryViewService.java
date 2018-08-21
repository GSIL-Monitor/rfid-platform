package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailInventoryViewDao;
import com.casesoft.dmc.model.search.DetailInventoryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DetailInventoryViewService {
    @Autowired
    private DetailInventoryViewDao inventoryViewDao;

    @Transactional(readOnly = true)
    public Page<DetailInventoryView> findPage(Page<DetailInventoryView> page, List<PropertyFilter> filters) {
        return this.inventoryViewDao.findPage(page, filters);
    }

	public List<DetailInventoryView> findByIds(String ids) {
		// TODO Auto-generated method stub
		return this.inventoryViewDao.find("from DetailInventoryView  d where d.id in ("+ids+")");
	}
	
	public List<DetailInventoryView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.inventoryViewDao.find(filters);
	}
	
}
