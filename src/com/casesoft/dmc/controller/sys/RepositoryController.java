package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.tag.util.StringUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.VO.State;
import com.casesoft.dmc.model.cfg.VO.TreeVO;
import com.casesoft.dmc.model.rem.RepositoryManagement;
import com.casesoft.dmc.model.rem.VO.TreeChildVO;
import com.casesoft.dmc.model.rem.VO.code;
import com.casesoft.dmc.model.rem.VO.sku;
import com.casesoft.dmc.model.rem.VO.styled;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.rem.RepositoryManagementService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lly on 2018/7/7.
 */
@Controller
@RequestMapping("sys/repositoryController")
public class RepositoryController extends BaseController implements IBaseInfoController<Unit> {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private RepositoryManagementService repositoryManagementService;
    @Autowired
    private EpcStockService epcStockService;

    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        return null;
    }

    @Override
    public List<Unit> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit entity) throws Exception {
        this.logAllRequestParams();
        User currentUser = this.getCurrentUser();
        String id = entity.getOwnerId();
        RepositoryManagement repositoryManagement = new RepositoryManagement();
        repositoryManagement.setParentId(entity.getOwnerId());
        repositoryManagement.setRoot(false);
        repositoryManagement.setRemLevelType(Constant.RepositoryType.Company);
        repositoryManagement.setRelatedId(id);
        repositoryManagement.setDeep(Constant.RepositoryType.rack);
        if(!StringUtil.isEmpty(entity.getRackId())){
            repositoryManagement.setName(entity.getRackId()+"号货架");
            id += "-"+entity.getRackId();
            repositoryManagement.setTreePath(entity.getOwnerId()+">"+id);
        }
        if(!StringUtil.isEmpty(entity.getLevelId()) || !StringUtil.isEmpty(entity.getAllocationId())){
            //接上父节点的treepath
            if(!StringUtil.isEmpty(entity.getLevelId())){
                id += "-"+entity.getLevelId();
                RepositoryManagement rm = repositoryManagementService.get("id",entity.getOwnerId());
                repositoryManagement.setTreePath(rm.getTreePath() + ">" + id);
                repositoryManagement.setName(entity.getLevelId()+"号货层");
                repositoryManagement.setDeep(Constant.RepositoryType.level);
            }
            if(!StringUtil.isEmpty(entity.getAllocationId())){
                id += "-"+entity.getAllocationId();
                RepositoryManagement rm = repositoryManagementService.get("id",entity.getOwnerId());
                repositoryManagement.setTreePath(rm.getTreePath() + ">" + id);
                repositoryManagement.setName(entity.getAllocationId()+"号货位");
                repositoryManagement.setDeep(Constant.RepositoryType.allocation);
            }
        }
        repositoryManagement.setId(id);
        try {
            this.repositoryManagementService.save(repositoryManagement);
            //刷新缓存
            //CacheManager.refreshUnitCache();
            return this.returnSuccessInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
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

    //@RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/sys/repositoryManagement";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {

        ModelAndView mv = new ModelAndView("/views/sys/repositoryManagement");
        User user = this.getCurrentUser();
        mv.addObject("ownerId", user.getOwnerId());
        return mv;
    }

    @RequestMapping(value = "/unitList")
    @ResponseBody
    public List<TreeChildVO> getUnitList(String id){
        //条件查询所有仓库
        this.logAllRequestParams();
        if (id.equals("#")) {
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                    .getRequest());
            //插入缓存
            return warehouseService.unitTreeList(filters);
        }
        else{
            //查询节点
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                    .getRequest());
            PropertyFilter filter = new PropertyFilter("EQS_parentId", id);
            filters.add(filter);
            List<TreeChildVO> result = new ArrayList<>();
            List<RepositoryManagement> repositoryManagements=repositoryManagementService.find(filters);
            for (RepositoryManagement repositoryManagement:repositoryManagements){
                TreeChildVO treeChildVO = new TreeChildVO();
                treeChildVO.setId(repositoryManagement.getId());
                treeChildVO.setChildren(true);
                treeChildVO.setState(new State(false));
                treeChildVO.setText(repositoryManagement.getName());
                treeChildVO.setDeep(repositoryManagement.getDeep());
                if (Constant.RepositoryType.allocation.equals(repositoryManagement.getDeep())){
                    treeChildVO.setChildren(false);
                }
                result.add(treeChildVO);
            }
            return result;
        }
    }
    @RequestMapping(value = "/unitListById")
    @ResponseBody
    public List<TreeVO> getUnitListById(String id){
        //条件查询所有仓库
        this.logAllRequestParams();
        //查询节点
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_parentId", id);
        filters.add(filter);
        List<TreeVO> result = new ArrayList<TreeVO>();
        List<RepositoryManagement> repositoryManagements=repositoryManagementService.find(filters);
        for (RepositoryManagement repositoryManagement:repositoryManagements){
            TreeVO treeVO = new TreeVO();
            treeVO.setId(repositoryManagement.getId());
            treeVO.setState(new State(true));
            treeVO.setText(repositoryManagement.getName());
            treeVO.setDeep(repositoryManagement.getDeep());
            //查询子节点
            filters.remove(filter);
            filter = new PropertyFilter("EQS_parentId", repositoryManagement.getId());
            List<TreeVO> child = new ArrayList<>();
            filters.add(filter);
            List<RepositoryManagement> rm=repositoryManagementService.find(filters);
            if (rm.size() > 0){
                for (RepositoryManagement rm1:rm){
                    TreeVO childVO = new TreeVO();
                    childVO.setId(rm1.getId());
                    childVO.setState(new State(true));
                    childVO.setText(rm1.getName());
                    childVO.setDeep(rm1.getDeep());
                    //查询子节点
                    filters.remove(filter);
                    filter = new PropertyFilter("EQS_parentId", rm1.getId());
                    filters.add(filter);
                    List<TreeVO> child1 = new ArrayList<>();
                    List<RepositoryManagement> rm2=repositoryManagementService.find(filters);
                    if(rm2.size() > 0){
                        for(RepositoryManagement rm3:rm2){
                            TreeVO childVO1 = new TreeVO();
                            childVO1.setId(rm3.getId());
                            childVO1.setState(new State(true));
                            childVO1.setText(rm3.getName());
                            childVO1.setDeep(rm3.getDeep());
                            child1.add(childVO1);
                        }
                    }
                    childVO.setChildren(child1);
                    child.add(childVO);
                }
            }
            treeVO.setChildren(child);
            result.add(treeVO);
        }
        return result;
    }

    /**
     *
     * @param id 仓库id
     * @return 仓库下的子节点json
     * {"result":[{"children":true,"deep":"1","id":"AUTO_WH001-1","state":{"opened":false},"text":"1号货架"},{"children":true,"deep":"1","id":"AUTO_WH001-2","state":{"opened":false},"text":"2号货架"}],"statusCode":"000","success":true}
     * @throws Exception
     */
    @RequestMapping(value ={"getRmByUnitWS","getRmByUnit"})
    @ResponseBody
    public MessageBox getRmByUnit(String id) throws Exception {
        MessageBox ms = new MessageBox();
        try{
            List<TreeChildVO> voList = this.getUnitList(id);
            if(voList.size() > 0){
                ms.setResult(voList);
                ms.setSuccess(true);
            }
            else {
                ms.setMsg("此仓库下没有库位！");
            }
        }catch (Exception e){
            ms.setSuccess(false);
            ms.setMsg("请求失败!");
            e.printStackTrace();
            return ms;
        }
        return ms;
    }
    @RequestMapping(value = "findbysku")
    @ResponseBody
    public Page<sku> findbysku(Page<sku> page,String rmId){
        String wareId = null;
        String rackId =null;
        String levelId = null;
        String allocationId =null;
        String rackName = null;
        String areaName = null;
        String allocationName = null;
        int count =0;
        int pageNo = 0;
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        if (CommonUtil.isNotBlank(rmId)){
            String []rm = rmId.split("-");
            if (rm.length == 1){
                wareId = rm[0];
            }
            else if(rm.length ==2){
                rackId = rm[0]+"-"+rm[1];
            }
            else if(rm.length ==3){
                levelId = rm[0]+"-"+rm[1]+"-"+rm[2];
            }
            else{
                allocationId = rmId;
            }
        }
        //获取字符串中的数字
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Page<sku> skus = epcStockService.findskuByRm(wareId,rackId,levelId,allocationId,page);
        List<sku>  skuList= page.getRows();
        for(sku sku:skuList){
            if(CommonUtil.isNotBlank(sku.getFloorAllocation())){
                Matcher m = p.matcher(sku.getFloorRack().split("-")[1]);
                Matcher m1 = p.matcher(sku.getFloorArea().split("-")[2]);
                Matcher m2 = p.matcher(sku.getFloorAllocation().split("-")[3]);
                rackName = m.replaceAll("").trim()+"号货架";
                areaName = m1.replaceAll("").trim()+"号货层";
                allocationName = m2.replaceAll("").trim()+"号货位";
            }
            sku.setRackName(rackName);
            sku.setAreaName(areaName);
            sku.setAllocationName(allocationName);
        }
        page.setRows(skuList);
        /*count = epcStockService.count(wareId,rackId,levelId,allocationId);
        pageNo = count/page.getPageSize();
        page.setTotPage(pageNo);
        page.setTotal(count);
        page.setRows(skus);*/
        return page;
    }
    @RequestMapping(value = "findbycode")
    @ResponseBody
    public Page<code> findbycode(Page<code> page, String rmId){
        String wareId = null;
        String rackId =null;
        String levelId = null;
        String allocationId =null;
        String rackName = null;
        String areaName = null;
        String allocationName = null;
        int count =0;
        int pageNo = 0;
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        if (CommonUtil.isNotBlank(rmId)){
            String []rm = rmId.split("-");
            if (rm.length == 1){
                wareId = rm[0];
            }
            else if(rm.length ==2){
                rackId = rm[0]+"-"+rm[1];
            }
            else if(rm.length ==3){
                levelId = rm[0]+"-"+rm[1]+"-"+rm[2];
            }
            else{
                allocationId = rmId;
            }
        }
        //获取字符串中的数字
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Page<code> codes = epcStockService.findcodeByRm(wareId,rackId,levelId,allocationId,page);
        List<code>  codeList= page.getRows();
        for(code code:codeList){
            if(CommonUtil.isNotBlank(code.getFloorAllocation())){
                Matcher m = p.matcher(code.getFloorRack().split("-")[1]);
                Matcher m1 = p.matcher(code.getFloorArea().split("-")[2]);
                Matcher m2 = p.matcher(code.getFloorAllocation().split("-")[3]);
                rackName = m.replaceAll("").trim()+"号货架";
                areaName = m1.replaceAll("").trim()+"号货层";
                allocationName = m2.replaceAll("").trim()+"号货位";
            }
            code.setRackName(rackName);
            code.setAreaName(areaName);
            code.setAllocationName(allocationName);
        }
        page.setRows(codeList);
        /*count = epcStockService.count(wareId,rackId,levelId,allocationId);
        pageNo = count/page.getPageSize();
        page.setTotPage(pageNo);
        page.setTotal(count);
        page.setRows(skus);*/
        return page;
    }
    @RequestMapping(value = "findbystyle")
    @ResponseBody
    public Page<styled> findbystyle(Page<styled> page, String rmId){
        String wareId = null;
        String rackId =null;
        String levelId = null;
        String allocationId =null;
        String rackName = null;
        String areaName = null;
        String allocationName = null;
        int count =0;
        int pageNo = 0;
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        if (CommonUtil.isNotBlank(rmId)){
            String []rm = rmId.split("-");
            if (rm.length == 1){
                wareId = rm[0];
            }
            else if(rm.length ==2){
                rackId = rm[0]+"-"+rm[1];
            }
            else if(rm.length ==3){
                levelId = rm[0]+"-"+rm[1]+"-"+rm[2];
            }
            else{
                allocationId = rmId;
            }
        }
        //获取字符串中的数字
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Page<styled> styleds = epcStockService.findstyledByRm(wareId,rackId,levelId,allocationId,page);
        List<styled>  styledList= page.getRows();
        for(styled styled:styledList){
            if(CommonUtil.isNotBlank(styled.getFloorAllocation())){
                Matcher m = p.matcher(styled.getFloorRack().split("-")[1]);
                Matcher m1 = p.matcher(styled.getFloorArea().split("-")[2]);
                Matcher m2 = p.matcher(styled.getFloorAllocation().split("-")[3]);
                rackName = m.replaceAll("").trim()+"号货架";
                areaName = m1.replaceAll("").trim()+"号货层";
                allocationName = m2.replaceAll("").trim()+"号货位";
            }
            styled.setRackName(rackName);
            styled.setAreaName(areaName);
            styled.setAllocationName(allocationName);
        }
        page.setRows(styledList);
        /*count = epcStockService.count(wareId,rackId,levelId,allocationId);
        pageNo = count/page.getPageSize();
        page.setTotPage(pageNo);
        page.setTotal(count);
        page.setRows(skus);*/
        return page;
    }
}
