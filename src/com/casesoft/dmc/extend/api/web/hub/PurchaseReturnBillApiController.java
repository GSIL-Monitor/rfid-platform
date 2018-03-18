package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.PurchaseReturnBill;
import com.casesoft.dmc.model.logistics.PurchaseReturnBillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PurchaseReturnBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by Session on 2017-07-11.
 */

@Api
@Controller
@RequestMapping(value = "/api/hub/purchaseRefund")
public class PurchaseReturnBillApiController extends BaseInfoApiController{
	@Autowired
	private PurchaseReturnBillService purchaseReturnBillService;



	@RequestMapping(value = "/listWS.do")
	@ResponseBody
	private MessageBox listWS(){
		this.logAllRequestParams();
		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		String userId = this.getReqParam("userId");
		User curUser = CacheManager.getUserById(userId);
		if(CommonUtil.isNotBlank(curUser)){
			if(!curUser.getId().equals("admin")){
				PropertyFilter filter = new PropertyFilter("EQS_ownerId", curUser.getOwnerId());
				filters.add(filter);
			}
		}
		List<PurchaseReturnBill> purchaseReturnBills =this.purchaseReturnBillService.find(filters);

		return returnSuccessInfo("获取成功",purchaseReturnBills);
	}

	@RequestMapping(value = "/listDtlWS.do")
	@ResponseBody
	private MessageBox listDtlWS(String billNo){
		this.logAllRequestParams();
		List<PurchaseReturnBillDtl> details =this.purchaseReturnBillService.findDetailsByBillNo(billNo);

		return returnSuccessInfo("获取成功",details);
	}

	@RequestMapping(value = "/saveWS.do")
	@ResponseBody
	private MessageBox saveWS(String bill,String billDtls,String userId){
		this.logAllRequestParams();

		PurchaseReturnBill purchaseReturnBill = JSON.parseObject(bill,PurchaseReturnBill.class);
		List<PurchaseReturnBillDtl> purchaseReturnBillDtls =JSON.parseArray(billDtls,PurchaseReturnBillDtl.class);


		if(CommonUtil.isBlank(purchaseReturnBill.getBillNo())){
			String prefix = BillConstant.BillPrefix.purchaseReturn
					+ CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
			//String billNo=this.purchaseReturnBillService.findMaxBillNo(prefix);
			purchaseReturnBill.setBillNo(prefix);
			purchaseReturnBill.setId(prefix);
		}else {
			purchaseReturnBill.setId(purchaseReturnBill.getBillNo());
			for(PurchaseReturnBillDtl detail:purchaseReturnBillDtls){
				if(CommonUtil.isNotBlank(detail.getId())){
					detail.setBillNo(purchaseReturnBill.getBillNo());
					detail.setBillId(purchaseReturnBill.getBillNo());
				}
			}
		}

		try {
			User user = CacheManager.getUserById(userId);
			BillConvertUtil.convertToPurchaseReturnBill(purchaseReturnBill, purchaseReturnBillDtls, user);
			this.purchaseReturnBillService.saveBatch(purchaseReturnBill,purchaseReturnBillDtls);
			return returnSuccessInfo("保存成功",purchaseReturnBill.getBillNo());
		}catch(Exception e){
			e.printStackTrace();
			return returnFailInfo("保存失败",e.getMessage());
		}

	}

}
