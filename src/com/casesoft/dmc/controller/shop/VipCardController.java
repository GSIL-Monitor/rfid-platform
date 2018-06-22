package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.VipCard;
import com.casesoft.dmc.service.shop.VipCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 会员卡管理
 * Created by luis on 2018/6/20.
 */
@Controller
@RequestMapping("/shop/vipCard")
public class VipCardController  extends BaseController implements IBaseInfoController<VipCard> {
    @Autowired
    private VipCardService vipCardService;
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/shop/vipCard");
        return mv;
    }
    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<VipCard> findPage(Page<VipCard> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.vipCardService.findPage(page, filters);
        return page;
    }

    @RequestMapping(value = "/list")
    @ResponseBody()
    @Override
    public List<VipCard> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<VipCard> vipCards = this.vipCardService.find(filters);
        return vipCards;
    }
    /*保存*/
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(VipCard vipCard) throws Exception {
        this.logAllRequestParams();
        String pageType = this.getReqParam("pageType");
        VipCard rank= this.vipCardService.get("id", vipCard.getId());
        if ("add".equals(pageType)) {
            if (CommonUtil.isNotBlank(rank)) {
                return this.returnFailInfo("保存失败");
            } else {
                rank = new VipCard();
                rank.setId(vipCard.getRank());
                rank.setName(vipCard.getName());
                rank.setRank(vipCard.getRank());
                rank.setRemark(vipCard.getRemark());
                rank.setDiscount(vipCard.getDiscount());
                rank.setFreeShipping(vipCard.getFreeShipping());
                rank.setUpgradeType(vipCard.getUpgradeType());
                rank.setUpgradeDealNo(vipCard.getUpgradeDealNo());
                rank.setUpgradeConsumeNo(vipCard.getUpgradeConsumeNo());
                rank.setUpgradePoints(vipCard.getUpgradePoints());
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                rank.setCreateTime(simpleDateFormat.format(date));
                rank.setRank(vipCard.getRank());
            }
        } else {
            rank.setName(vipCard.getName());
            rank.setRank(vipCard.getRank());
            rank.setRemark(vipCard.getRemark());
            rank.setDiscount(vipCard.getDiscount());
            rank.setFreeShipping(vipCard.getFreeShipping());
            rank.setUpgradeType(vipCard.getUpgradeType());
            rank.setUpgradeDealNo(vipCard.getUpgradeDealNo());
            rank.setUpgradeConsumeNo(vipCard.getUpgradeConsumeNo());
            rank.setUpgradePoints(vipCard.getUpgradePoints());
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
            rank.setCreateTime(simpleDateFormat.format(date));
            rank.setRank(vipCard.getRank());
        }
        try {
            this.vipCardService.save(rank);
            return returnSuccessInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }
    }
    @RequestMapping("/findCardDiscount")
    @ResponseBody
    public MessageBox findCardDiscount(String idCard){
        VipCard vipCard = this.vipCardService.get("rank",idCard);
        return new MessageBox(true,"获取会员等级折扣成功",vipCard.getDiscount());
    }

    @RequestMapping("/membershipUpgrade")
    @ResponseBody
    public void membershipUpgrade(){
        this.vipCardService.membershipUpgrade();
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @RequestMapping("/del")
    @ResponseBody
    public MessageBox delete() throws Exception {
        String id = this.getReqParam("id");
        try {
            this.vipCardService.delete(id);
            return returnSuccessInfo("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return returnFailInfo("删除失败");
        }
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
        return "/views/shop/vipCard";
    }
}
