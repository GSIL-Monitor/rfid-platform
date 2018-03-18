package com.casesoft.dmc.controller.sys;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.SidebarMenu;
import com.casesoft.dmc.model.sys.RoleRes;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.sys.impl.ResourceService;
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
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.Role;
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

	
	
	
	

	
}
