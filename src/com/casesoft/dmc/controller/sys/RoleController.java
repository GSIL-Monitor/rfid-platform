package com.casesoft.dmc.controller.sys;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.SidebarMenu;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.service.sys.ResourceButtonService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import groovy.ui.Console;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.service.sys.impl.RoleService;
import org.springframework.web.servlet.ModelAndView;

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
    private ResourceButtonService resourceButtonService;
	
	
	
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
	public List<Resource> findResource(String roleId) throws Exception {
		this.logAllRequestParams();

		List<Resource> resourceList  = this.resourceService.getSelectedResourceList();
        List<PropertyFilter> filters  =new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_roleId", roleId);
        filters.add(filter);
        List<ResourceButton> allResourceButton = this.resourceButtonService.find(filters);
        //根据code分组Button
        if(CommonUtil.isNotBlank(allResourceButton)&&allResourceButton.size()!=0){
            for(Resource resource:resourceList){
                for(ResourceButton resourceButton:allResourceButton){
                    if(resource.getCode().equals(resourceButton.getCode())){
                        if(CommonUtil.isNotBlank(resource.getResourceButtonList())&&resource.getResourceButtonList().size()!=0){
                            List<ResourceButton> resourceButtonList = resource.getResourceButtonList();
                            resourceButtonList.add(resourceButton);
                            resource.setResourceButtonList(resourceButtonList);
                        }else{
                            List<ResourceButton> resourceButtonList=new ArrayList<ResourceButton>();
                            resourceButtonList.add(resourceButton);
                            resource.setResourceButtonList(resourceButtonList);
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
        if(CommonUtil.isBlank(role.getId())) {
            role.setId(role.getCode());
            role.setCreatorId(this.getCurrentUser().getId());
            role.setCreateTime(new Date());
            role.setOwnerId(this.getCurrentUser().getOwnerId());
        } else {
            Role dto = this.roleService.getRole(role.getId());
            dto.setName(role.getName());
            dto.setRemark(role.getRemark());
            role = dto;
        }
        this.roleService.saveOrUpdate(role);

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
    @RequestMapping(value = "/updateResourceButtonIsshow")
    @ResponseBody
    public MessageBox updateResourceButtonIsshow(String id,Integer ishow){
        try {
            ResourceButton resourceButton = this.resourceButtonService.load(id);
            resourceButton.setIshow(ishow);
            this.resourceButtonService.update(resourceButton);
            return this.returnSuccessInfo("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnSuccessInfo("更新失败");
        }

    }
    @RequestMapping(value = "/checkButtonId")
    @ResponseBody
    public MessageBox checkButtonId(String code,String buttonId){
        try {
            List<PropertyFilter> filters  =new ArrayList<PropertyFilter>();
            PropertyFilter filtercode = new PropertyFilter("EQS_code", code);
            PropertyFilter filterbuttonId = new PropertyFilter("EQS_buttonId", buttonId);
            filters.add(filtercode);
            filters.add(filterbuttonId);
            List<ResourceButton> allResourceButton = this.resourceButtonService.find(filters);
            if(allResourceButton.size()>0){
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
            ResourceButton resourceButton = JSON.parseObject(roleStr,ResourceButton.class);
            List<Role> allRoles = this.roleService.getAllRoles();
            ArrayList<ResourceButton> saveLists=new ArrayList<>();
            for(Role role:allRoles){
                ResourceButton saveresourceButton=new ResourceButton();
                BeanUtils.copyProperties(resourceButton,saveresourceButton);
                saveresourceButton.setId(resourceButton.getCode()+"-"+resourceButton.getButtonId()+"-"+role.getId());
                saveresourceButton.setIshow(1);
                saveresourceButton.setRoleId(role.getId());
                saveLists.add(saveresourceButton);
            }
            this.resourceButtonService.saveAll(saveLists);
            return this.returnFailInfo("保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
        }


    }



}
