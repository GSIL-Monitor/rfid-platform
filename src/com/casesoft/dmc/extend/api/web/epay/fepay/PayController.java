package com.casesoft.dmc.extend.api.web.epay.fepay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayConstant;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfo;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.EPayInfoService;
import com.casesoft.dmc.extend.api.web.epay.fepay.base.IEPayController;
import com.casesoft.dmc.model.cfg.Device;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/api/epay")
@Scope("prototype")
public class PayController extends BaseController implements IEPayController<EPayInfo> {
	@Autowired
	private EPayInfoService ePayInfoService;
	@RequestMapping(value = "/barPayWS.do")
	@ResponseBody
	@Override
	public MessageBox barPay(EPayInfo epayInfo) {
		this.logAllRequestParams();
		Device device=CacheManager.getDeviceByCode(epayInfo.getDeviceId());
		if(CommonUtil.isBlank(device)){
			return new MessageBox(false,"设备号未注册！");
		}
		MessageBox box=  EPayFactory.createEPayTarget(epayInfo).barPay();
		ePayInfoService.save(epayInfo);
		return box;
	}
	@RequestMapping(value = "/qrPayWS.do")
	@ResponseBody
	@Override
	public MessageBox qrPay(EPayInfo t) {
		this.logAllRequestParams();
		Device device=CacheManager.getDeviceByCode(t.getDeviceId());
		if(CommonUtil.isBlank(device)){
			return new MessageBox(false,"设备号未注册！");
		}
		return  EPayFactory.createEPayTarget(t).qrPay();
	}
	@RequestMapping(value = "/queryWS.do")
	@ResponseBody
	@Override
	public MessageBox query(EPayInfo t) {
		this.logAllRequestParams();
		Device device=CacheManager.getDeviceByCode(t.getDeviceId());
		if(CommonUtil.isBlank(device)){
			return new MessageBox(false,"设备号未注册！");
		}
		return EPayFactory.createEPayTarget(t).query();
	}
	@RequestMapping(value = "/cancelOrderWS.do")
	@ResponseBody
	@Override
	public MessageBox cancelOrder(EPayInfo t) {
		this.logAllRequestParams();
		Device device=CacheManager.getDeviceByCode(t.getDeviceId());
		if(CommonUtil.isBlank(device)){
			return new MessageBox(false,"设备号未注册！");
		}
		MessageBox box= EPayFactory.createEPayTarget(t).cancelOrder();
		ePayInfoService.save(t);
		return box;
	}
	@RequestMapping(value = "/refundOrderWS.do")
	@ResponseBody
	@Override
	public MessageBox refundOrder(EPayInfo t) {
		this.logAllRequestParams();
		Device device=CacheManager.getDeviceByCode(t.getDeviceId());
		if(CommonUtil.isBlank(device)){
			return new MessageBox(false,"设备号未注册！");
		}
		MessageBox box= EPayFactory.createEPayTarget(t).refundOrder();
		ePayInfoService.save(t);
		return box;
	}
	@RequestMapping(value = "/indexWS.do")
	@ResponseBody
	public String index() {
		return null;
	}

	/**
	 *
	 * IBox:{"_inputCharset":"UTF-8","signType":"MD5","sign":"134E4D33BA6E1E99683EC4D1B
	 6FA6982","requestTime":"1482396689175","tradeStatus":"1","errorCode":null,"error
	 Desc":"","tradeNo":"92161222294165117973","outTradeNo":"KE0000011482396615339","
	 terminalNo":"00210101018300021222","tradeTime":"2016-12-22 16:51:31","settlement
	 Date":null,"totalFee":"1","cardNo":null,"tranType":"31","resv":null}
	 * */
	@RequestMapping(value = "/callbackWS.do")
	@ResponseBody
	public String callbackWS() {
		this.logAllRequestParams();
		HttpServletRequest request=this.getRequest();
		String contentStr="";
		try {
			InputStream is= request.getInputStream();
			contentStr= IOUtils.toString(is, "utf-8");
			System.err.println("IBox:"+contentStr);
			JSONObject jsonObject= JSON.parseObject(contentStr);
			EPayInfo ePayInfo=new EPayInfo();
			ePayInfo.setAuthCode(jsonObject.getString("terminalNo"));
			ePayInfo.setDeviceId(jsonObject.getString("terminalNo"));
			ePayInfo.setOperatorCode(jsonObject.getString("terminalNo"));
			ePayInfo.setTotalAmount(jsonObject.getDouble("totalFee")/100);
			ePayInfo.setRefundAmount(0d);
			ePayInfo.setOutTradeNo(jsonObject.getString("outTradeNo"));
			ePayInfo.setTradeCode(jsonObject.getString("tradeNo"));
			ePayInfo.setTaskId(jsonObject.getString("outTradeNo"));
			ePayInfo.setSubject("盒子支付");
			ePayInfo.setTimestamp(jsonObject.getDate("tradeTime"));
			ePayInfo.setSubErrorCode(jsonObject.getString("errorCode"));
			ePayInfo.setSubErrorMsg(jsonObject.getString("errorDesc"));
			ePayInfo.setResultCode(jsonObject.getString("tradeStatus"));
			if(jsonObject.getString("tradeStatus").equals("1")){
				ePayInfo.setResultMsg("支付成功！");
			}else if(jsonObject.getString("tradeStatus").equals("2")){
				ePayInfo.setResultMsg("支付失败！");
			}else{
				ePayInfo.setResultMsg("结果未知！");
			}
			ePayInfo.setRemark("盒子支付");
			ePayInfo.setType(EPayConstant.EpayType.IBOX_TYPE);
			/*//盒子支付交易类型，
			磁条卡：31：消费，32：冲正，33：撤销，34：撤销冲正
			 IC卡：	51：消费，52：冲正,53：撤销，54：撤销冲正*/
			ePayInfo.setTradeType(jsonObject.getString("tranType"));
			this.ePayInfoService.save(ePayInfo);

		} catch (IOException e) {
			e.printStackTrace();
		}
 		return new String("SUCCESS");
	}
}
