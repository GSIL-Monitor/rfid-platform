package com.casesoft.dmc.service.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.controller.search.temp.SaleAndFittingStatistics;
import com.casesoft.dmc.controller.search.temp.SaleRanking;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailSaleViewDao;
import com.casesoft.dmc.model.search.DetailSaleView;


@Service
@Transactional
public class DetailSaleViewService {
    @Autowired
    private DetailSaleViewDao saleViewDao;

    @Transactional(readOnly = true)
    public Page<DetailSaleView> findPage(Page<DetailSaleView> page, List<PropertyFilter> filters) {
        return this.saleViewDao.findPage(page, filters);
    }

	public List<DetailSaleView> findByIds(String ids) {
		// TODO Auto-generated method stub
		return this.saleViewDao.find("from DetailSaleView  d where d.id in ("+ids+")");
	}
	
	public List<DetailSaleView> find(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return this.saleViewDao.find(filters);
	}

	public List<SaleRanking> findSaleRanking(boolean isAll,String warehId,String beginDate,String endDate){
		StringBuilder hql=new StringBuilder();
		if(isAll){
			hql.append("select new com.casesoft.dmc.controller.search.temp.SaleRanking("
					+ "d.styleId,sum(d.qty),sum(d.actPrice)) "
					+ " from   DetailSaleView  d GROUP by d.styleId order by sum(d.qty) desc");
			return this.saleViewDao.find(hql.toString(),new Object[]{});
		}else{
			hql.append("select new com.casesoft.dmc.controller.search.temp.SaleRanking("
					+ "d.warehId,d.styleId,sum(d.qty),sum(d.actPrice)) "
					+ " from   DetailSaleView  d  where "
					+ " d.warehId=? and d.day between ? and ? GROUP by d.warehId,d.styleId order by sum(d.qty) desc");
			
			return this.saleViewDao.find(hql.toString(),new Object[]{warehId,beginDate,endDate});
			
		}
	}
	public List<SaleRanking> findSaleRankingByWareh(boolean isAll,String styleId,String beginDate,String endDate){
		StringBuilder hql=new StringBuilder();
		if(isAll){
			hql.append("select new com.casesoft.dmc.controller.search.temp.SaleRanking("
					+ "d.warehId,sum(d.qty),sum(d.actPrice),(select sum(s.actPrice) from DetailSaleView s)) "
					+ " from   DetailSaleView  d GROUP by d.warehId order by sum(d.actPrice) desc");
			return this.saleViewDao.find(hql.toString(),new Object[]{});
		}else{
			String chStyleHql="";
			String faStyleHql="";
			if(styleId!=null&&!"".equals(styleId.replace(" ", ""))){
				chStyleHql+= " and s.styleId='"+styleId+"'";
						/*+ "  and  d.day between '"+beginDate+"' and '"+endDate+"'"*/;
				faStyleHql+= " d.styleId='"+styleId+"' and ";

			}
			hql.append("select new com.casesoft.dmc.controller.search.temp.SaleRanking("
					+ "d.warehId,sum(d.qty),sum(d.actPrice),"
					+ "(select sum(s.actPrice) from DetailSaleView s where s.day between ? and ? "
					+chStyleHql
					+ ")) "
					+ " from   DetailSaleView  d  where "
					+faStyleHql
					+ "  d.day between ? and ? GROUP by d.warehId order by sum(d.actPrice) desc");
			
			return this.saleViewDao.find(hql.toString(),new Object[]{beginDate,endDate,beginDate,endDate});
			
		}
	
	}
	public List<SaleAndFittingStatistics> findSalesStatistic(String warehId,String styleId,String beginDate,String endDate){
		StringBuilder hql=new StringBuilder();
		String faStyleHql="";
		if(warehId!=null&&!"".equals(warehId.replace(" ", ""))){
			faStyleHql+= " d.warehId='"+warehId+"' and ";
		}
		if(styleId!=null&&!"".equals(styleId.replace(" ", ""))){
			faStyleHql+= " d.styleId='"+styleId+"' and ";
		}
		hql.append("select new com.casesoft.dmc.controller.search.temp.SaleAndFittingStatistics("
				+ "d.warehId,d.day,d.sku,d.styleId,d.sizeId,d.colorId,sum(d.qty),sum(d.actPrice))"
				+ " from   DetailSaleView  d  where "
				+faStyleHql
				+ "  d.day between ? and ? "
				+ "GROUP by d.warehId,d.day,d.sku,d.styleId,d.sizeId,d.colorId ");
		
		return this.saleViewDao.find(hql.toString(),new Object[]{beginDate,endDate});
	}
	
}
