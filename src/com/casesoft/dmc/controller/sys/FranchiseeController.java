package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/20.
 */
@Controller
@RequestMapping("/sys/franchisee")
public class FranchiseeController extends BaseController implements IBaseInfoController<Unit> {
    @Autowired
    private AgentService agentService;

    @RequestMapping(value = "/checkUniqueCode")
    @ResponseBody
    public Map<String, Boolean> checkUniqueCode(String code, String pageType) throws Exception {
        code = code.toUpperCase();
        Unit agent = this.agentService.findUnique(code);
        Map<String, Boolean> json = new HashMap<>();
        if (CommonUtil.isNotBlank(agent) && "add".equals(pageType)) {
            json.put("valid", false);
        } else {
            json.put("valid", true);
        }
        return json;
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.agentService.findPage(page, filters);
        Map<String, String> propertyKeyMap = new HashMap<String, String>();
        for (PropertyKey propertyKey : CacheManager.getPropertyKeyMap().values()) {
            if ("AT".equals(propertyKey.getType()))
                propertyKeyMap.put(propertyKey.getCode(), propertyKey.getName());
        }

        for (Unit u : page.getRows()) {
            u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());
            if (CommonUtil.isNotBlank(propertyKeyMap.get(u.getGroupId())))
                u.setGroupName(propertyKeyMap.get(u.getGroupId()));
        }
        return page;
    }

    @Override
    public List<Unit> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit unit) throws Exception {
        this.logAllRequestParams();
        Unit u = this.agentService.findUnique(unit.getCode());

        User sessionUser = this.getCurrentUser();
        if (CommonUtil.isBlank(u)) {
            u = new Unit();
            if (CommonUtil.isBlank(unit.getCode())) {
                String code = this.agentService.findMaxCodes(Constant.UnitType.Franchisee);
                u.setCode(code);
            } else {
                u.setCode(unit.getCode());
            }
            u.setId(u.getCode());
            u.setCreatorId(sessionUser.getCode());
            u.setCreateTime(new Date());
        }
        u.setName(unit.getName());
        u.setGroupId(unit.getGroupId());
        u.setOwnerId(unit.getOwnerId());
        u.setTel(unit.getTel());
        u.setLinkman(unit.getLinkman());
        u.setRemark(unit.getRemark());
        u.setType(Constant.UnitType.Franchisee);
        u.setUpdateTime(new Date());
        u.setUpdaterId(sessionUser.getCode());
        u.setSrc(Constant.DataSrc.SYS);

        this.agentService.save(u);

        return this.returnSuccessInfo("保存成功");
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
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/Franchisee";
    }
}
