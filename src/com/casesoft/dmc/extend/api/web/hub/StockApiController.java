package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.stock.Inventory;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.search.DetailStockViewService;
import com.casesoft.dmc.service.stock.EpcStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/stock", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "仓库库存、门店库存、ERP库存获取数据接口")
public class StockApiController extends ApiBaseController {

    @Autowired
    private EpcStockService epcStockService;

    @Autowired
    private DetailStockViewService detailStockViewService;

    /* 提供给设备统计并查询实时库存
     * @return
     */
    @RequestMapping(value = "/countAndFindStockWS.do")
    @ResponseBody
    public SinglePage<Inventory> countAndFindStockWS(String filter_EQS_deviceId, String filter_LIKES_sku, String ownerId) {
        this.logAllRequestParams();
        try {

            String deviceId = this.getReqParam("filter_EQS_deviceId");
            Device device = CacheManager.getDeviceByCode(deviceId);
            Unit storage = CacheManager.getUnitById(device.getStorageId());
            String warehouse1Id = storage.getId();

            String sku = this.getReqParam("filter_LIKES_sku");
            String styleId = null;
            List<BillDtl> list = this.epcStockService.findEpcStockSKUByWharehousId(
                    warehouse1Id, sku, ownerId, styleId);
            SinglePage<Inventory> sp = StockUtil.billConvertToPageVo(list);
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/pageWS.do")
    @ResponseBody
    public MessageBox findPage(String pageSize, String pageNo, String sortIds, String orders, String userId) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        Page<DetailStockView> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if (CommonUtil.isNotBlank(sortIds)) {
            if (sortIds.split(",").length != orders.split(",").length) {
                return new MessageBox(false, "排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        User user = CacheManager.getUserById(userId);
        page = this.detailStockViewService.findPage(page, filters, user.getOwnerId());
        return new MessageBox(true, "ok", page.getRows());

    }

    @Override
    public String index() {
        return null;
    }


    @RequestMapping(value = "/sumInfoOfStockWS.do")
    @ResponseBody
    public MessageBox sumInfoOfStock(String sku, String userId) throws Exception {
        String warehId = null;
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        for (PropertyFilter filter:filters) {
            if(filter.getPropertyName()=="warehId");
            warehId = filter.getMatchValue().toString();
        }
        User user = CacheManager.getUserById(userId);
        if(CommonUtil.isBlank(sku)){
            sku="";
        }
        Map<String, String> sumInfo = this.detailStockViewService.stockSumInfoBySku(sku, user.getOwnerId(), warehId);
        return new MessageBox(true, "ok", sumInfo);
    }
}
