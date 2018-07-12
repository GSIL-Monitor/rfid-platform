package com.casesoft.dmc.core.listener;

import java.util.Date;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.casesoft.dmc.core.Constant;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.service.logistics.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


import com.casesoft.dmc.core.model.Tonline;
import com.casesoft.dmc.core.service.OnlineServiceI;
import com.casesoft.dmc.core.util.resource.ResourceUtil;


/**
 * 监听在线用户上线下线
 * 
 * @author
 * 
 */
public class OnlineListener implements ServletContextListener, ServletContextAttributeListener, HttpSessionListener, HttpSessionAttributeListener, HttpSessionActivationListener, HttpSessionBindingListener, ServletRequestListener, ServletRequestAttributeListener {

	private static final Logger logger = Logger.getLogger(OnlineListener.class);

	private static ApplicationContext ctx = null;

	/*@Autowired
	private SaleOrderBillService saleOrderBillService;*/
	private static SaleOrderBillService saleOrderBill;
	private static SaleOrderReturnBillService saleOrderReturnBill;
	private static ConsignmentBillService consignmentBill;
	private static TransferOrderBillService transferOrderBill;
	private static PurchaseOrderBillService purchaseOrderBill;
	private static PurchaseReturnBillService purchaseReturnBill;

	public OnlineListener() {
	}

	public void requestDestroyed(ServletRequestEvent arg0) {
	}

