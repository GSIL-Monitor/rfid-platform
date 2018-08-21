package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 普通用户 user.type=0
 */

@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController implements IBaseInfoController<User>{
    @Autowired
    private UserService userService;
    @Autowired
    private  SaleOrderBillService saleOrderBill;
    @Autowired
    private  SaleOrderReturnBillService saleOrderReturnBill;
    @Autowired
    private  ConsignmentBillService consignmentBill;
    @Autowired
    private  TransferOrderBillService transferOrderBill;
    @Autowired
    private  PurchaseOrderBillService purchaseOrderBill;
    @Autowired
    private  PurchaseReturnBillService purchaseReturnBill;


    private PropertyFilter typeFilter = new PropertyFilter("EQI_type",""+Constant.UserType.User);
	
    @RequestMapping("/index")
    @Override
	public String index() {		
		return "/views/sys/user";
	}
    
    @RequestMapping("/page")
    @ResponseBody
	@Override
	public Page<User> findPage(Page<User> page) throws Exception {
		this.logAllRequestParams();//日志
		List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
		System.out.println("filters");
        filters.add(typeFilter);
        page.setPageProperty();

        page=this.userService.findPage(page, filters);

        for(User u: page.getRows()){
        	if(CommonUtil.isNotBlank(u.getOwnerId())) {
				Unit owner = CacheManager.getUnitById(u.getOwnerId());
				if(CommonUtil.isNotBlank(owner)){
					u.setUnitName(owner.getName());
				}

			}
    }
		return page;
	}

    @RequestMapping("/pageWS")
    @ResponseBody
    public Page<User> findUserPage(Page<User> page) throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        System.out.println("filters");
        page.setPageProperty();

        page=this.userService.findPage(page, filters);

        return page;
    }
    @RequestMapping(value = {"/list","/listWS"})
    @ResponseBody
	@Override
	public List<User> list() throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        System.out.println("filters");
        List<User> users = this.userService.find(filters);
        if (CommonUtil.isNotBlank(users)) {
            for (User u : users) {
                if (CommonUtil.isNotBlank(u.getOwnerId())) {
                    Unit unit = CacheManager.getUnitById(u.getOwnerId());
                    if (CommonUtil.isNotBlank(unit)){
                        String ownerName = CacheManager.getUnitById(u.getOwnerId()).getName();
                        u.setUnitName(ownerName);
                    }
                }
             }
            return users;
        }
        return new ArrayList<>();
    }

    @RequestMapping(value="/save")
    @ResponseBody
	@Override
	public MessageBox save(User user) throws Exception {
        this.logAllRequestParams();
        String pageType = this.getReqParam("pageType");
        User sessionUser = this.getCurrentUser();
        User u=this.userService.getUserByCode(user.getCode());
        if (pageType.equals("add")){
            if (CommonUtil.isBlank(u)){
                u=new User();
                u.setId(user.getCode());
                u.setCode(user.getCode());
                u.setLocked(0);
                u.setCreatorId(sessionUser.getCode());
                u.setCreateDate(new Date());
                u.setPhone(user.getPhone());
                u.setType(Constant.UserType.User);
                u.setSrc(Constant.DataSrc.SYS);
                u.setName(user.getName());
                u.setPassword(user.getPassword());
                u.setIsAdmin(user.getIsAdmin());
                u.setRoleId(user.getRoleId());
                u.setPhone(user.getPhone());
                u.setOwnerId(user.getOwnerId());
            }else {
                return returnFailInfo("登录名已存在，请重新输入");
            }
        }else {
            u.setName(user.getName());
            u.setPassword(user.getPassword());
            u.setIsAdmin(user.getIsAdmin());
            u.setRoleId(user.getRoleId());
            u.setPhone(user.getPhone());
            u.setOwnerId(user.getOwnerId());
        }
           try {
               this.userService.save(u);
               CacheManager.refreshUserCache();
               return returnSuccessInfo("保存成功");
           }catch (Exception e){
                /*将异常打印到日志*/
                this.logger.error(e.getMessage());
                return returnFailInfo("保存失败");
           }
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

    @RequestMapping("/resetPwd")
    @ResponseBody
    public MessageBox resetPwd(String code, String password) throws Exception {
        this.logAllRequestParams();
        User user = this.userService.getUser(code);
        user.setPassword(password);

        this.userService.updateUser(user);
        return this.returnSuccessInfo("密码修改成功!");
    }

    @RequestMapping("/logout")
    @ResponseBody
    public MessageBox logout() throws Exception {
        try{
            //修改单据
            String billNosale=(String)session.getAttribute("billNosale");

            if(CommonUtil.isNotBlank(billNosale)){
                try {
                    SaleOrderBill saleOrderBill = this.saleOrderBill.get("billNo", billNosale);
                    if(CommonUtil.isNotBlank(saleOrderBill)){
                        System.out.println("saleOrder34:"+saleOrderBill.getId());
                        saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.saleOrderBill.save(saleOrderBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();

                }

            }
            String billNosaleReturn=(String)  this.getSession().getAttribute("billNosaleReturn");

            if(CommonUtil.isNotBlank(billNosaleReturn)){
                try {
                    SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBill.findBillByBillNo(billNosaleReturn);
                    if(CommonUtil.isNotBlank(saleOrderReturnBill)){
                        System.out.println("saleOrder34:"+saleOrderReturnBill.getId());
                        saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.saleOrderReturnBill.save(saleOrderReturnBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String billNoConsignment=(String)  this.getSession().getAttribute("billNoConsignment");

            if(CommonUtil.isNotBlank(billNoConsignment)){
                try {
                    ConsignmentBill consignmentBill = this.consignmentBill.findBillByBillNo(billNoConsignment);
                    if(CommonUtil.isNotBlank(consignmentBill)){
                        System.out.println("saleOrder34:"+consignmentBill.getId());
                        consignmentBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.consignmentBill.update(consignmentBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String billNotransfer=(String)  this.getSession().getAttribute("billNotransfer");

            if(CommonUtil.isNotBlank(billNoConsignment)){
                try {
                    TransferOrderBill transferOrderBill = this.transferOrderBill.get("billNo", billNotransfer);
                    if(CommonUtil.isNotBlank(transferOrderBill)){
                        System.out.println("saleOrder34:"+transferOrderBill.getId());
                        transferOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.transferOrderBill.update(transferOrderBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String billNopurchase=(String)  this.getSession().getAttribute("billNopurchase");

            if(CommonUtil.isNotBlank(billNoConsignment)){
                try {
                    PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBill.get("billNo",billNopurchase);
                    if(CommonUtil.isNotBlank(consignmentBill)){
                        System.out.println("saleOrder34:"+purchaseOrderBill.getId());
                        purchaseOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.purchaseOrderBill.save(purchaseOrderBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String billNoPurchaseReturn=(String) this.getSession().getAttribute("billNoPurchaseReturn");

            if(CommonUtil.isNotBlank(billNoConsignment)){
                try {
                    PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBill.findUniqueByBillNo(billNoPurchaseReturn);
                    if(CommonUtil.isNotBlank(consignmentBill)){
                        System.out.println("saleOrder34:"+purchaseReturnBill.getId());
                        purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
                        this.purchaseReturnBill.save(purchaseReturnBill);
                    }else{
                        System.out.println("saleOrder12:null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            this.getSession().removeAttribute(Constant.Session.User_Session);
            this.getSession().removeAttribute("Session_Unit");
           // this.getSession().invalidate();
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()) {
                logger.error("此用户无Session");
                this.returnFailInfo("此用户无Session");
            } else {
                currentUser.logout();
            }


        }catch(Exception e){
            logger.error(e.getMessage());
            return this.returnFailInfo(e.getLocalizedMessage());
        }
        return this.returnSuccessInfo("退出成功");
    }

    @RequestMapping("/login")
    public ModelAndView login(String code, String password, String loginType) throws Exception {
        this.logAllRequestParams();
        this.getRequest().setCharacterEncoding("utf-8");
        this.getResponse().setContentType("text/html;charset=utf-8");
        ModelAndView model = new ModelAndView();

        try {
            //  user = this.userService.getUser(userCode, password);
            UsernamePasswordToken token = new UsernamePasswordToken(code, password);
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            model.setViewName("/oliveIndex");
            if(currentUser.isAuthenticated()){
                System.out.println("用户[" + code + "]登录认证通过");
                return model;
            }else{
                token.clear();
            }

        } catch (ServiceException ex) {
            final String message = ex.getMessage();
            this.getSession().setAttribute("message", message);
            //model.setViewName("/oliveLogin");
            response.sendRedirect(request.getContextPath()+"/oliveLogin.jsp");
            return null;
        }catch (UnknownAccountException e){
            this.getSession().setAttribute("message", e.getMessage());
            response.sendRedirect(request.getContextPath()+"/oliveLogin.jsp");
            return null;
        }catch (IncorrectCredentialsException e){
            this.getSession().setAttribute("message", e.getMessage());
            response.sendRedirect(request.getContextPath()+"/oliveLogin.jsp");
            return null;
        }

        return model;
    }

    @RequestMapping("/loginOrCode")
    public ModelAndView loginOrCode(String code, String password, String loginType) throws Exception {
        this.logAllRequestParams();
        this.getRequest().setCharacterEncoding("utf-8");
        this.getResponse().setContentType("text/html;charset=utf-8");
        ModelAndView model = new ModelAndView();

        try {
            //  user = this.userService.getUser(userCode, password);
            UsernamePasswordToken token = new UsernamePasswordToken(code, password);
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
           // model.setViewName("/oliveIndex");
            //response.sendRedirect("https://web.qinsilk.com/tms/mall/29358?salerId=130775");
            if(currentUser.isAuthenticated()){
                System.out.println("用户[" + code + "]登录认证通过");
                response.sendRedirect("https://web.qinsilk.com/tms/mall/29358?salerId=130775");
                return null;
            }else{
                token.clear();
            }

        } catch (ServiceException ex) {
            final String message = ex.getMessage();
            this.getSession().setAttribute("message", message);
            //model.setViewName("/oliveLogin");
            response.sendRedirect(request.getContextPath()+"/orCodeLogin.jsp");
            return null;
        }catch (UnknownAccountException e){
            this.getSession().setAttribute("message", e.getMessage());
            response.sendRedirect(request.getContextPath()+"/orCodeLogin.jsp");
            return null;
        }catch (IncorrectCredentialsException e){
            this.getSession().setAttribute("message", e.getMessage());
            response.sendRedirect(request.getContextPath()+"/orCodeLogin.jsp");
            return null;
        }

        return model;
    }

}
