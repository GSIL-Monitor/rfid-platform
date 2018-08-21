package com.casesoft.dmc.extend.msg;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.msg.websocket.SessionManager;
import com.casesoft.dmc.extend.msg.websocket.entity.MessageType;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessage;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;

import java.io.*;
import java.util.Date;

/**
 * Created by pc on 2016/4/1.
 */
public class PushUtil {
	/**
	 * 克隆对象
	 * 
	 * @param object
	 * */
	public static Object deepClone(Object object) throws IOException,
            ClassNotFoundException {// 将对象写到流里
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(object);// 从流里读出来
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		return (oi.readObject());
	}

	/**
	 * 发送通知消息
	 */

	public static void sendNoticeMessage(Business bus,
			SessionManager sessionManager) {
		try {
			WsMessage wsMessage = new WsMessage();
			Unit s = CacheManager.getStorageById(bus.getOrigId()); // wing
			String unitCode="";
			bus.setOrigName(s == null ? "" : s.getName());
			bus.setOrigUnitName(CacheManager.getUnitById(bus.getOwnerId())
					.getName());
			if (CommonUtil.isNotBlank(bus.getDestUnitId())) {
				Unit unit2 = CacheManager.getUnitById(bus.getDestUnitId());
				bus.setDestUnitName(unit2 == null ? bus.getDestUnitId() : unit2
						.getName());
			}
			if (CommonUtil.isNotBlank(bus.getDestId())) {
				bus.setDestName(CacheManager.getUnitById(
						bus.getDestId()).getName());
			}
			StringBuffer msg = new StringBuffer();
			StringBuffer basicInfo=new StringBuffer();
			switch (bus.getToken().intValue()) {
			case Constant.Token.Storage_Inventory:
				msg.append("类型:").append("仓库盘点</br>");
				unitCode=bus.getOrigId();
				break;
			case Constant.Token.Storage_Inbound:
				msg.append("类型:").append("仓库入库</br>");
				unitCode=bus.getOrigId();
				basicInfo.append("发货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r");
				break;
			case Constant.Token.Storage_Refund_Outbound:
				msg.append("类型:").append("仓库退货入库</br>");
				unitCode=bus.getDestUnitId();
				basicInfo.append("发货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r");
				break;
			case Constant.Token.Storage_Outbound:
				unitCode=bus.getDestUnitId();
				msg.append("类型:").append("仓库发货出库</br>");
				basicInfo.append("发货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r");
				break;
			case Constant.Token.Storage_Transfer_Outbound:
				unitCode=bus.getDestUnitId();
				msg.append("类型:").append("仓库调拨出库</br>");
				basicInfo.append("发货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r");
				break;
			case Constant.Token.Storage_Refund_Inbound:
				unitCode=bus.getOrigId();
				basicInfo.append("发货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r");
				unitCode=bus.getOrigId();
				msg.append("类型:").append("仓库退货入库</br>");
				break;
			case Constant.Token.Shop_Transfer_Outbound:
				basicInfo.append("发货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r");
				unitCode=bus.getDestUnitId();
				msg.append("类型:").append("仓库调拨出库</br>");
				break;
			case Constant.Token.Shop_Inventory:
				msg.append("类型:").append("门店盘点</br>");
				unitCode=bus.getOrigId();
				break;
			case Constant.Token.Storage_Transfer_Inbound:// 仓库调拨入库
				msg.append("类型:").append("仓库调拨入库</br>");
				unitCode=bus.getOrigId();
				basicInfo.append("发货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r");
                break;
			case Constant.Token.Shop_Inbound:
				msg.append("类型:").append("门店收货入库</br>");
				unitCode=bus.getOrigId();
				basicInfo.append("发货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r");
				break;
			case Constant.Token.Shop_Transfer_Inbound:
				msg.append("类型:").append("门店调拨入库</br>");
				unitCode=bus.getOrigId();
				basicInfo.append("发货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r");
				break;
			case Constant.Token.Shop_Refund_Outbound:
				msg.append("类型:").append("门店退货出库</br>");
				unitCode=bus.getDestUnitId();
				basicInfo.append("发货方：《").append(bus.getOrigId()).append("》")
				.append(bus.getOrigName()).append("</br>\n\r")
				.append("收货方：《").append(bus.getDestUnitId()).append("》")
				.append(bus.getDestUnitName()).append("</br>\n\r");
				break;
			case Constant.Token.Shop_Sales:
				break;
			case Constant.Token.Shop_Sales_refund:
				break;
			default:
				break;
			}
			if (CommonUtil.isNotBlank(bus.getBillNo())) {
				msg.append("单号：").append(bus.getBillNo()).append("</br>\n\r");
				msg.append("实数：\n")
						.append(String.valueOf(bus.getBill().getActQty()))
						.append("单据数量：")
						.append(String.valueOf(bus.getBill().getTotQty()))
						.append("</br>\n");
			}

			msg.append("日期：")
					.append(CommonUtil.getDateString(bus.getEndTime(),
							"yyyy-MM-dd")).append("</br>\n\r").append("任务号：")
					.append(bus.getId()).append("</br>\n\r");
			if (bus.getToken().intValue() == Constant.Token.Shop_Inventory
					|| bus.getToken().intValue() == Constant.Token.Storage_Inventory) {
				msg.append("操作方：《").append(bus.getOrigId()).append("》")
						.append(bus.getOrigName()).append("</br>\n\r");
			} else {
				msg.append(basicInfo.toString());
				/*msg.append("发货方：《").append(bus.getOrigId()).append("》")
						.append(bus.getOrigName()).append("</br>\n\r")
						.append("收货方：《").append(bus.getDestUnitId()).append("》")
						.append(bus.getDestUnitName()).append("</br>\n\r");*/
			}
			msg.append("总数量：").append(String.valueOf(bus.getTotEpc()))
					.append("</br></br>");
			wsMessage.setFromCode(bus.getDeviceId());
			wsMessage.setContent(msg.toString());
/*
			wsMessage.setAcceptType(PushUser.TYPE_TASK);
*/
			wsMessage.setMsgType(MessageType.MSG_PTOP_TYPE);
 			wsMessage.setSendDate(new Date());

			sessionManager.sendMessageUnit(unitCode, wsMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
