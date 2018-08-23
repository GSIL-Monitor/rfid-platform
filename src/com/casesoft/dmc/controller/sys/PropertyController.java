package com.casesoft.dmc.controller.sys;

import java.text.SimpleDateFormat;
import java.util.*;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.cfg.MultiLevelRelation;
import com.casesoft.dmc.model.cfg.VO.TreeVO;
import com.casesoft.dmc.model.shop.payDetail;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.MultiLevelRelationService;
import com.casesoft.dmc.service.shop.payDetailService;
import com.casesoft.dmc.service.sys.impl.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.cfg.PropertyService;


@Controller
@RequestMapping("/sys/property")
public class PropertyController extends BaseController implements IBaseInfoController<PropertyType> {

    @Autowired
    private PropertyKeyService propertyKeyService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private payDetailService payDetailService;
    @Autowired
    private MultiLevelRelationService multiLevelRelationService;


    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/property";
    }


    @Override
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<PropertyType> findPage(Page<PropertyType> page)
            throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.propertyService.findPage(page, filters);
        return page;
    }

    @RequestMapping(value = {"/searchByType","/searchByTypeWS"})
    @ResponseBody
    public List<PropertyKey> searchByType(String type) throws Exception {
        this.logAllRequestParams();
        List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType(type);
        return pkList;
    }
    @RequestMapping(value = "/searchType")
    @ResponseBody
    public List<PropertyKey> searchType(String type){
        try {
            List<PropertyKey> propertyKeyList = this.propertyService.getPKByType(type);
            return propertyKeyList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<PropertyType> list() throws Exception {

        return null;
    }

    @Override
    @RequestMapping(value = "/save")
    @ResponseBody
    public MessageBox save(PropertyType entity) throws Exception {
        try {
            PropertyType propertyTypebyid = this.propertyService.findPropertyTypebyid(entity.getId());
            if (CommonUtil.isBlank(propertyTypebyid)) {
                Integer num = this.propertyService.findtypeNum(entity.getType());
                entity.setSeqNo((num + 1));
                entity.setBrand("*");
                entity.setIsUse("Y");
                entity.setKeyId(entity.getId());
                this.propertyService.save(entity);
            } else {
                propertyTypebyid.setType(entity.getType());
                propertyTypebyid.setValue(entity.getValue());
                this.propertyService.save(propertyTypebyid);
            }
            CacheManager.refreshPropertyTypeCache();
            return returnSuccessInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }
    }

    /**
     * 保存属性信息
     */
    @RequestMapping(value = {"/saveproperty", "/savepropertyWS"})
    @ResponseBody
    public MessageBox saveproperty(PropertyKey entity, String userId) throws Exception {
        try {
            PropertyKey propertyKey = this.propertyService.findPropertyKeyByNameAndType(entity.getName(), entity.getType());
            if (CommonUtil.isBlank(propertyKey)) {
                Integer num = this.propertyService.findtkeyNum(entity.getType());
                entity.setSeqNo((num + 1));
                if (getCurrentUser() == null) {     //小程序增加商品，userId传值
                    User CurrentUser = CacheManager.getUserById(userId);
                    entity.setOwnerId(CurrentUser.getCreatorId());
                    entity.setRegisterId(CurrentUser.getId());
                }else {       //web增加商品,session传值
                    entity.setOwnerId(getCurrentUser().getCreatorId());
                    entity.setRegisterId(getCurrentUser().getId());
                }
                entity.setIsDefault("0");
                entity.setCode(entity.getSeqNo() + "");
                entity.setType(entity.getId());
                entity.setId(entity.getId() + "-" + entity.getCode());
                entity.setLocked(0);
                entity.setRegisterDate(new Date());
                entity.setYnuse("Y");
                if(entity.getType().equals("C1")){
                    Unit unit = new Unit();
                    unit.setId(entity.getId());
                    unit.setCode(entity.getId());
                    unit.setName(entity.getName());
                    Unit ut = this.vendorService.findById(unit.getId());
                    if (CommonUtil.isBlank(ut)) {
                        ut = new Unit();
                        if (CommonUtil.isBlank(unit.getCode())) {
                            String code = this.vendorService.findMaxCode(Constant.UnitType.Vender);
                            ut.setCode(code);
                        } else {
                            ut.setCode(unit.getCode());
                        }
                        ut.setId(ut.getCode());
                        if (this.getCurrentUser() == null) {  //小程序增加供应商，userId传值
                            User CurrentUser = CacheManager.getUserById(userId);
                            ut.setCreatorId(CurrentUser.getCode());
                        }else {   //web增加供应商,session传值
                            ut.setCreatorId(this.getCurrentUser().getCode());
                        }
                        ut.setCreateTime(new Date());
                    }
                    ut.setType(Constant.UnitType.Vender);
                    ut.setUpdateTime(new Date());
                    if (this.getCurrentUser() == null) {
                        User CurrentUser = CacheManager.getUserById(userId);
                        ut.setUpdaterId(CurrentUser.getCode());
                    }else {
                        ut.setUpdaterId(this.getCurrentUser().getCode());
                    }
                    ut.setOwnerId("1");
                    ut.setSrc(Constant.DataSrc.SYS);
                    ut.setName(unit.getName());
                    this.propertyService.saveKey(entity,ut);
                }else {
                    this.propertyService.saveKey(entity);
                }
            } else {
                return returnFailInfo("保存失败,名称已存在不能重复添加");
            }
            CacheManager.refreshUnitCache();
            CacheManager.refreshPropertyCache();
            return returnSuccessInfo("保存成功", entity);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.getMessage());
            return returnFailInfo("保存失败");
        }
    }

    @RequestMapping(value = "checkCode")
    @ResponseBody
    public Map<String, Boolean> checkCode(String id, String pageType) {
        PropertyType propertyTypebyid = this.propertyService.findPropertyTypebyid(id);
        Map<String, Boolean> json = new HashMap<String, Boolean>();
        if ("add".equals(pageType) && CommonUtil.isNotBlank(propertyTypebyid)) {
            Boolean valid = json.put("valid", false);
        } else {
            json.put("valid", true);
        }
        return json;

    }

    @RequestMapping(value = "checkCodetype")
    @ResponseBody
    public Map<String, Boolean> checkCodetype(String code, String type, String pageType) {
        PropertyKey propertyKeybyid = this.propertyService.findPropertyKeybyid(type + "-" + code);
        Map<String, Boolean> json = new HashMap<String, Boolean>();
        if ("add".equals(pageType) && CommonUtil.isNotBlank(propertyKeybyid)) {
            Boolean valid = json.put("valid", false);
        } else {
            json.put("valid", true);
        }
        return json;

    }

    @RequestMapping("/changetypeStatus")
    @ResponseBody
    public MessageBox changeStatus(String id, String status) {
        this.logAllRequestParams();
        PropertyType propertyTypebyid = this.propertyService.findPropertyTypebyid(id);
        propertyTypebyid.setIsUse(status);
        try {
            this.propertyService.save(propertyTypebyid);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }

    @RequestMapping("/changepropertyStatus")
    @ResponseBody
    public MessageBox changepropertyStatus(String id, String status) {
        this.logAllRequestParams();
        PropertyKey propertyKeybyid = this.propertyService.findPropertyKeybyid(id);
        propertyKeybyid.setYnuse(status);
        try {
            this.propertyService.saveKey(propertyKeybyid);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }
    @RequestMapping("/setDefault")
    @ResponseBody
    public MessageBox setDefault(String id) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = new ArrayList<>();
        PropertyFilter filter = new PropertyFilter("EQS_isDefault", "1");
        PropertyFilter filter1 = new PropertyFilter("EQS_type", "PT");
        filters.add(filter);
        filters.add(filter1);
        List<PropertyKey> propertyKeys = this.propertyKeyService.find(filters);
        if(CommonUtil.isNotBlank(propertyKeys)){
            for (PropertyKey propertyKey:propertyKeys){
                propertyKey.setIsDefault("0");
                this.propertyService.saveKey(propertyKey);
            }
        }
        PropertyKey propertyKeybyid = this.propertyService.findPropertyKeybyid(id);
        propertyKeybyid.setIsDefault("1");
        try {
            this.propertyService.saveKey(propertyKeybyid);
            return returnSuccessInfo("设置默认支付成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("设置默认支付失败");
        }
    }
    @RequestMapping("/savePayPriceWS")
    @ResponseBody
    public MessageBox savePayPrice(payDetail payDetail) {
        this.logAllRequestParams();
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            payDetail.setPayDate(df.format(new Date()));
            payDetail.setId(payDetail.getBillNo()+payDetail.getPayType());
            payDetailService.save(payDetail);
        }
        catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,"结算失败");
        }
        return new MessageBox(true,"结算成功");
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

    @RequestMapping("/findclassname")
    @ResponseBody
    public Page<PropertyKey> findclassname(Page<PropertyKey> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.propertyKeyService.findPage(page, filters);
        return page;
    }
    @RequestMapping("/findclass9name")
    @ResponseBody
    public List<PropertyKey> findclass9name() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());

        List<PropertyKey> propertyKeys = this.propertyKeyService.find(filters);
        return propertyKeys;
    }

    @RequestMapping(value = "/listPropertyTree")
    @ResponseBody
    public List<TreeVO> listPropertyTree(String multiLevelType) {
        List<TreeVO> treeVOS = this.propertyService.listPropertyTree(multiLevelType);
        return treeVOS;
    }

    @RequestMapping("/loadPropertyDetail")
    @ResponseBody
    public PropertyKey loadPropertyDetail(String id){
        return this.propertyService.findPropertyKeybyid(id);
    }

    /**
     * 保存属性信息
     */
    @RequestMapping(value = {"/savePropertyInTree", "/savePropertyInTreeWS"})
    @ResponseBody
    public MessageBox saveproperty(PropertyKey entity, String userId, String parentId) {
        try {
            PropertyKey propertyKey = this.propertyService.findPropertyKeybyid(entity.getId());
            if (CommonUtil.isBlank(propertyKey)) {
                PropertyKey propertyKeyByNameAndType = this.propertyService.findPropertyKeyByNameAndType(entity.getName(), entity.getType());
                if(CommonUtil.isNotBlank(propertyKeyByNameAndType) && propertyKeyByNameAndType.getName().equals(entity.getName())){
                    return returnFailInfo("保存失败,名称已存在不能重复添加");
                }
                propertyKey = new PropertyKey();
                Integer num = this.propertyService.findtkeyNum(entity.getType());
                propertyKey.setSeqNo((num + 1));
                if (getCurrentUser() == null) {     //小程序增加商品，userId传值
                    User CurrentUser = CacheManager.getUserById(userId);
                    propertyKey.setOwnerId(CurrentUser.getCreatorId());
                    propertyKey.setRegisterId(CurrentUser.getId());
                }else {       //web增加商品,session传值
                    propertyKey.setOwnerId(getCurrentUser().getCreatorId());
                    propertyKey.setRegisterId(getCurrentUser().getId());
                }
                propertyKey.setIsDefault("0");
                propertyKey.setCode(propertyKey.getSeqNo() + "");
                propertyKey.setId(entity.getType() + "-" + propertyKey.getCode());
                propertyKey.setLocked(0);
                propertyKey.setRegisterDate(new Date());
            }
            propertyKey.setName(entity.getName());
            propertyKey.setYnuse(entity.getYnuse());
            propertyKey.setType(entity.getType());
            if("C3".equals(entity.getType())){
                this.propertyService.saveKey(propertyKey, parentId, Constant.MultiLevelType.C3);
            }
            CacheManager.refreshUnitCache();
            CacheManager.refreshPropertyCache();
            return returnSuccessInfo("保存成功", entity);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.getMessage());
            return returnFailInfo("保存失败");
        }
    }

    @RequestMapping(value = {"/listMultiLevel","/listMultiLevelWS"})
    @ResponseBody
    public List<MultiLevelRelation> listMultiLevel(String type) {
        this.logAllRequestParams();
        List<MultiLevelRelation> multiLevelList = this.multiLevelRelationService.listMultiLevelByType(type);
        return multiLevelList;
    }
}
