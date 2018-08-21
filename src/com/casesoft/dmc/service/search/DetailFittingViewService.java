package com.casesoft.dmc.service.search;

import com.casesoft.dmc.controller.search.temp.SaleAndFittingStatistics;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailFittingViewDao;
import com.casesoft.dmc.model.search.DetailFittingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DetailFittingViewService {
    @Autowired
    private DetailFittingViewDao fittingViewDao;

    @Transactional(readOnly = true)
    public Page<DetailFittingView> findPage(Page<DetailFittingView> page, List<PropertyFilter> filters) {
        return this.fittingViewDao.findPage(page, filters);
    }

	public List<DetailFittingView> findByIds(String ids) {
		// TODO Auto-generated method stub
		return this.fittingViewDao.find("from DetailFittingView  d where d.id in ("+ids+")");
	}
	
	public List<DetailFittingView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.fittingViewDao.find(filters);
	}

	public List<DetailFittingView> findDetailFittingByCount(String count) {		
	
		String hql = "select new com.casesoft.dmc.model.search.DetailFittingView("+count
				   + ",styleId,colorId,sizeId,sizeSortId,sku,sum(qty),ownerId,"				   
				   + "warehId,year,'"+count+"')  from DetailFittingView  group by "+count+","
				   + "year,styleId,colorId,sizeId,sku,sizeSortId,ownerId,warehId order by "+count+" desc" ;
		List<DetailFittingView>  list= this.fittingViewDao.find(hql);
		return list;
	}
	
	public List<SaleAndFittingStatistics> findFittingStatistic(String warehId,String styleId,String beginDate,String endDate){
		StringBuilder hql=new StringBuilder();
		String faStyleHql="";
		if(warehId!=null&&!"".equals(warehId.replace(" ", ""))){
			faStyleHql+= " d.warehId='"+warehId+"' and ";
		}
		if(styleId!=null&&!"".equals(styleId.replace(" ", ""))){
			faStyleHql+= " d.styleId='"+styleId+"' and ";
		}
		hql.append("select new com.casesoft.dmc.controller.search.temp.SaleAndFittingStatistics("
				+ "d.warehId,d.day,d.sku,d.styleId,d.sizeId,d.colorId,sum(d.qty))"
				+ " from   DetailFittingView  d  where "
				+faStyleHql
				+ "  d.day between ? and ? "
				+ "GROUP by d.warehId,d.day,d.sku,d.styleId,d.sizeId,d.colorId ");
		
		return this.fittingViewDao.find(hql.toString(),new Object[]{beginDate,endDate});
	}
	
}
