package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.tag.util.StringUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.cfg.VO.State;
import com.casesoft.dmc.model.rem.RepositoryManagement;
import com.casesoft.dmc.model.rem.VO.TreeChildVO;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.rem.RepositoryManagementService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String maxCode = Constant.UnitCodePrefix.Warehouse+this.getSuffix();
        RepositoryManagement repositoryManagement = new RepositoryManagement();
        repositoryManagement.setId(maxCode);
        repositoryManagement.setParentId(entity.getOwnerId());
        repositoryManagement.setRoot(false);
        repositoryManagement.setRemLevelType(Constant.RepositoryType.Company);
        repositoryManagement.setRelatedId(maxCode);
        repositoryManagement.setRepath(entity.getDeep());
        if(!StringUtil.isEmpty(entity.getRackId())){
            repositoryManagement.setName(entity.getRackId());
            repositoryManagement.setTreePath(entity.getOwnerId()+">"+maxCode+">");
        }
        if(!StringUtil.isEmpty(entity.getLevelId()) || !StringUtil.isEmpty(entity.getAllocationId())){
            //接上父节点的repath
            RepositoryManagement rm = repositoryManagementService.get("id",entity.getOwnerId());
            repositoryManagement.setTreePath(rm.getTreePath() + ">" + maxCode + ">");
            if(!StringUtil.isEmpty(entity.getLevelId())){
                repositoryManagement.setName(entity.getLevelId());
            }
            if(!StringUtil.isEmpty(entity.getAllocationId())){
                repositoryManagement.setName(entity.getRackId());
            }
        }
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
    private String getSuffix(){
        String suffix = Math.round(Math.random()*1000)+"";
        return suffix;
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

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/sys/repositoryManagement";
    }

    @RequestMapping(value = "/unitList")
    @ResponseBody
    public List<TreeChildVO> getUnitList(){
        //条件查询所有仓库
        this.logAllRequestParams();
        String id = request.getParameter("id");
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
                treeChildVO.setDeep(Constant.RepositoryType.rack);
                result.add(treeChildVO);
            }
            return result;
        }
    }



}
