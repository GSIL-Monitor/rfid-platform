package com.casesoft.dmc.controller.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.SidebarMenu;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Role;
import com.casesoft.dmc.model.sys.RoleRes;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController implements IBaseInfoController<Role>{
	
	@RequestMapping(value ="/index")
	@Override
	public String index() {
		return "/views/sys/role";
	}
	
	
	@Autowired
	private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;
	
	
	
	@RequestMapping(value="/page")
    @ResponseBody
	@Override
	public Page<Role> findPage(Page<Role> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters  =PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();
		page = this.roleService.findPage(page, filters);	
		
		RoleUtil.convertToVo(page.getRows());
		return page;
	
	}

	@RequestMapping(value="/findResource")
    @ResponseBody
	public List<Resource> findResource(String roleId,String pageType) throws Exception {
		this.logAllRequestParams();
        List<Resource> resourceList = this.resourceService.getSelectedResourceList();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (CommonUtil.isNotBlank(pageType) && pageType.equals("add")) {
            PropertyFilter filter = new PropertyFilter("EQS_roleId", "0");
            filters.add(filter);
        }
        if (CommonUtil.isNotBlank(pageType) && pageType.equals("edit")) {
            PropertyFilter filter = new PropertyFilter("EQS_roleId", roleId);
            filters.add(filter);
        }
        List<ResourcePrivilege> allResourcePrivilege = this.resourcePrivilegeService.find(filters);
            //根据code分组Button
        if (CommonUtil.isNotBlank(allResourcePrivilege) && allResourcePrivilege.size() != 0) {
            for (Resource resource : resourceList) {
                for (ResourcePrivilege resourcePrivilege : allResourcePrivilege) {
                    if(resourcePrivilege.getType().equals("button")) {
                        if (resource.getCode().equals(resourcePrivilege.getCode())) {
                            if (CommonUtil.isNotBlank(resource.getResourcePrivilegeList()) && resource.getResourcePrivilegeList().size() != 0) {
                                List<ResourcePrivilege> resourcePrivilegeList = resource.getResourcePrivilegeList();
                                resourcePrivilegeList.add(resourcePrivilege);
                                resource.setResourcePrivilegeList(resourcePrivilegeList);
                            } else {
                                List<ResourcePrivilege> resourcePrivilegeList = new ArrayList<ResourcePrivilege>();
                                resourcePrivilegeList.add(resourcePrivilege);
                                resource.setResourcePrivilegeList(resourcePrivilegeList);
                            }
                        }
                    }else if(resourcePrivilege.getType().equals("table")){
                        if (resource.getCode().equals(resourcePrivilege.getCode())) {
                            if (CommonUtil.isNotBlank(resource.getResourcetableList()) && resource.getResourcetableList().size() != 0) {
                                List<ResourcePrivilege> resourcetableList = resource.getResourcetableList();
                                resourcetableList.add(resourcePrivilege);
                                resource.setResourcetableList(resourcetableList);
                            } else {
                                List<ResourcePrivilege> resourcetableList = new ArrayList<ResourcePrivilege>();
                                resourcetableList.add(resourcePrivilege);
                                resource.setResourcetableList(resourcetableList);
                            }
                        }
                    }else if(resourcePrivilege.getType().equals("div")){
                        if (resource.getCode().equals(resourcePrivilege.getCode())) {
                            if (CommonUtil.isNotBlank(resource.getResourceDivList()) && resource.getResourceDivList().size() != 0) {
                                List<ResourcePrivilege> resourceDivList = resource.getResourceDivList();
                                resourceDivList.add(resourcePrivilege);
                                resource.setResourceDivList(resourceDivList);
                            } else {
                                List<ResourcePrivilege> resourceDivList = new ArrayList<ResourcePrivilege>();
                                resourceDivList.add(resourcePrivilege);
                                resource.setResourceDivList(resourceDivList);
                            }
                        }
                    }
                }
            }
        }


        if(CommonUtil.isBlank(roleId)) {
            RoleUtil.convertToTreeGrid(resourceList);
        } else {
            List<RoleRes> rrList = CacheManager.getAuthByRoleId(roleId);
            if(CommonUtil.isNotBlank(rrList)) {
	            RoleUtil.convertToTreeGrid(resourceList);
	            for(RoleRes rr : rrList) {
	                for(Resource res : resourceList) {
	                    if(rr.getResId().equals(res.getCode())) {
	                        res.setChecked(true);
	                        break;
	                    }
	                }
	            }
            }
            
        }
        Collections.sort(resourceList, new MenuIdComparator());//对子菜单进行排序
		return resourceList;
	
	}


	
	@RequestMapping(value = "/list")
    @ResponseBody
	@Override
	public List<Role> list() throws Exception {
        List<PropertyFilter> filters  =PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Role> list=this.roleService.find(filters);
		return list;
	}
    @RequestMapping(value="/save")
    @ResponseBody
	@Override
	public MessageBox save(Role role) throws Exception {
		this.logAllRequestParams();
		ArrayList<ResourcePrivilege> saveList=new ArrayList<ResourcePrivilege>();
        if(CommonUtil.isBlank(role.getId())) {
            role.setId(role.getCode());
            role.setCreatorId(this.getCurrentUser().getId());
            role.setCreateTime(new Date());
            role.setOwnerId(this.getCurrentUser().getOwnerId());
            //添加按钮
            List<PropertyFilter> filters  =new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_roleId", "0");
            filters.add(filter);
            List<ResourcePrivilege> allResourcePrivilege = this.resourcePrivilegeService.find(filters);
            for(ResourcePrivilege resourcePrivilege : allResourcePrivilege){
                ResourcePrivilege newresourcePrivilege =new ResourcePrivilege();
                BeanUtils.copyProperties(resourcePrivilege, newresourcePrivilege);

                newresourcePrivilege.setId(resourcePrivilege.getCode()+"-"+ resourcePrivilege.getPrivilegeId()+"-"+role.getId()+"-"+resourcePrivilege.getType());
                newresourcePrivilege.setRoleId(role.getId());
                newresourcePrivilege.setIsShow(1);
                newresourcePrivilege.setType(resourcePrivilege.getType());


                saveList.add(newresourcePrivilege);
            }


        } else {
            Role dto = this.roleService.getRole(role.getId());
            dto.setName(role.getName());
            dto.setRemark(role.getRemark());
            role = dto;
        }

        this.roleService.saveOrUpdateAndList(role,saveList);
        return this.returnSuccessInfo("保存成功", role);
	}
	@Override
	public MessageBox edit(String roleId) throws Exception {

		return null;
	}

    @RequestMapping(value ="/editPage")
    public ModelAndView editPage(String roleId) throws Exception {
        this.logAllRequestParams();
        Role role = this.roleService.getRole(roleId);
        ModelAndView mv = new ModelAndView("/views/sys/role_edit");
        mv.addObject("pageType","edit");
        mv.addObject("role",role);
        return mv;
    }

    @RequestMapping(value ="/addPage")
    public ModelAndView addPage() throws Exception {
        ModelAndView mv = new ModelAndView("/views/sys/role_edit");
        mv.addObject("pageType","add");
        mv.addObject("role",new Role());
        return mv;
    }

    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public Map<String,Boolean> checkUniqueCode(String code,String pageType) throws Exception {
        this.logAllRequestParams();
        Role role = this.roleService.findUnique(code);
        Map<String,Boolean> json = new HashMap<>();
        if(pageType=="add"&&CommonUtil.isNotBlank(role)){
            json.put("valid",false);
        }else{
            json.put("valid",true);
        }
        return json;
    }

    @RequestMapping(value="/addAuth")
    @ResponseBody
    public MessageBox addAuth(RoleRes roleRes) throws Exception {
        this.logAllRequestParams();
        roleRes.setId(roleRes.getRoleId()+"-"+roleRes.getResId());
        roleRes.setCreateTime(new Date());
        roleRes.setCreatorId(this.getCurrentUser().getId());
        this.roleService.saveRoleRes(roleRes);
        CacheManager.refreshAuthCache();
        return this.returnSuccessInfo("保存成功");

    }

    @RequestMapping(value="/deleteAuth")
    @ResponseBody
    public MessageBox deleteAuth(RoleRes roleRes) throws Exception {
        this.logAllRequestParams();
        this.roleService.deleteRoleRes(roleRes);
        CacheManager.refreshAuthCache();
        return this.returnSuccessInfo("删除成功");

    }

    /**
     * 通过角色ID获取权限
     *
     * @return
     */
    @RequestMapping(value="/showSidebarMenu")
    @ResponseBody
    public List<SidebarMenu> showSidebarMenu(String roleId) {
        this.logAllRequestParams();
        List<Resource> resourceList = this.resourceService.getResourceByRole(roleId);
        List<SidebarMenu> tree = ResourceUtil.convertToSidebarMenuVo(resourceList);
        return tree;
    }
	@Override
	public MessageBox delete(String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportExcel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageBox importExcel(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     *
     * @param roleStr 前端传的数据
     * @param pageType 判断是add（）还是edit（）
     * @return liu tianci
     * @throws Exception
     */

    @RequestMapping(value="/powerSave")
    @ResponseBody
    public MessageBox powerSave(String roleStr,String pageType) throws Exception {
        this.logAllRequestParams();
        Resource resource = JSON.parseObject(roleStr,Resource.class);
        Resource rs = this.resourceService.get("code",resource.getCode());
        String maxCode = this.resourceService.findMaxByCode(resource.getOwnerId());
        int maxSeqNo = this.resourceService.findMaxBySeqNo(resource.getOwnerId());
            if (CommonUtil.isBlank(rs)) {
                rs = new Resource();
                rs.setCode(maxCode);
                rs.setEname(resource.getEname());
                rs.setIconCls(resource.getIconCls());
                rs.setImage(resource.getImage());
                rs.setLocked(resource.getLocked());
                rs.setName(resource.getName());
                rs.setOwnerId(resource.getOwnerId());
                rs.setSeqNo(maxSeqNo);
                rs.setStatus(resource.getStatus());
                rs.setUrl(resource.getUrl());
                rs.setClientCode(resource.getClientCode());
                rs.setClientName(resource.getClientName());
                rs.setWxUrl(resource.getWxUrl());
            }else {
                if (rs.getOwnerId().equals("01")||rs.getOwnerId().equals(resource.getOwnerId())){
                    rs.setCode(resource.getCode());
                    rs.setSeqNo(resource.getSeqNo());
                }else {
                    rs.setCode(maxCode);
                    rs.setSeqNo(maxSeqNo);
                }
                rs.setEname(resource.getEname());
                rs.setIconCls(resource.getIconCls());
                rs.setImage(resource.getImage());
                rs.setLocked(resource.getLocked());
                rs.setName(resource.getName());
                rs.setOwnerId(resource.getOwnerId());

                rs.setStatus(resource.getStatus());
                rs.setUrl(resource.getUrl());
                rs.setClientCode(resource.getClientCode());
                rs.setClientName(resource.getClientName());
                rs.setWxUrl(resource.getWxUrl());
            }
        try {
            if (resource.getCode().equals("")){
                this.resourceService.save(rs);
            }else {
                this.resourceService.deleteAndSave(resource.getCode(),rs);
            }
            return this.returnSuccessInfo("保存成功");
        }catch (Exception e){
            e.printStackTrace();
            this.logger.error(e.getMessage());
            return this.returnFailInfo("保存失败");
        }
    }
    @RequestMapping(value = "/searchByOwnerId")
    @ResponseBody
    public List<Resource> searchByOwnerId(String ownerId)throws Exception{
        this.logAllRequestParams();
        List<Resource> resourceList = this.resourceService.getResourceKeyByOwnerId(ownerId);
        return resourceList;
    }
    @RequestMapping(value = "/updateResourceButtonIsShow")
    @ResponseBody
    public MessageBox updateResourceButtonIsShow(String id, Integer isShow){
        try {
            ResourcePrivilege resourcePrivilege = this.resourcePrivilegeService.load(id);
            resourcePrivilege.setIsShow(isShow);
            this.resourcePrivilegeService.update(resourcePrivilege);
            return this.returnSuccessInfo("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnSuccessInfo("更新失败");
        }

    }
    @RequestMapping(value = "/checkPrivilegeId")
    @ResponseBody
    public MessageBox checkPrivilegeId(String code, String privilegeId){
        try {
            List<PropertyFilter> filters  =new ArrayList<PropertyFilter>();
            PropertyFilter filtercode = new PropertyFilter("EQS_code", code);
            PropertyFilter filterbuttonId = new PropertyFilter("EQS_privilegeId", privilegeId);
            filters.add(filtercode);
            filters.add(filterbuttonId);
            List<ResourcePrivilege> allResourcePrivilege = this.resourcePrivilegeService.find(filters);
            if(allResourcePrivilege.size()>0){
                return this.returnFailInfo("有相同的buttonId");
            }else{
                return this.returnSuccessInfo("检测成功");
            }

        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("检测失败");
        }
    }
    @RequestMapping(value = "/saveResourceButton")
    @ResponseBody
    public MessageBox saveResourceButton(String roleStr){
        try {
            ResourcePrivilege resourcePrivilege = JSON.parseObject(roleStr,ResourcePrivilege.class);
            List<Role> allRoles = this.roleService.getAllRoles();
            ArrayList<ResourcePrivilege> saveLists=new ArrayList<>();
            for(Role role:allRoles){
                ResourcePrivilege saveresourcePrivilege =new ResourcePrivilege();
                BeanUtils.copyProperties(resourcePrivilege, saveresourcePrivilege);
                saveresourcePrivilege.setId(resourcePrivilege.getCode()+"-"+ resourcePrivilege.getPrivilegeId()+"-"+role.getId()+"-button");
                saveresourcePrivilege.setIsShow(1);
                saveresourcePrivilege.setRoleId(role.getId());
                saveresourcePrivilege.setType("button");
                saveLists.add(saveresourcePrivilege);
            }
            this.resourcePrivilegeService.saveAll(saveLists);
            return this.returnSuccessInfo("保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
        }
    }

    @RequestMapping(value = "/saveResourceDiv")
    @ResponseBody
    public MessageBox saveResourceDiv(String roleStr){
        try {
            ResourcePrivilege resourcePrivilege = JSON.parseObject(roleStr,ResourcePrivilege.class);
            List<Role> allRoles = this.roleService.getAllRoles();
            ArrayList<ResourcePrivilege> saveLists=new ArrayList<>();
            for(Role role:allRoles){
                ResourcePrivilege saveresourcePrivilege =new ResourcePrivilege();
                BeanUtils.copyProperties(resourcePrivilege, saveresourcePrivilege);
                saveresourcePrivilege.setId(resourcePrivilege.getCode()+"-"+ resourcePrivilege.getPrivilegeId()+"-"+role.getId()+"-div");
                saveresourcePrivilege.setIsShow(1);
                saveresourcePrivilege.setRoleId(role.getId());
                saveresourcePrivilege.setType("div");
                saveLists.add(saveresourcePrivilege);
            }
            this.resourcePrivilegeService.saveAll(saveLists);
            return this.returnSuccessInfo("保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
        }
    }

    @RequestMapping(value = "/saveResourceTable")
    @ResponseBody
    public MessageBox saveResourceTable(String roleStr){
        try {
            ResourcePrivilege resourcePrivilege = JSON.parseObject(roleStr,ResourcePrivilege.class);
            List<Role> allRoles = this.roleService.getAllRoles();
            ArrayList<ResourcePrivilege> saveLists=new ArrayList<>();
            for(Role role:allRoles){
                ResourcePrivilege saveresourcePrivilege =new ResourcePrivilege();
                BeanUtils.copyProperties(resourcePrivilege, saveresourcePrivilege);
                saveresourcePrivilege.setId(resourcePrivilege.getCode()+"-"+ resourcePrivilege.getPrivilegeId()+"-"+role.getId()+"-table");
                saveresourcePrivilege.setIsShow(1);
                saveresourcePrivilege.setRoleId(role.getId());
                saveresourcePrivilege.setType("table");
                saveLists.add(saveresourcePrivilege);
            }
            this.resourcePrivilegeService.saveAll(saveLists);
            return this.returnSuccessInfo("保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
        }


    }



}
