package com.casesoft.dmc.extend.api.web.epay.fepay.base;

import com.casesoft.dmc.core.vo.MessageBox;

/**
 * 目标接口
 * */
public interface IEPayTarget {

	public MessageBox barPay();// 条码支付

	public MessageBox qrPay();// 二维码支付

	public MessageBox query();// 查询

	public MessageBox cancelOrder() ;// 取消订货单

	public MessageBox refundOrder();// 退订单
}
