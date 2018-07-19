package com.casesoft.dmc.controller.pad;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.exception.RfidReaderException;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.wechat.wxpay.pay.WXPayConfigImpl;
import com.casesoft.dmc.model.logistics.PurchaseBystyleid;
import com.casesoft.dmc.model.pad.MobilePayment;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import com.casesoft.dmc.service.pad.MobilePaymentService;
import com.casesoft.dmc.service.rfidReader.RfidReaderService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.impl.UserService;
import com.casesoft.dmc.service.task.TaskService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * pad sale and returnsale Controller
 * 2018.5.31
 * liutianci
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
    @Autowired
    private MobilePaymentService mobilePaymentService;
    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;
    @Autowired
    private RfidReaderService rfidReaderService;
    private String deviceId = "6252";

    /**
     *无人收银验证用户登录
     * @param code 用户名
     * @param password 密码
     * @return LTC time :2018/5/19
     * @throws Exception
     */
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

    /**
     *获取默认客户的各个属性
     * @param defalutCustomerId 默认客户
     * @return LTC time :2018/5/19
     */
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

    /**
     * 小程序自助收银通过OwnerId获取默认客户
     * add by Anna on 2018-06-06
     */
    @RequestMapping("/customer/findDefaultCustomerIdWS")
    @ResponseBody
    public MessageBox findDefaultCustomerId(String ownerId){
        this.logAllRequestParams();
        Unit unit = CacheManager.getUnitByCode(ownerId);
        String defaultCustomerId = unit.getDefalutCustomerId();
        return new MessageBox(true,"",defaultCustomerId);
    }


    /**
     * 客户查询
     * @param page 分页
     * @return page
     * @throws Exception
     */
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
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     * 扫描检测唯一吗是否可以出/入库(存在库存表中)
     */
    @RequestMapping("/scanning/checkEpcStockWS")
    @ResponseBody
    public MessageBox checkEpcStock(String warehId ,String type) {
        List<String>epcList = new ArrayList<>();
        List<String>codeList = new ArrayList<>();
        try {
            epcList = rfidReaderService.fastSwitchAntInventory(deviceId, (byte) 0x01, (byte) 0x00, (byte) 0x01);
            for (String uniqueCode : epcList){
                String epcCode = uniqueCode.toUpperCase();
                uniqueCode = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
                codeList.add(uniqueCode);
            }
        }catch (RfidReaderException e){
            e.printStackTrace();
        }
        int sumCode = codeList.size();
        List<EpcStock> epcStockList = new ArrayList<>();
        List<EpcStock> successEpcStock = new ArrayList<>();
        List<String> failSqu = new ArrayList<>();
        StringBuffer failCodestr = new StringBuffer();
        if (sumCode!=0){
            try {
                epcStockList = this.epcStockService.findEpcCodes(TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code"));
                for(EpcStock epcStock :epcStockList) {
                    if (type.equals("1")){
                        if (epcStock.getWarehouseId().equals(warehId)&&epcStock.getInStock()!=1){
                            StockUtil.convertEpcStock(epcStock);
                            successEpcStock.add(epcStock);
                        }else {
                            failSqu.add(epcStock.getSku());
                        }
                    }else {
                        if (epcStock.getWarehouseId().equals(warehId)&&epcStock.getInStock()==1){
                            StockUtil.convertEpcStock(epcStock);
                            successEpcStock.add(epcStock);
                        }else {
                            failSqu.add(epcStock.getSku());
                        }
                    }
                }
                if (type.equals("1")){
                    if (failSqu.size()==0) {
                        return new MessageBox(true, "总共扫描 " + sumCode + " 件,0件不能入库", successEpcStock);
                    }else {
                        for (String s :failSqu){
                            s=s+"、";
                            failCodestr.append(s);
                        }
                        int sumSqu = failSqu.size();
                        return new MessageBox(true,"总共扫描 "+sumCode+" 件，其中"+failCodestr+"不能入库,总共："+sumSqu+"件",successEpcStock);
                    }
                }else {
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
        }else {
            return new MessageBox(false,"未扫描到商品！");
        }

    }

    /**
     * 选完支付方式后修改单中的支付方式
     * @param billNo 销售单单号
     * @param payType 支付类型
     * @return 结果
     */
    @RequestMapping(value = {"/padUser/payType","/padUser/payTypeWS"})
    @ResponseBody
    public MessageBox payType(String billNo,String payType){
        try {
            this.saleOrderBillService.update(payType,billNo);
            return new MessageBox(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,"修改失败");
        }
    }

    /**
     *
     * @param request 获取当前ip地址
     * @param payPrice 支付金额
     * @param billNo 销售单单号
     * @return 支付消息
     * @throws Exception
     */
    @RequestMapping(value = {"/padUser/WXcode","/padUser/WXcodeWS"})
    @ResponseBody
    public MessageBox WXpay(HttpServletRequest request, String payPrice, String billNo) throws Exception {
        String codeUrl;
        int price = Integer.parseInt(payPrice)*100;
        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
        WXPay wxPay = new WXPay(config);
        String out_trade_no = billNo;
        String ip = getIpAddr(request);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", "AS-自助买单");  //商品或支付单简要描述
        data.put("out_trade_no", out_trade_no); //商户系统内部的订单号,32个字符内、可包含字母
        data.put("device_info", ""); //微信支付分配的终端设备号，与下单一致
        data.put("fee_type", "CNY"); //设置货币类型，人民币
        data.put("total_fee", /*Integer.toString(price)*/"1"); //订单总金额，单位为分，只能为整数
        data.put("spbill_create_ip", ip); //调用微信支付API的机器IP
        data.put("notify_url", "http://tnsti6.natappfree.cc/csr/pad/padUser/getWxPayNotifyWS.do"); //接收微信支付异步通知回调地址
        data.put("trade_type", "NATIVE");//交易方式，扫码支付
        data.put("product_id", "12"); //设置trade_type=NATIVE时，此参数必传。此id为二维码中包含的商品ID
        // data.put("time_expire", "20170112104120");//订单失效时间
        try {
            //下单
            Map<String, String> r = wxPay.unifiedOrder(data);
            if (r.get("return_code").equals("FAIL")){
                return new MessageBox(false,"下单失败");
            }else {
                codeUrl = r.get("code_url");
                return new MessageBox(true,"下单成功",codeUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false,"下单失败");
        }
    }
    //保存支付成功后的单号
    HashMap<String,Integer>  wxPayType = new HashMap<String, Integer>();

    /**
     *
     * @param req 微信端请求的数据
     * @param resp 返回微信端的结果
     * @throws Exception
     */
    @RequestMapping("/padUser/getWxPayNotifyWS")
    @ResponseBody
    public void getWxPayNotify(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String notifyData = "";
        try {
            InputStream is = req.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((s = in.readLine()) != null){
                stringBuffer.append(s);
            }
            in.close();
            is.close();
            notifyData = stringBuffer.toString();
            WXPayConfigImpl config = new WXPayConfigImpl();
            WXPay wxpay = new WXPay(config);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
            logger.info("微信支付返回的通知为：" + notifyMap);
            System.out.print(notifyMap);
            String resXml = "";
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                wxPayType.put(notifyMap.get("out_trade_no"),1);
                remove(notifyMap.get("out_trade_no"));
                MobilePayment mobilePayment = new MobilePayment();
                mobilePayment.setId(notifyMap.get("transaction_id"));
                mobilePayment.setTradeNo(notifyMap.get("out_trade_no"));
                mobilePayment.setAppid(notifyMap.get("appid"));
                mobilePayment.setOpenid(notifyMap.get("openid"));
                mobilePayment.setFee(notifyMap.get("total_fee"));
                mobilePayment.setPaymentType("微信支付");
                mobilePayment.setTime(notifyMap.get("time_end"));
                this.mobilePaymentService.save(mobilePayment);
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
            else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                logger.error("支付失败");
            }
            resp.setContentType("text/html;charset=UTF-8");
            OutputStream os = resp.getOutputStream();
            os.write(resXml.getBytes());
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.flush();
            oos.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param billNo 当前支付的单号
     * @return liutianci
     */
    @ResponseBody
    @RequestMapping(value = {"/padUser/getPayState","/padUser/getPayStateWS"})
    public MessageBox getPayState(String billNo){
        for (String key : wxPayType.keySet()) {
            if (key.equals(billNo));
            {
                wxPayType.remove(billNo);
                return new MessageBox(true,"支付成功");
            }
        }
        return null;
    }

    /**
     * 关联单号
     * @param billNo 销售单单号
     * @param rbillNo 退货单单号
     * @return 保存结果
     */
    @ResponseBody
    @RequestMapping(value = {"/padUser/associated","/padUser/associatedWS"})
    public MessageBox associated(String billNo,String rbillNo){
        try {
            this.saleOrderBillService.updateNo(billNo,rbillNo);
            this.saleOrderReturnBillService.updateNo(billNo,rbillNo);
            return new MessageBox(true,"销售退货单号关联成功");
        }catch (Exception e){
            this.logger.error(e.getMessage());
            return new MessageBox(false,"销售退货单号关联失败");
        }
    }

    /**
     * 移除支付成功后未出库成功的消息
     * @param billNo 销售单号
     * @throws Exception
     */
    public void remove(String billNo) throws Exception {
        Timer timer= new Timer();
        TimerTask task  = new TimerTask(){    //创建一个新的计时器任务。
            @Override
            public void run() {
                synchronized(this){
                    wxPayType.remove(billNo);
                }
            }
        };
        //超时设定
        int overTime =Integer.parseInt(PropertyUtil.getValue("overTime"));
        timer.schedule(task, overTime);
    }

    /**
     * 4天线功率设置
     * @param deviceId 设备号
     * @param outputPower 功率
     * @return MessageBox
     */
    @ResponseBody
    @RequestMapping("/padUser/setPower")
    public MessageBox setPower(String deviceId, byte outputPower){
        try {
            this.rfidReaderService.setOutPutPower(deviceId,outputPower);
            return new MessageBox(true,"设置成功");
        } catch (RfidReaderException e) {
            e.printStackTrace();
            return new MessageBox(false,"设置失败");
        }
    }

    /**
     * 获取当前系统的ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 有的手机获取到的ip会有多个，如117.136.74.180, 101.226.125.109，只要取一个即可，否则微信支付会报错
        String[] ips = ip.split(",");
        ip = ips[0];
        return ip;
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
