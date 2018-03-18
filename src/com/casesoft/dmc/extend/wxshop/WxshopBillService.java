package com.casesoft.dmc.extend.wxshop;

import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.product.NoPushProductDao;
import com.casesoft.dmc.dao.product.NoPushStyleDao;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.NoPushProduct;
import com.casesoft.dmc.model.product.NoPushStyle;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/4.
 */
@Service
@Transactional
public class WxshopBillService implements IBillWSService {
    @Autowired
    private NoPushStyleDao noPushStyleDao;
    @Autowired
    private NoPushProductDao noPushProductDao;
    @Override
    public List<Bill> findBills(String[] properties, String[] values) throws Exception {
        return null;
    }

    @Override
    public List<BillDtl> findBillDtls(String[] properties, String[] values) throws Exception {
        return null;
    }

    @Override
    public List<ErpStock> findErpStock(String[] properties, String[] values) throws Exception {
        return null;
    }

    @Override
    public List<Product> findErpBasic(String styleId, String colorId) {
        return null;
    }

    @Override
    public List<BillDtl> findBillDtls(String id) {
        return null;
    }

    @Override
    public String findBillsJSON(String[] properties, String[] values) throws Exception {
        return null;
    }

    @Override
    public String findBillDtlsJSON(String[] properties, String[] values) throws Exception {
        return null;
    }

    @Override
    public String findBillDtlsJSON(String id) throws Exception {
        return null;
    }

    @Override
    public MessageBox uploadToERP(Bill bill) {
        return null;
    }

    @Override
    public MessageBox uploadTaskToErp(Business bus) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String id) {
        return null;
    }

    @Override
    public MessageBox update(Business bus) {
        return null;
    }

    @Override
    public MessageBox uploadPosToERP(SaleBill bill) {
        return null;
    }

    @Override
    public Bill productBill(String[] properties, String[] values) {
        return null;
    }

    @Override
    public String destroyBill(String[] properties, String[] values) {
        return null;
    }

    @Override
    public MessageBox updateUnitInfo(Business bus) {
        return null;
    }

    @Override
    public List<Record> findRecordByTask(String taskId) {
        return null;
    }



}
