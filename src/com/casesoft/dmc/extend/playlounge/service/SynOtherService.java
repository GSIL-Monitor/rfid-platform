package com.casesoft.dmc.extend.playlounge.service;

import com.casesoft.dmc.extend.playlounge.dao.SynOtherDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.ISynOtherDuty;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.tag.InitService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SynOtherService  implements ISynOtherDuty{

	private SynOtherDao synOtherDao;

	private InitService initService;

	@Override
	public void batchEpc(List<Epc> epcs) {
		this.synOtherDao.batchEpc(this.initService.getAllCurrentEpc());
		
	}

	@Override
	public void batchFittingRecord() {
		
	}

	@Override
	public List<SaleBill> downloadSaleInfo() {
		return this.synOtherDao.downloadSaleInfo();
	}
	public SynOtherDao getSynOtherDao() {
		return synOtherDao;
	}
	
	public void setSynOtherDao(SynOtherDao synOtherDao) {
		this.synOtherDao = synOtherDao;
	}

	public InitService getInitService() {
		return initService;
	}

	public void setInitService(InitService initService) {
		this.initService = initService;
	}
}
