package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.hall.HallRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017/3/14 0014.
 */
@Controller
@RequestMapping(value = "/hall/room")
public class HallRoomController extends BaseController implements IBaseInfoController<Unit>{

	@Autowired
	private HallRoomService hallRoomService;


	@RequestMapping(value = "/index")
	@Override
	public String index() {
		return "/views/hall/hallRoomInfo";
	}

	@RequestMapping(value = "/page")
	@ResponseBody
	@Override
	public Page<Unit> findPage(Page<Unit> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		filters.add(new PropertyFilter("EQI_type","6"));
		page.setPageProperty();
		page=this.hallRoomService.findPage(page,filters);
		for(Unit hallRoom:page.getRows()){
			if(CommonUtil.isNotBlank(hallRoom.getOwnerId())){
				hallRoom.setUnitName(CacheManager.getUnitById(hallRoom.getOwnerId()).getName());
			}
		}
		return page;
	}

	@RequestMapping("/list")
	@ResponseBody
	@Override
	public List<Unit> list() throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		filters.add(new PropertyFilter("EQI_type","6"));
		List<Unit> list =this.hallRoomService.find(filters);
		return list;
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	@Override
	public MessageBox save(Unit hallRoom) throws Exception {
		Unit hroom=this.hallRoomService.findRoomByCode(hallRoom.getCode());
		if(CommonUtil.isBlank(hroom)){
			hroom=new Unit();
			String code =this.hallRoomService.findMaxCode();
			hroom.setCode(code);
			hroom.setId(code);
			hroom.setCreatorId(this.getCurrentUser().getOwnerId());
			hroom.setCreateTime(new Date());
			hroom.setOwnerId(hallRoom.getOwnerId());
			hroom.setType(6);
		}
		hroom.setName(hallRoom.getName());
		hroom.setTel(hallRoom.getTel());
		hroom.setLinkman(hallRoom.getLinkman());
		hroom.setEmail(hallRoom.getEmail());
		hroom.setRemark(hallRoom.getRemark());
		hroom.setUpdaterId(this.getCurrentUser().getOwnerId());
		hroom.setUpdateTime(new Date());

		try{
			this.hallRoomService.save(hroom);
			CacheManager.refreshUnitCache();
			return returnSuccessInfo("保存成功");
		} catch(Exception e){
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

}
