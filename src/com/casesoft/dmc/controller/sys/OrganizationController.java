package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.VO.TreeVO;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.OrganizationService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2018/5/17.
 */
@Controller
@RequestMapping("/sys/organizationController")
public class OrganizationController extends BaseController implements IBaseInfoController<Unit> {

    @Autowired
    private UnitService unitService;
    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/sys/organization";
    }

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
        Unit organization = unitService.getunitbyId(entity.getCode());
        if (CommonUtil.isBlank(organization)) {
            organization = new Unit();
            String maxCode = this.unitService.findMaxCode(Constant.UnitType.Organization);
            organization.setId(maxCode);
            organization.setCode(maxCode);
            organization.setCreatorId(currentUser.getCode());
            organization.setCreateTime(new Date());
            organization.setSrc(Constant.DataSrc.SYS);
            organization.setType(Constant.UnitType.Organization);
            organization.setLocked(0);
        }
        organization.setName(entity.getName());
        organization.setOwnerId(entity.getOwnerId());
        organization.setTel(entity.getTel());
        organization.setLinkman(entity.getLinkman());
        organization.setEmail(entity.getEmail());
        organization.setProvinceId(entity.getProvinceId());
        organization.setCityId(entity.getCityId());
        organization.setAddress(entity.getAddress());
        organization.setRemark(entity.getRemark());
        try {
            this.organizationService.save(organization);
            CacheManager.refreshUnitCache();
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

    @RequestMapping(value = "/listOrganizationTree")
    @ResponseBody
    public List<TreeVO> listOrganizationTree() {
        return this.organizationService.listOrganizationTree();
    }

    @RequestMapping(value = "/findOrganization")
    @ResponseBody
    public MessageBox findOrganization(String unitId) {
        try {
            Unit unit = this.unitService.get("id", unitId);
            Unit owner = CacheManager.getUnitByCode(unit.getOwnerId());
            if (CommonUtil.isNotBlank(owner)) {
                unit.setUnitName(owner.getName());
            }
            return this.returnSuccessInfo("查询成功", unit);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("查询失败");
        }
    }

    @RequestMapping(value = "/move")
    @ResponseBody
    public MessageBox move(String id, String parentId, String position, String sourceParentId, String sourcePosition) {
        try {
            this.organizationService.move(id, parentId, position, sourceParentId, sourcePosition);
            return this.returnSuccessInfo("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("操作失败");
        }
    }
}