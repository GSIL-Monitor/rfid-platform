package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBill;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Session on 2017-07-11.
 * 销售退货接口
 */
@Api
@Controller
@RequestMapping(value = "/api/hub/saleOrderRefund")
public class SaleOrderaReturnBillApiController extends BaseInfoApiController{

	@Autowired
	private SaleOrderReturnBillService saleOrderReturnBillService;
	private static  Queue<Object> objectQueue = new LinkedList<>();
	@RequestMapping(value = "/listWS.do")
	@ResponseBody
	public MessageBox listWS(){
		this.logAllRequestParams();

		List<PropertyFilter> filterList =PropertyFilter.buildFromHttpRequest(this.getRequest());
		String userId = this.getReqParam("userId");
		User curUser = CacheManager.getUserById(userId);
		if(CommonUtil.isNotBlank(curUser)){
			if(!curUser.getId().equals("admin")){
				PropertyFilter filter = new PropertyFilter("EQS_ownerId", curUser.getOwnerId());
				filterList.add(filter);
			}
		}
		List<SaleOrderReturnBill> billList =this.saleOrderReturnBillService.find(filterList);
		for(SaleOrderReturnBill sb:billList){
			if(CommonUtil.isNotBlank(sb.getOrigUnitId())){
				if(CommonUtil.isNotBlank(sb.getCustomerType())) {
					if (sb.getCustomerType().equals(BillConstant.customerType.Customer)) {
						if(CommonUtil.isNotBlank(CacheManager.getCustomerById(sb.getOrigUnitId()))){
							sb.setOrigUnitName(CacheManager.getCustomerById(sb.getOrigUnitId()).getName());
						}
					} else {
						if(CommonUtil.isNotBlank(CacheManager.getUnitByCode(sb.getOrigUnitId()))){
							sb.setOrigUnitName(CacheManager.getUnitByCode(sb.getOrigUnitId()).getName());
						}
					}
				}
			}
		}

		return returnSuccessInfo("获取成功",billList);
	}


	@RequestMapping(value = "/listDtlWS.do")
	@ResponseBody
	public  MessageBox listDtlWS(String billNo){
		this.logAllRequestParams();

		List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls =this.saleOrderReturnBillService.findDtlByBillNo(billNo);
		return returnSuccessInfo("获取成功",saleOrderReturnBillDtls);
	}

	/**
	 * @param saleOrderReturnArray 上传JSON Array数据
	 * */
	@RequestMapping(value="/saveJSONWS")
	@ResponseBody
	public void saveSaleOrderBill(String saleOrderReturnArray){
		this.logAllRequestParams();
		JSONArray jsonArray = JSON.parseArray(saleOrderReturnArray);
		RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");
		//Queue<Object> objectQueue = new LinkedList<>();
		for(Object o : jsonArray){
			SaleOrderReturnBill saleOrderReturnBill=null;
			try {
				saleOrderReturnBill = JSON.parseObject(JSON.toJSON(o).toString(),SaleOrderReturnBill.class);
				List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls = saleOrderReturnBill.getDtlList();
				this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBill,saleOrderReturnBillDtls,null);
				boolean sHasKey = redisUtils.sHasKey("saleOrderReturnBill", saleOrderReturnBill.getId());
				if(sHasKey){
					redisUtils.setRemove("saleOrderReturnBill",saleOrderReturnBill.getId());
				}
			}catch (Exception e){
				this.logger.error("保存失败");
				objectQueue.add(o);
				saleOrderReturnBill.setErrorMessage(e.getMessage());
				if(CommonUtil.isNotBlank(saleOrderReturnBill)) {
					redisUtils.hset("saleOrderReturnBill", saleOrderReturnBill.getId(), JSON.toJSONString(saleOrderReturnBill));
				}
			}
		}
	}


	@RequestMapping(value = "/saveWS.do")
	@ResponseBody
	public MessageBox saveWS(String bill,String strDtlList,String userId){
		System.out.println("bill="+bill);
		System.out.println("strDtlList="+strDtlList);
		SaleOrderReturnBill saleOrderReturnBill = JSON.parseObject(bill,SaleOrderReturnBill.class);
		List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls =JSON.parseArray(strDtlList,SaleOrderReturnBillDtl.class);

		if(CommonUtil.isBlank(saleOrderReturnBill.getBillNo())){
			String prefix= BillConstant.BillPrefix.SaleOrderReturn+CommonUtil.getDateString(new Date(),"yyMMddHHmmssSSS");
			//String billNo=this.saleOrderReturnBillService.findMaxBillNO(prefix);
			saleOrderReturnBill.setBillNo(prefix);
			saleOrderReturnBill.setId(prefix);
		}
		saleOrderReturnBill.setId(saleOrderReturnBill.getBillNo());
		for(SaleOrderReturnBillDtl sdtl:saleOrderReturnBillDtls){
			if(CommonUtil.isNotBlank(sdtl.getId())){
				sdtl.setBillNo(saleOrderReturnBill.getBillNo());
				sdtl.setBillId(saleOrderReturnBill.getBillNo());
			}
		}
		try {
			User user = CacheManager.getUserById(userId);
			BillConvertUtil.convertToSaleOrderReturnBill(saleOrderReturnBill, saleOrderReturnBillDtls, user);
			this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBill,saleOrderReturnBillDtls,null);
			return returnSuccessInfo("保存成功",saleOrderReturnBill.getBillNo());
		}catch (Exception e){
			e.printStackTrace();
			return returnFailInfo("服务器处理失败");
		}
	}



}
