package com.casesoft.dmc.dao.stock;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.stock.InventoryDto;

@Repository
public class StockDao extends BaseHibernateDao<InventoryDto, String> {

	public List<EpcStock> findEpcStock(String storageId){
		String hql="from EpcStock epcStock where epcStock.warehouseId=? and epcStock.inStock=1";
		return this.find(hql, new Object[]{storageId});
	}
}
