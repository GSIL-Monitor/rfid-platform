package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.sys.UserUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.location.Area;
import com.casesoft.dmc.model.location.City;
import com.casesoft.dmc.model.location.Province;
import com.casesoft.dmc.model.logistics.AccountStatementView;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.PaymentGatheringBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.logistics.AccountStatementViewService;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
import com.casesoft.dmc.service.logistics.PaymentGatheringBillService;
import com.casesoft.dmc.service.logistics.StaticsInandOutService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.sys.GuestService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping(value = "/api/hub/base", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "基础信息(组织、设备、用户)模块接口")

//passed with no error
public class BaseInfoApiController extends ApiBaseController {

    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private GuestViewService guestViewService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private StaticsInandOutService staticsInandOutService;
    @Autowired
    private AccountStatementViewService accountStatementViewService;
    @Autowired
    private PaymentGatheringBillService paymentGatheringBillService;

    @Autowired
    private MonthAccountStatementService monthAccountStatementService;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/listUnitWS.do")
    @ResponseBody
    public List<Unit> listUnitWS(String deviceId) {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(getRequest());
        List<Unit> unitList = this.unitService.find(filters);
        UserUtil.convertToUnitVo(unitList);
        return unitList;
    }

    @RequestMapping(value = "/findUnitByDeviceWS.do")
    @ResponseBody
    public Unit findUnitByDeviceWS(@ApiParam @RequestParam String deviceId) {
    	Assert.notNull(deviceId,"deviceId不能为空！");
        Unit u = CacheManager.getUnitById(CacheManager.getDeviceByCode(deviceId).getStorageId());
        u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());
        return u;
    }

    @RequestMapping(value = "/findGuestsWS.do")
    @ResponseBody
    public MessageBox findGuestWS(String pageSize,String pageNo,String sortIds,String  orders,String userId) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        if(CommonUtil.isNotBlank(userId)){
            User user = CacheManager.getUserById(userId);
            if(CommonUtil.isNotBlank(user)){
                String roleId = user.getRoleId();
                String userOwnerId = user.getOwnerId();
                if(roleId.equals("JMSJS")){
                    PropertyFilter filter = new PropertyFilter("EQS_ownerId", userOwnerId);
                    filters.add(filter);
                }
            }

        }
        PropertyFilter filter = new PropertyFilter("EQI_status", "1");
        filters.add(filter);
        Page<GuestView> page = new Page<>(Integer.parseInt(pageSize));
        List<GuestView> guestViewList = new ArrayList<>();
        if(Integer.parseInt(pageSize)>0) {
            page.setPage(Integer.parseInt(pageNo));
            if (CommonUtil.isNotBlank(sortIds)) {
                if (sortIds.split(",").length != orders.split(",").length) {
                    return new MessageBox(false, "排序字段与排序方向的个数不相等");
                }
                page.setOrderBy(sortIds);
                page.setOrder(orders);
            }
            page.setPageProperty();
            page = this.guestViewService.findPage(page, filters);
            guestViewList = page.getRows();
        }else{
            //不分页调用
            guestViewList = this.guestViewService.find(filters);
        }
        for(GuestView guest : guestViewList){
            Unit defaultWareh = CacheManager.getUnitByCode(guest.getDefaultWarehId());
            if(CommonUtil.isNotBlank(defaultWareh)){
                guest.setDefaultWarehouseName(defaultWareh.getName());
            }
        }
        return this.returnSuccessInfo("获取成功",guestViewList);
    }
    @RequestMapping(value = "/findBusinessWS.do")
    @ResponseBody
    public MessageBox findBusinessWS(String pageSize,String pageNo) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<User> page = new Page<>(Integer.parseInt(pageSize));

        page.setPage(Integer.parseInt(pageNo));

        page = this.userService.findPage(page,filters);

        return this.returnSuccessInfo("获取成功",page.getRows());
    }


    @RequestMapping(value = "/findCustomerWS.do")
    @ResponseBody
    public MessageBox findCustomerWS(){
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Customer> customerList =this.customerService.find(filters);
        return returnSuccessInfo("ok",customerList);
    }


    @RequestMapping(value = "/listUserWS.do")
    @ResponseBody
    @Deprecated
    public List<User> listUserWS(String filter_EQI_isAdmin, String filter_EQS_ownerId, String filter_EQS_code,String userId) {
        this.logAllRequestParams();


        String isAdmin = filter_EQI_isAdmin;
        String ownerId = filter_EQS_ownerId;
        String code = filter_EQS_code;

        List<PropertyFilter> filters = new ArrayList<>();

       if(CommonUtil.isNotBlank(userId)){
           User user = CacheManager.getUserById(userId);
           if(CommonUtil.isNotBlank(user)){
               String roleId = user.getRoleId();
               ownerId = user.getOwnerId();
               if(roleId.equals("JMSJS")){
                   PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
                   filters.add(filter);
               }
           }

       }
        if (CommonUtil.isNotBlank(isAdmin) && isAdmin.equals("4")) {//营业员
            PropertyFilter filter = new PropertyFilter("EQI_type", "" + 4);
            filters.add(filter);
        } else {
            PropertyFilter filter = new PropertyFilter("EQI_type", "" + 0);
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(ownerId)) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(code)) {
            PropertyFilter filter = new PropertyFilter("EQS_code", code);
            filters.add(filter);
        }

        List<User> lists = this.userService.find(filters);
        return lists;
    }

    @RequestMapping(value = "/findUserWS.do")
    @ResponseBody
    @Deprecated
    public MessageBox findUserWS(String filter_EQI_isAdmin, String filter_EQS_ownerId, String filter_EQS_code,String userId) {
        this.logAllRequestParams();
        try{
            List<User> lists = listUserWS(filter_EQI_isAdmin,filter_EQS_ownerId,filter_EQS_code,userId);
            return new MessageBox(true,"ok",lists);
        }catch (Exception e){
            return new MessageBox(false,e.getMessage());
        }


    }

    @RequestMapping(value = "/loginWS.do")
    @ResponseBody
    public MessageBox login(String userCode, String password, String deviceId) throws Exception {
    	
    	Assert.notNull(userCode!=null||password!=null,"未输入用户名或密码");
        User user;
    	try{
           user  = this.userService.getUser(userCode, password);
    	}catch (Exception e){
           return new MessageBox(false,e.getMessage());
        }
        Unit unit = CacheManager.getUnitByCode(user.getOwnerId());//
        return new MessageBox(true,"ok",unit);
    }

    @RequestMapping("findMenuWS")
    @ResponseBody
    public MessageBox findMenu(String userId){
        try{
            User user = CacheManager.getUserById(userId);
            List<Resource> resourceList  = this.resourceService.getSelectedResourceList();
            List<Resource> resources = new ArrayList<>();
            List<RoleRes> rrList = CacheManager.getAuthByRoleId(user.getRoleId());
            for(RoleRes rr : rrList) {
                for(Resource res : resourceList) {
                    if(rr.getResId().equals(res.getCode())) {
                        if(CommonUtil.isNotBlank(res.getClientCode())){
                            resources.add(res);
                        }
                        break;
                    }
                }
            }
            resources.add(new Resource("","","TaskActivity","任务"));
            resources.add(new Resource("","","BasicInfoActivity","基础数据"));
            resources.add(new Resource("","","UpdateActivity","更新"));
            resources.add(new Resource("","","SettingsActivity","设置"));
            return new MessageBox(true,"ok",resources);
        }catch (Exception e){
            return new MessageBox(false,e.getMessage());
        }
    }
    @RequestMapping(value="/searchByTypeWS")
    @ResponseBody
    public MessageBox searchByType(String type) throws Exception {
        this.logAllRequestParams();
        List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType(type);
        return new MessageBox(true,"ok",pkList);
    }

    @RequestMapping(value = "/saveGuestWS.do")
    @ResponseBody
    public MessageBox saveGuestWS() throws Exception {
        String code =this.getReqParam("code");
        String name =this.getReqParam("name");
        String unitType =this.getReqParam("unitType");
        String linkman =this.getReqParam("linkman");
        String tel = this.getReqParam("tel");
        Integer sex =Integer.parseInt(this.getReqParam("sex"));
        Integer status =Integer.parseInt(this.getReqParam("status"));
        String userId =this.getReqParam("userId");
        Integer discount =Integer.parseInt(this.getReqParam("discount"));
//        String storeDate =this.getReqParam("storeDate");
        String remark =this.getReqParam("remark");

        Integer type;
        if("CT-AT".equals(unitType))
            type=2;
        else if("CT-ST".equals(unitType))
            type=4;
        else if("CT-LS".equals(unitType))
            type=6;
        else
            return returnFailInfo("客户类型不能为空");
        if(!"CT-LS".equals(unitType)) {
            Unit guest = this.guestService.findById(code);
            String prefix = "";
            if (CommonUtil.isBlank(guest)) {
                guest = new Unit();
                if (Constant.UnitType.Agent == guest.getType())
                    prefix = Constant.UnitCodePrefix.Agent;
                else
                    prefix = Constant.UnitCodePrefix.Shop;
                String pcode = this.guestService.findMaxNo(prefix);
                guest.setId(pcode);
                guest.setCode(pcode);
                guest.setCreateTime(new Date());
                guest.setCreatorId(userId);
            }
            guest.setName(name);
            guest.setStatus(status);
            guest.setSex(sex);
            guest.setType(type);
            guest.setLinkman(linkman);
            guest.setTel(tel);
            if(CommonUtil.isBlank(CacheManager.getUserById(userId)))
                return returnFailInfo("该用户不存在");
            guest.setOwnerId(CacheManager.getUserById(userId).getOwnerId());
            guest.setDiscount(discount);
            guest.setRemark(remark);
            guest.setUpdaterId(userId);
            guest.setUpdateTime(new Date());
//            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
//            if(CommonUtil.isNotBlank(storeDate))
//                guest.setStoreDate(simpleDateFormat.parse(storeDate));
            try {
//                this.guestService.save(guest);
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        } else{
            Customer guest =this.customerService.getById(code);
            if(CommonUtil.isBlank(guest)) {
                guest =new Customer();
                String maxNo = this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
                guest.setCode(maxNo);
                guest.setId(maxNo);
                guest.setCreatorId(userId);
                guest.setCreateTime(new Date());
            }
            guest.setName(name);
            guest.setStatus(status);
            guest.setSex(sex);
            guest.setType(type);
            guest.setLinkman(linkman);
            guest.setTel(tel);
            if(CommonUtil.isBlank(CacheManager.getUserById(userId)))
                return returnFailInfo("该用户不存在");
            guest.setOwnerId(CacheManager.getUserById(userId).getOwnerId());
            guest.setDiscount(discount);
            guest.setRemark(remark);
            guest.setUpdaterId(userId);
            guest.setUpdateTime(new Date());
//            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
//            guest.setStoreDate(simpleDateFormat.parse(storeDate));
            try {
//                this.customerService.save(guest);
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        }

    }
    /*
     *name:供应商名称
     * phone:供应商电话
     */
    @RequestMapping(value="/findOwingValueAndStoredValueWs")
    @ResponseBody
    public MessageBox findPage(String nameOrTel) throws Exception {
        if(CommonUtil.isBlank(nameOrTel)){
            nameOrTel="";
        }


        Map<String, String> map = this.unitService.findunitsumbynameandphone(nameOrTel);
        return new MessageBox(true,"ok",map);
    }

    /*
     *pageSize:当前页数
     * pageNo:一次加载多少条
     * sortIds:排序字段 多个,分割
     * orders:排序顺序 多个,分割
     */
    @RequestMapping(value="/findUnitServiceWS")
    @ResponseBody
    public MessageBox findUnitService(String pageSize,String pageNo,String sortIds,String  orders,String userId){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Unit> unitList = new ArrayList<>();
        if(Integer.parseInt(pageSize) > 0) {
            Page<Unit> page = new Page<>(Integer.parseInt(pageSize));
            page.setPage(Integer.parseInt(pageNo));
            if (CommonUtil.isNotBlank(sortIds)) {
                if (sortIds.split(",").length != orders.split(",").length) {
                    return new MessageBox(false, "排序字段与排序方向的个数不相等");
                }
                page.setOrderBy(sortIds);
                page.setOrder(orders);
            }
            page.setPageProperty();
            page = this.unitService.findPage(page, filters);
            unitList = page.getRows();
        }else{
            //不分页调用
            unitList = this.unitService.find(filters);
        }
        return new MessageBox(true,"ok",unitList);
    }


    @RequestMapping(value="/savePaymentGatheringBillWs")
    @ResponseBody
    public MessageBox savePaymentGatheringBill( PaymentGatheringBill paymentGatheringBill){

        //PaymentGatheringBill paymentGatheringBill = JSON.parseObject(transferOrderBillStr,PaymentGatheringBill.class);
        String prefix = BillConstant.PayBill.ReceiptBill
                + CommonUtil.getDateString(new Date(), "yyMMdd");
        String maxNo = this.paymentGatheringBillService.getMaxNo(prefix);
        paymentGatheringBill.setId(maxNo);
        paymentGatheringBill.setBillNo(maxNo);
        paymentGatheringBill.setBillDate(new Date());
        //User currentUser = this.getCurrentUser();
        User currentUser = CacheManager.getUserById(paymentGatheringBill.getOprId());
        if(CommonUtil.isBlank(currentUser)){
            return new MessageBox(false,"保存失败", "没有对应的登录用户信息");
        }else{
            paymentGatheringBill.setOwnerId(currentUser.getOwnerId());
            //paymentGatheringBill.setOprId(currentUser.getCode());
            paymentGatheringBill.setBillType("2");
            this.paymentGatheringBillService.save(paymentGatheringBill);
            return new MessageBox(true,"保存成功", paymentGatheringBill.getBillNo());
        }

    }



    /**
     * 根据客户类型，客户名或客户电话取出 总欠款客户数，总欠款金额，总储值客户数，总储值金额
     * @author yushen
     * @param unitType 客户类型
     * @param nameOrTel 客户名或客户电话
     */
    @RequestMapping(value = "/sumInfoGuestAccountWS.do")
    @ResponseBody
    public MessageBox sumInfoGuestAccount(String unitType,String nameOrTel) throws Exception {
        Map<String,String> numInfo = this.guestViewService.countInfoByCondition(unitType, nameOrTel);
        return new MessageBox(true,"ok",numInfo);
    }


    /**
     * 获取客户的对账单
     * @author yushen
     * @param pageSize 分页大小
     * @param pageNo 当前页码
     * @param sortIds 排序字段，多个属性用 , 分割
     * @param orders 排列顺序，多个属性用 , 分割
     *
     */
    @RequestMapping(value = "/findAllGuestAccountWS.do")
    @ResponseBody
    public MessageBox findAllGuestAccount(String pageSize,String pageNo,String sortIds,String orders){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        Page<GuestView> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if(CommonUtil.isNotBlank(sortIds)){
            if(sortIds.split(",").length != orders.split(",").length){
                return new MessageBox(false,"排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();
        page = this.guestViewService.findPage(page, filters);
        return new MessageBox(true,"ok",page.getRows());
    }


    /**
     * 获取客户的对账流水详表
     * @author yushen
     * @param pageSize 分页大小
     * @param pageNo 当前页码
     * @param sortIds 排序字段，多个属性用 , 分割
     * @param orders 排列顺序，多个属性用 , 分割
     *
     */
    @RequestMapping(value = "/findGuestAccountStatementWS.do")
    @ResponseBody
    public MessageBox findGuestAccountStatement(String pageSize,String pageNo,String sortIds,String orders) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<AccountStatementView> page = new Page<>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageNo));
        if(CommonUtil.isNotBlank(sortIds)){
            if(sortIds.split(",").length != orders.split(",").length){
                return new MessageBox(false,"排序字段与排序方向的个数不相等");
            }
            page.setOrderBy(sortIds);
            page.setOrder(orders);
        }
        page.setPageProperty();

        page = this.accountStatementViewService.findPage(page, filters);
        Date endDate= CommonUtil.converStrToDate(this.getReqParam("filter_GED_billDate"),"yyyy-MM-dd");
        Date startDate= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate,"yyyy-MM-01"),"yyyy-MM-dd");
        Map<String,MonthAccountStatement> monthAccountStatementMap = new HashMap<>();
        if(CommonUtil.isNotBlank(endDate)){


            List<MonthAccountStatement> monthAccountStatementList= getInitialBills(endDate);

            for(MonthAccountStatement m :monthAccountStatementList){
                monthAccountStatementMap.put(m.getUnitId(),m);
            }
            //月结期初到查询起始时间的对账流水单
            List<AccountStatementView> accountStatementViews = this.accountStatementViewService.findStatementsInTime(startDate, endDate);

            //
            for(AccountStatementView statement :accountStatementViews){
                MonthAccountStatement tempM = monthAccountStatementMap.get(statement.getUnitId());
                if(CommonUtil.isNotBlank(tempM)){
                    Double initialVal = tempM.getTotVal();
                    initialVal += statement.getDiffPrice();
                    tempM.setTotVal(initialVal);
                    monthAccountStatementMap.put(statement.getUnitId(),tempM);
                }else {
                    tempM = new MonthAccountStatement();
                    Double initialVal;
                    if(CommonUtil.isNotBlank(tempM.getTotVal())){
                        initialVal = tempM.getTotVal();
                    }else {
                        initialVal = 0D;
                    }
                    initialVal += statement.getDiffPrice();
                    tempM.setTotVal(initialVal);
                    monthAccountStatementMap.put(statement.getUnitId(),tempM);
                }
            }
        }

        Map<String,Double> totOwingMap = new HashMap<>();//存客户或供应商累计欠款金额
        for(AccountStatementView a: page.getRows()){
            //取出期初欠款金额
            Double initialOwingVal;
            if(CommonUtil.isBlank(monthAccountStatementMap.get(a.getUnitId()))){
                initialOwingVal=0D;
            }else {
                initialOwingVal = monthAccountStatementMap.get(a.getUnitId()).getTotVal();
            }
            if(totOwingMap.containsKey(a.getUnitId())){
                Double totOwingVal = totOwingMap.get(a.getUnitId());
                totOwingVal += a.getDiffPrice();
                totOwingMap.put(a.getUnitId(),Math.round(totOwingVal*100)*0.01D);
            }else{
                Double totOwingVal = initialOwingVal+a.getDiffPrice();
                totOwingMap.put(a.getUnitId(),Math.round(totOwingVal*100)*0.01D);
            }
            String groupId;
            if("0".equals(a.getUnitType())){
                groupId = "欠供应商"+a.getUnitId()+" 金额："+initialOwingVal;
            }else{
                groupId = "客户"+a.getUnitId()+" 欠款："+initialOwingVal;
            }
            a.setGroupId(groupId);
            a.setTotalOwingVal(totOwingMap.get(a.getUnitId()));
        }

        return this.returnSuccessInfo("获取成功",page.getRows());
    }
    public  List<MonthAccountStatement> getInitialBills(Date date){
        //根据日期取出月份
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date lastMonthDate = cal.getTime();
        String month = CommonUtil.getDateString(lastMonthDate,"yyyy-MM");
        System.out.println("--月份为："+month);
        //取出对应月份和unitId下的月结账单
        List<MonthAccountStatement> monthAccountStatementList = this.monthAccountStatementService.getStatement(month);
        return monthAccountStatementList;
    }


    /**
     * 根据客户类型，客户名或客户电话取出 总欠款客户数，总欠款金额，总储值客户数，总储值金额
     * @author yushen
     * @param paymentGatheringBill paymentGatheringBill对象，要求传递customsId,payPrice,oprId三个参数
     *
     */
    @RequestMapping(value = "/gatheringSaveWS.do")
    @ResponseBody
    public MessageBox gatheringSave(PaymentGatheringBill paymentGatheringBill){
        this.logAllRequestParams();

        String prefix = BillConstant.PayBill.ReceiptBill
                + CommonUtil.getDateString(new Date(), "yyMMdd");
        String maxNo = this.paymentGatheringBillService.getMaxNo(prefix);
        paymentGatheringBill.setId(maxNo);
        paymentGatheringBill.setBillNo(maxNo);
        paymentGatheringBill.setBillDate(new Date());
        User currentUser = CacheManager.getUserById(paymentGatheringBill.getOprId());
        paymentGatheringBill.setOwnerId(currentUser.getOwnerId());
        paymentGatheringBill.setBillType("0");
        try {
            this.paymentGatheringBillService.saveGuest(paymentGatheringBill);
            return returnSuccessInfo("收款成功");
        }catch (Exception e){
            return returnFailInfo("收款失败");
        }
    }
    /**
     * 根据用户查默认仓库
     * @author chenzhifan
     * @param userId 用户id
     *
     */
    @RequestMapping(value = "/getDefaultwarehouseWs.do")
    @ResponseBody
    public MessageBox getDefaultwarehouse(String userId) {

        try {
            User user = this.userService.getUser(userId);
            if (CommonUtil.isBlank(user)) {
                return returnFailInfo("查询用户失败");
            } else {
                Unit u = this.unitService.getunitbyId(user.getOwnerId());
                Unit defaultWareh = this.unitService.getunitbyId(u.getDefaultWarehId());
                if(CommonUtil.isNotBlank(defaultWareh)){
                    u.setDefaultWarehouseName(defaultWareh.getName());
                }
                User userDefaultSale = this.userService.getUser(u.getDefaultSaleStaffId());
                if(CommonUtil.isNotBlank(userDefaultSale)){
                    u.setDefaultSaleStaffName(userDefaultSale.getName());
                }
                return new MessageBox(true, "获取成功", u);
            }
        } catch (Exception e) {
            return returnFailInfo("获取失败");
        }
    }
    @RequestMapping(value = "/saveVendorNWarehouseWS")
    @ResponseBody
    public MessageBox saveWVUnit(String unitStr,String curUser){
        this.logAllRequestParams();
        Unit u =JSON.parseObject(unitStr,Unit.class);

        Integer type =u.getType();
        if(type!=0&&type!=9)
            return returnFailInfo("只允许保存供应商或仓库！");
        Unit unit =this.unitService.findUnitByCode(u.getCode(),type);
        if(CommonUtil.isBlank(u.getCode())){
            unit =new Unit();
            String code="";
            if(type==0) {
                code = this.unitService.findMaxCode(Constant.UnitType.Vender);

            } else {
                code = this.unitService.findMaxCode(Constant.UnitType.Warehouse);

            }
            unit.setSrc("01");
            unit.setLocked(0);
            unit.setCode(code);
            unit.setId(code);
            unit.setCreateTime(new Date());
            if(CommonUtil.isNotBlank(CacheManager.getUserById(curUser))){
                unit.setCreatorId(curUser);
                unit.setOwnerId(CacheManager.getUserById(curUser).getOwnerId());
            }else{
                return returnFailInfo("用户不存在!");
            }

        }
        unit.setGroupId(u.getGroupId());
        unit.setType(type);
        unit.setName(u.getName());
        unit.setGroupId(u.getGroupId());
        unit.setTel(u.getTel());
        unit.setLinkman(u.getLinkman());
        unit.setEmail(u.getEmail());
        unit.setProvinceId(u.getProvinceId());
        unit.setCityId(u.getCityId());
        unit.setAddress(u.getAddress());
        unit.setRemark(u.getRemark());
        this.unitService.save(unit);

        return returnSuccessInfo("保存成功");
    }

    @RequestMapping(value = "/getProvinceInfoWS")
    @ResponseBody
    public MessageBox getProvinceInfo(){
        try {
            List<Province> provinceList = this.unitService.findAllProvince();
            return new MessageBox(true,"ok",provinceList);
        }catch (Exception e){
            return  new MessageBox(false,e.getMessage());
        }
    }

    @RequestMapping(value = "/getCityInfoWS")
    @ResponseBody
    public MessageBox getCityInfo(String provinceId){
        try {
            List<City> cityList = this.unitService.findCityInfo(provinceId);
            return new MessageBox(true,"ok",cityList);
        }catch (Exception e){
            return  new MessageBox(false,e.getMessage());
        }
    }

    @RequestMapping(value = "/getAreaInfoWS")
    @ResponseBody
    public MessageBox etAreaInfo(String cityId){
        try {
            List<Area> areaList = this.unitService.findAreaInfo(cityId);
            return new MessageBox(true,"ok",areaList);
        }catch (Exception e){
            return  new MessageBox(false,e.getMessage());
        }
    }

}
