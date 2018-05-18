package com.casesoft.dmc.controller.pad;
import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.impl.UserService;
import com.casesoft.dmc.service.task.TaskService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

/**
 * 普通用户 user.type=0
 */

@Controller
@RequestMapping("/pad")
public class PadUserController extends BaseController implements IBaseInfoController<User>{
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GuestViewService guestViewService;

    @RequestMapping("/padUser/loginWS")
    @ResponseBody
    public MessageBox loginWS(String code, String password) throws Exception {
        this.logAllRequestParams();
        this.getRequest().setCharacterEncoding("utf-8");
        this.getResponse().setContentType("text/html;charset=utf-8");
        User user;
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(code, password);
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            user = this.userService.getUser(code);
            if(currentUser.isAuthenticated()){
                //response.sendRedirect(request.getContextPath()+"/views/pad/padUser.html");
                System.out.println("客户端用户[" + code + "]登录认证通过");
            }else{
                token.clear();
            }
            return new MessageBox(true,"登录成功",user);
        } catch (Exception e){
            this.logger.error(e.getMessage());
            //response.sendRedirect(request.getContextPath()+"/padLogin.html");
            return new MessageBox(false,"请核对用户名密码");
        }
    }

    @RequestMapping("/customer/addWS")
    @ResponseBody
    public MessageBox addWS(String defalutCustomerId){
        this.logAllRequestParams();
        try {
            GuestView guestView = this.guestViewService.load(defalutCustomerId);
            return new MessageBox(true,"",guestView);
        }catch (Exception e){
            return new MessageBox(false,"");
        }
    }
    @RequestMapping("customer/pageWS")
    @ResponseBody
    public Page<GuestView> findPageView(Page<GuestView> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        String ownerId = getCurrentUser().getOwnerId();
        String roleId = getCurrentUser().getRoleId();
        if (roleId.equals("JMSJS")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page = this.guestViewService.findPage(page, filters);
        if (page.getRows().size() > 0) {
            for (GuestView g : page.getRows()) {
                if (CommonUtil.isNotBlank(CacheManager.getUserById(g.getUpdaterId()))) {
                    g.setUpdaterName(CacheManager.getUserById(g.getUpdaterId()).getName());
                }
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(g.getOwnerId()))) {
                    g.setUnitName(CacheManager.getUnitById(g.getOwnerId()).getName());
                }
            }
        }
        return page;
    }


    /**
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @param type    出入库类型 0出库，1入库
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     * 扫描检测唯一吗是否可以出/入库(存在库存表中)
     */
    @RequestMapping("/scanning/checkEpcStockWS")
    @ResponseBody
    public MessageBox checkEpcStock(String warehId, Integer type) {
        List<String>codeList = new ArrayList<>();
        codeList.add("0081800000124");
        int sumCode = codeList.size();
        List<EpcStock> epcStockList = new ArrayList<>();
        List<EpcStock> successEpcStock = new ArrayList<>();
        List<String> failSqu = new ArrayList<>();
        StringBuffer failCodestr = new StringBuffer();
        try {
            epcStockList = this.epcStockService.findEpcCodes(TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code"));
            for(EpcStock epcStock :epcStockList) {
                if (warehId.equals("")){
                    StockUtil.convertEpcStock(epcStock);
                    successEpcStock.add(epcStock);
                }else {
                    if (epcStock.getWarehouseId().equals(warehId)&&epcStock.getInStock()==1){
                        StockUtil.convertEpcStock(epcStock);
                        successEpcStock.add(epcStock);
                    }else {
                        failSqu.add(epcStock.getSku());
                    }
                }
            }
            if (warehId.equals("")){
                return new MessageBox(true, "总共扫描 " + sumCode + " 件,0件不能入库", successEpcStock);
            }
            else {
                if (failSqu.size()==0) {
                    return new MessageBox(true, "总共扫描 " + sumCode + " 件,0件不能出库", successEpcStock);
                }else {
                    for (String s :failSqu){
                        s=s+"、";
                        failCodestr.append(s);
                    }
                    int sumSqu = failSqu.size();
                    return new MessageBox(true,"总共扫描 "+sumCode+" 件，其中"+failCodestr+"不能出库,总共："+sumSqu+"件",successEpcStock);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public MessageBox save(String saleOrderBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            SaleOrderBill saleOrderBill = JSON.parseObject(saleOrderBillStr, SaleOrderBill.class);
            if(CommonUtil.isNotBlank(saleOrderBill.getBillNo())){
                Integer status = this.saleOrderBillService.findBillStatus(saleOrderBill.getBillNo());
                if(status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")){
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }
            }
            saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
            List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            if (CommonUtil.isBlank(saleOrderBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.saleOrder
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                saleOrderBill.setId(prefix);
                saleOrderBill.setBillNo(prefix);
            }
            saleOrderBill.setId(saleOrderBill.getBillNo());
            BillConvertUtil.covertToSaleOrderBill(saleOrderBill, saleOrderBillDtlList, curUser);
            this.saleOrderBillService.save(saleOrderBill, saleOrderBillDtlList);
            return new MessageBox(true, "保存成功", saleOrderBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/findBillDtl")
    @ResponseBody
    public List<SaleOrderBillDtl> findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<SaleOrderBillDtl> saleOrderBillDtls = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderBillService.getBillRecod(billNo);
        Map<String, String> codeMap = new HashMap<>();
        for (BillRecord r : billRecordList) {
            if (codeMap.containsKey(r.getSku())) {
                String code = codeMap.get(r.getSku());
                code += "," + r.getCode();
                codeMap.put(r.getSku(), code);
            } else {
                codeMap.put(r.getSku(), r.getCode());
            }
        }
        for (int i = 0; i < saleOrderBillDtls.size(); i++) {
            SaleOrderBillDtl dtl = saleOrderBillDtls.get(i);
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            dtl.setTagPrice(CacheManager.getStyleById(dtl.getStyleId()).getPrice());
            if (codeMap.containsKey(dtl.getSku())) {
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return saleOrderBillDtls;
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    public MessageBox cancel(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        this.saleOrderBillService.cancelUpdate(saleOrderBill);
        return new MessageBox(true, "撤销成功");
    }

    @Override
    public Page<User> findPage(Page<User> page) throws Exception {
        return null;
    }

    @Override
    public List<User> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(User entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }
}
