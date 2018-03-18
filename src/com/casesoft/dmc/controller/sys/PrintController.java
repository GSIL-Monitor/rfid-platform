package com.casesoft.dmc.controller.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Print;
import com.casesoft.dmc.model.sys.PrintCont;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.PrintService;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/20.
 */
@Controller
@RequestMapping(value = "/sys/print")
public class PrintController extends BaseController implements IBaseInfoController<Print> {
    @Autowired
    private PrintService printService;

    @Autowired
    private SaleOrderBillService saleOrderBillService;

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;

    @Autowired
    private PurchaseReturnBillService purchaseReturnBillService;

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private UserService userService;

    @Autowired
    private GuestViewService guestViewService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/sys/print";
    }

    @Override
    public Page<Print> findPage(Page<Print> page) throws Exception {
        return null;
    }

    @Override
    public List<Print> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(Print entity) throws Exception {
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
    @RequestMapping(value="/saveprint")
    @ResponseBody
   public MessageBox savePrint(String printList){
        Print print = JSON.parseObject(printList, Print.class);
        this.printService.save(print);
        return new MessageBox(true, "保存成功");
    }
    @RequestMapping(value="/updateprint")
    @ResponseBody
    public MessageBox updatePrint(String printList){
        Print print = JSON.parseObject(printList, Print.class);
        this.printService.update(print);
        return new MessageBox(true, "保存成功");
    }
    @RequestMapping(value="/findAll")
    @ResponseBody
    public  MessageBox findAll(){

        List<Print> list = this.printService.getAll();
        return this.returnSuccessInfo("保存成功",list);
    }
    @RequestMapping(value="/findPrint")
    @ResponseBody
    public MessageBox findPrint(String id){
        Print print = this.printService.findPrint(Long.parseLong(id));
        return this.returnSuccessInfo("查询成功",print);
    }

    @RequestMapping(value="/printMessage")
    @ResponseBody
    public MessageBox printMessage(String id,String billno){
        Map<String,Object> map=new HashMap<String,Object>();
        if(billno.indexOf(BillConstant.BillPrefix.purchase)!=-1){
            Print print = this.printService.findPrint(Long.parseLong(id));
            PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.load(billno);
            PrintCont printCont = new PrintCont();
            printCont.setCompanyName("ANCTENT STONE");
            printCont.setCusterm(purchaseOrderBill.getOrigUnitName());
            printCont.setRemark(purchaseOrderBill.getRemark());
            if(CommonUtil.isBlank(purchaseOrderBill.getPayPrice())){
                printCont.setPrice("0");
            }else{
                //DecimalFormat df = new DecimalFormat("#.00");
                //printCont.setPrice(df.format(purchaseOrderBill.getPayPrice()));
                printCont.setPrice(purchaseOrderBill.getPayPrice()+"");
            }
            printCont.setOrderDate(CommonUtil.getDateString(purchaseOrderBill.getBillDate(),"yyyy-MM-dd"));
            printCont.setPrintTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            printCont.setStorehouseName(purchaseOrderBill.getDestName());
            printCont.setOrdersSn(purchaseOrderBill.getBillNo());
            printCont.setBilltype("采购单");
            User user = CacheManager.getUserById(purchaseOrderBill.getOprId());
            printCont.setOperatorName(user.getName());
            List<PurchaseOrderBillDtl> billDtlByBillNo = this.purchaseOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",print);
            map.put("cont",printCont);
            map.put("contDel",billDtlByBillNo);

        }
        if(billno.indexOf(BillConstant.BillPrefix.saleOrder)!=-1){
            Print print = this.printService.findPrint(Long.parseLong(id));
            SaleOrderBill saleOrderBill = this.saleOrderBillService.load(billno);
            PrintCont printCont = new PrintCont();
            printCont.setCompanyName("ANCTENT STONE");
            printCont.setCusterm(saleOrderBill.getDestUnitName());
            printCont.setRemark(saleOrderBill.getRemark());
            if(CommonUtil.isNotBlank(saleOrderBill.getAfterBalance())){
                printCont.setAfterBalance(saleOrderBill.getAfterBalance()+"");
            }
            if(CommonUtil.isNotBlank(saleOrderBill.getPreBalance())){
                printCont.setPreBalance(saleOrderBill.getPreBalance()+"");
            }
            if(CommonUtil.isBlank(saleOrderBill.getPayPrice())){
                printCont.setPrice("0");
            }else{
                //DecimalFormat df = new DecimalFormat("#.00");
                //printCont.setPrice(df.format(saleOrderBill.getPayPrice()));
                printCont.setPrice(saleOrderBill.getPayPrice()+"");
            }
            printCont.setOrderDate(CommonUtil.getDateString(saleOrderBill.getBillDate(),"yyyy-MM-dd"));
            printCont.setPrintTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            printCont.setStorehouseName(saleOrderBill.getOrigName());
            User user1 = this.userService.getUser(saleOrderBill.getBusnissId());
            printCont.setBusinessName(user1.getName());
            printCont.setOrdersSn(saleOrderBill.getBillNo());
            printCont.setBilltype("销售单");
            User user = CacheManager.getUserById(saleOrderBill.getOprId());
            printCont.setOperatorName(user.getName());
            String ownerId = user.getOwnerId();
            GuestView guestView = this.guestViewService.findByid(ownerId);
            if(CommonUtil.isNotBlank(guestView)){
                String Province="";
                String City="";
                String AreaId="";
                String Address="";
                if(CommonUtil.isNotBlank(guestView.getProvince())){
                    Province=guestView.getProvince();
                }
                if(CommonUtil.isNotBlank(guestView.getCity())){
                    City=guestView.getCity();
                }
                if(CommonUtil.isNotBlank(guestView.getAreaId())){
                    AreaId=guestView.getAreaId();
                }
                if(CommonUtil.isNotBlank(guestView.getAddress())){
                    Address=guestView.getAddress();
                }
                printCont.setAddress(Province+City+AreaId+Address);
                printCont.setPhone(guestView.getPhone());
            }
            List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",print);
            map.put("cont",printCont);
            map.put("contDel",billDtlByBillNo);
        }
        if(billno.indexOf(BillConstant.BillPrefix.purchaseReturn)!=-1){
            Print print = this.printService.findPrint(Long.parseLong(id));
            PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.load(billno);
            PrintCont printCont = new PrintCont();
            printCont.setCompanyName("ANCTENT STONE");
            printCont.setCusterm(purchaseReturnBill.getOrigUnitName());
            printCont.setRemark(purchaseReturnBill.getRemark());
            if(CommonUtil.isBlank(purchaseReturnBill.getPayPrice())){
                printCont.setPrice("0");
            }else{
                //DecimalFormat df = new DecimalFormat("#.00");
                //printCont.setPrice(df.format(saleOrderBill.getPayPrice()));
                printCont.setPrice(purchaseReturnBill.getPayPrice()+"");
            }
            printCont.setOrderDate(CommonUtil.getDateString(purchaseReturnBill.getBillDate(),"yyyy-MM-dd"));
            printCont.setPrintTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            printCont.setStorehouseName(purchaseReturnBill.getOrigName());
            printCont.setOrdersSn(purchaseReturnBill.getBillNo());
            printCont.setBilltype("采购退货");
            User user = CacheManager.getUserById(purchaseReturnBill.getOprId());
            printCont.setOperatorName(user.getName());
            List<PurchaseReturnBillDtl> billDtlByBillNo = this.purchaseReturnBillService.findBillDtlByBillNo(billno);
            map.put("print",print);
            map.put("cont",printCont);
            map.put("contDel",billDtlByBillNo);

        }
        if(billno.indexOf(BillConstant.BillPrefix.SaleOrderReturn)!=-1){
            Print print = this.printService.findPrint(Long.parseLong(id));
            SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.load(billno);
            PrintCont printCont = new PrintCont();
            printCont.setCompanyName("ANCTENT STONE");
            printCont.setCusterm(saleOrderReturnBill.getOrigUnitName());
            printCont.setRemark(saleOrderReturnBill.getRemark());
            if(CommonUtil.isBlank(saleOrderReturnBill.getPayPrice())){
                printCont.setPrice("0");
            }else{
                //DecimalFormat df = new DecimalFormat("#.00");
                //printCont.setPrice(df.format(saleOrderBill.getPayPrice()));
                printCont.setPrice(saleOrderReturnBill.getPayPrice()+"");
            }
            printCont.setOrderDate(CommonUtil.getDateString(saleOrderReturnBill.getBillDate(),"yyyy-MM-dd"));
            printCont.setPrintTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            printCont.setStorehouseName(saleOrderReturnBill.getDestName());
            printCont.setOrdersSn(saleOrderReturnBill.getBillNo());
            printCont.setBilltype("销售退货");
            User user1 = this.userService.getUser(saleOrderReturnBill.getBusnissId());
            printCont.setBusinessName(user1.getName());
            User user = CacheManager.getUserById(saleOrderReturnBill.getOprId());
            printCont.setOperatorName(user.getName());
            String ownerId = user.getOwnerId();
            GuestView guestView = this.guestViewService.findByid(ownerId);
            if(CommonUtil.isNotBlank(guestView)){
                String Province="";
                String City="";
                String AreaId="";
                String Address="";
                if(CommonUtil.isNotBlank(guestView.getProvince())){
                    Province=guestView.getProvince();
                }
                if(CommonUtil.isNotBlank(guestView.getCity())){
                    City=guestView.getCity();
                }
                if(CommonUtil.isNotBlank(guestView.getAreaId())){
                    AreaId=guestView.getAreaId();
                }
                if(CommonUtil.isNotBlank(guestView.getAddress())){
                    Address=guestView.getAddress();
                }
                printCont.setAddress(Province+City+AreaId+Address);
                printCont.setPhone(guestView.getPhone());
            }
            List<SaleOrderReturnBillDtl> billDtlByBillNo = this.saleOrderReturnBillService.findBillDtlByBillNo(billno);
            map.put("print",print);
            map.put("cont",printCont);
            map.put("contDel",billDtlByBillNo);

        }
        if(billno.indexOf(BillConstant.BillPrefix.Transfer)!=-1){
            Print print = this.printService.findPrint(Long.parseLong(id));
            TransferOrderBill transferOrderBill = this.transferOrderBillService.load(billno);
            PrintCont printCont = new PrintCont();
            printCont.setCompanyName("ANCTENT STONE");
            printCont.setCusterm(transferOrderBill.getOrigUnitName());
            printCont.setRemark(transferOrderBill.getRemark());
            if(CommonUtil.isBlank(transferOrderBill.getPayPrice())){
                printCont.setPrice("0");
            }else{
                //DecimalFormat df = new DecimalFormat("#.00");
                //printCont.setPrice(df.format(saleOrderBill.getPayPrice()));
                printCont.setPrice(transferOrderBill.getPayPrice()+"");
            }
            printCont.setOrderDate(CommonUtil.getDateString(transferOrderBill.getBillDate(),"yyyy-MM-dd"));
            printCont.setPrintTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            printCont.setStorehouseName(transferOrderBill.getDestName());
            printCont.setOrdersSn(transferOrderBill.getBillNo());
            printCont.setBilltype("调拨单");
            User user = CacheManager.getUserById(transferOrderBill.getOprId());
            printCont.setOperatorName(user.getName());
            List<TransferOrderBillDtl> billDtlByBillNo= this.transferOrderBillService.findBillDtlByBillNo(billno);
            map.put("print",print);
            map.put("cont",printCont);
            map.put("contDel",billDtlByBillNo);

        }

        return this.returnSuccessInfo("查询成功",map);
    }
    @RequestMapping(value="/findFranchiseeUser")
    @ResponseBody
    public MessageBox findFranchiseeUser(){
        List<User> franchisee = this.userService.getFranchisee();
        return new MessageBox(true, "保存成功",franchisee);
    }
    @RequestMapping(value="/printA4Message")
    @ResponseBody
    public MessageBox printA4Message(String id,String billno,String destUnitId){
        Map<String,Object> map=new HashMap<String,Object>();
        Print print = this.printService.findPrint(Long.parseLong(id));
        List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(billno);
        Double alltotActPrice=0D;
        for (int i = 0; i < billDtlByBillNo.size(); i++) {
            SaleOrderBillDtl dtl = billDtlByBillNo.get(i);
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            alltotActPrice+=dtl.getTotActPrice();

        }
        GuestView guestView = this.guestViewService.load(destUnitId);

        PrintCont printCont = new PrintCont();
        DecimalFormat    df   = new DecimalFormat("######0.00");
        printCont.setBefor(df.format(guestView.getOwingValue()-alltotActPrice)+"");
        printCont.setNow(df.format(guestView.getOwingValue())+"");
        map.put("print",print);
        map.put("cont",printCont);
        map.put("contDel",billDtlByBillNo);
        return this.returnSuccessInfo("查询成功",map);
    }


}