	/**
	 * 向session里增加属性时调用(用户成功登陆后会调用)
	 */
	public void attributeAdded(HttpSessionBindingEvent evt) {
		try{
			String name = evt.getName();
			logger.debug("向session存入属性：" + name);
			HttpSession session = evt.getSession();
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ResourceUtil.getSessionInfoName());
			if (sessionInfo != null) {
				OnlineServiceI onlineService = (OnlineServiceI) ctx.getBean("onlineService");
				Tonline online = new Tonline();
				online.setCip(sessionInfo.getIp());
				online.setCdatetime(new Date());
				online.setCname(sessionInfo.getLoginName());
				onlineService.updateOnline(online);
			}
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("getAttribute：" + e.getMessage());
		}

	}

	/**
	 * 服务器初始化时调用
	 */
	public void contextInitialized(ServletContextEvent evt) {
		logger.debug("服务器启动");
		ctx = WebApplicationContextUtils.getWebApplicationContext(evt.getServletContext());
	}

	public void sessionDidActivate(HttpSessionEvent arg0) {
	}

	public void valueBound(HttpSessionBindingEvent arg0) {
	}

	public void attributeAdded(ServletContextAttributeEvent arg0) {
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
	}

	/**
	 * session销毁(用户退出系统时会调用)
	 */
	public void sessionDestroyed(HttpSessionEvent evt) {
		HttpSession session = evt.getSession();
		if (session != null) {
			logger.debug("session销毁：" + session.getId());
			//修改单据
			String billNosale=(String)session.getAttribute("billNosale");
			//System.out.println("saleOrder:"+ OnlineListener.saleOrderBill);
			//System.out.println("saleOrder:"+billNosale);
			if(CommonUtil.isNotBlank(billNosale)){
				try {
					SaleOrderBill saleOrderBill = OnlineListener.saleOrderBill.get("billNo", billNosale);
					if(CommonUtil.isNotBlank(saleOrderBill)){
						//System.out.println("saleOrder34:"+saleOrderBill.getId());
						saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.saleOrderBill.save(saleOrderBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();

				}

			}
			String billNosaleReturn=(String)session.getAttribute("billNosaleReturn");
			//System.out.println("saleOrder:"+ OnlineListener.saleOrderReturnBill);
			//System.out.println("saleOrder:"+billNosaleReturn);
			if(CommonUtil.isNotBlank(billNosaleReturn)){
				try {
					SaleOrderReturnBill saleOrderReturnBill = OnlineListener.saleOrderReturnBill.findBillByBillNo(billNosaleReturn);
					if(CommonUtil.isNotBlank(saleOrderReturnBill)){
						//System.out.println("saleOrder34:"+saleOrderReturnBill.getId());
						saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.saleOrderReturnBill.save(saleOrderReturnBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			String billNoConsignment=(String)session.getAttribute("billNoConsignment");
			//System.out.println("saleOrder:"+ OnlineListener.consignmentBill);
			//System.out.println("saleOrder:"+billNoConsignment);
			if(CommonUtil.isNotBlank(billNoConsignment)){
				try {
					ConsignmentBill consignmentBill = OnlineListener.consignmentBill.findBillByBillNo(billNoConsignment);
					if(CommonUtil.isNotBlank(consignmentBill)){
						//System.out.println("saleOrder34:"+consignmentBill.getId());
						consignmentBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.consignmentBill.update(consignmentBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			String billNotransfer=(String)session.getAttribute("billNotransfer");
			//System.out.println("saleOrder:"+ OnlineListener.transferOrderBill);
			//System.out.println("saleOrder:"+billNoConsignment);
			if(CommonUtil.isNotBlank(billNoConsignment)){
				try {
					TransferOrderBill transferOrderBill = OnlineListener.transferOrderBill.get("billNo", billNotransfer);
					if(CommonUtil.isNotBlank(transferOrderBill)){
						System.out.println("saleOrder34:"+transferOrderBill.getId());
						transferOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.transferOrderBill.update(transferOrderBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			String billNopurchase=(String)session.getAttribute("billNopurchase");
			//System.out.println("saleOrder:"+ OnlineListener.purchaseOrderBill);
			//System.out.println("saleOrder:"+billNoConsignment);
			if(CommonUtil.isNotBlank(billNoConsignment)){
				try {
					PurchaseOrderBill purchaseOrderBill = OnlineListener.purchaseOrderBill.get("billNo",billNopurchase);
					if(CommonUtil.isNotBlank(consignmentBill)){
						//System.out.println("saleOrder34:"+purchaseOrderBill.getId());
						purchaseOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.purchaseOrderBill.save(purchaseOrderBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			String billNoPurchaseReturn=(String)session.getAttribute("billNoPurchaseReturn");
			//System.out.println("saleOrder:"+ OnlineListener.purchaseReturnBill);
			//System.out.println("saleOrder:"+billNoPurchaseReturn);
			if(CommonUtil.isNotBlank(billNoConsignment)){
				try {
					PurchaseReturnBill purchaseReturnBill = OnlineListener.purchaseReturnBill.findUniqueByBillNo(billNoPurchaseReturn);
					if(CommonUtil.isNotBlank(consignmentBill)){
						//System.out.println("saleOrder34:"+purchaseReturnBill.getId());
						purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
						OnlineListener.purchaseReturnBill.save(purchaseReturnBill);
					}else{
						//System.out.println("saleOrder12:null");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			/*SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ResourceUtil.getSessionInfoName());
			if (sessionInfo != null) {
				OnlineServiceI onlineService = (OnlineServiceI) ctx.getBean("onlineService");
				onlineService.deleteOnline(sessionInfo.getLoginName(), sessionInfo.getIp());
			}*/

		}

	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
	}

	public void attributeAdded(ServletRequestAttributeEvent evt) {
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
	}

	public void sessionWillPassivate(HttpSessionEvent arg0) {
	}

	public void sessionCreated(HttpSessionEvent arg0) {
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
	}

	public void attributeRemoved(ServletRequestAttributeEvent arg0) {
	}

	public void contextDestroyed(ServletContextEvent evt) {
		logger.debug("服务器关闭");
	}

	public void attributeReplaced(ServletRequestAttributeEvent arg0) {
	}

	public void requestInitialized(ServletRequestEvent arg0) {
	}

	public void setSaleOrderBill(SaleOrderBillService saleOrderBill) {
		OnlineListener.saleOrderBill = saleOrderBill;
	}

	public SaleOrderBillService getSaleOrderBill() {
		return saleOrderBill;
	}

	public void setSaleOrderReturnBill(SaleOrderReturnBillService saleOrderReturnBill) {
		OnlineListener.saleOrderReturnBill = saleOrderReturnBill;
	}

	public SaleOrderReturnBillService getSaleOrderReturnBill() {
		return saleOrderReturnBill;
	}

	public void setConsignmentBill(ConsignmentBillService consignmentBill) {
		OnlineListener.consignmentBill = consignmentBill;
	}

	public ConsignmentBillService getConsignmentBill() {
		return consignmentBill;
	}

	public void setTransferOrderBill(TransferOrderBillService transferOrderBill) {
		OnlineListener.transferOrderBill = transferOrderBill;
	}

	public TransferOrderBillService getTransferOrderBill() {
		return transferOrderBill;
	}

	public void setPurchaseOrderBill(PurchaseOrderBillService purchaseOrderBill) {
		OnlineListener.purchaseOrderBill = purchaseOrderBill;
	}

	public PurchaseOrderBillService getPurchaseOrderBill() {
		return purchaseOrderBill;
	}

	public void setPurchaseReturnBill(PurchaseReturnBillService purchaseReturnBill) {
		OnlineListener.purchaseReturnBill = purchaseReturnBill;
	}

	public PurchaseReturnBillService getPurchaseReturnBill() {
		return purchaseReturnBill;
	}
}
