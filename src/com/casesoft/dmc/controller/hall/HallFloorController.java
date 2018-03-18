package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.hall.HallFloor;
import com.casesoft.dmc.service.hall.HallFloorService;
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

@RequestMapping("/hall/floor")
@Controller
public class HallFloorController extends BaseController implements IBaseInfoController<HallFloor> {

	@Autowired
	private HallFloorService hallFloorService;

	@RequestMapping("/index")
	@Override
	public String index() {
		return "/views/hall/hallFloor";
	}


	@RequestMapping(value = "/page")
	@ResponseBody
	@Override
	public Page<HallFloor> findPage(Page<HallFloor> page) throws Exception {
		this.logAllRequestParams();

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();
		page = this.hallFloorService.findPage(page, filters);
		for (HallFloor hf : page.getRows()) {
			if (CommonUtil.isNotBlank(CacheManager.getUnitById(hf.getOwnerId())) && "A".equals(hf.getAreaId())) {
				hf.setUnitName(CacheManager.getUnitById(hf.getOwnerId()).getName());
			}
		}
		return page;
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	@Override
	public List<HallFloor> list() throws Exception {
		this.logAllRequestParams();

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

		List<HallFloor> list = this.hallFloorService.find(filters);
		return list;
	}

	@RequestMapping("/save")
	@ResponseBody
	@Override
	public  synchronized MessageBox save(HallFloor entity) throws Exception {
		Boolean defaultStatusChange=false;//用于标记获取的默认分区是否更改或存在
		Boolean defaultAreaChange=false;
		HallFloor defaultFloor = this.hallFloorService.findDefaultFloor(entity.getOwnerId());
		HallFloor defaultArea =this.hallFloorService.findDefaultArea(entity.getOwnerId());
		HallFloor hallFloor = this.hallFloorService.findHallFloorByCode(entity.getCode());
		if (CommonUtil.isBlank(hallFloor)) {
			String code = this.hallFloorService.findMaxCode(entity.getAreaId(), entity.getOwnerId());
			hallFloor = new HallFloor();
			hallFloor.setCode(code);
			hallFloor.setCreateTime(new Date());
			hallFloor.setCreator(this.getCurrentUser().getOwnerId());
			hallFloor.setAreaId(entity.getAreaId());
		}
		hallFloor.setAsDefault(entity.getAsDefault());
		hallFloor.setOwnerId(entity.getOwnerId());
		hallFloor.setRemark(entity.getRemark());
		hallFloor.setName(entity.getName());
		hallFloor.setStatus(entity.getStatus());
		hallFloor.setUpdateTime(new Date());
		hallFloor.setUpdater(this.getCurrentUser().getOwnerId());

		if("A".equals(hallFloor.getAreaId())) {
			if (hallFloor.getAsDefault() == 1) {
				if (hallFloor.getStatus() == 1) {
					if (CommonUtil.isNotBlank(defaultArea)) {
						if (hallFloor.getCode() != defaultArea.getCode()) {
							defaultStatusChange = true;
							defaultArea.setAsDefault(0);
						}
					}
				} else {
					hallFloor.setAsDefault(0);
				}
			}
		}else if("E".equals(hallFloor.getAreaId())) {
			if (hallFloor.getAsDefault() == 1) {
				if (hallFloor.getStatus() == 1) {
					if (CommonUtil.isNotBlank(defaultFloor)) {
						if (hallFloor.getCode() != defaultFloor.getCode()) {
							defaultAreaChange = true;
							defaultFloor.setAsDefault(0);
						}
					}
				} else {
					hallFloor.setAsDefault(0);
				}
			}
		}
		try {
			if(defaultStatusChange&&"A".equals(hallFloor.getAreaId())) {
				this.hallFloorService.update(defaultArea);//确保默认分区的唯一性
			}else if(defaultAreaChange&&"E".equals(hallFloor.getAreaId())) {
				this.hallFloorService.update(defaultFloor);//确保默认库位的唯一性
			}
			this.hallFloorService.save(hallFloor);
			CacheManager.refreshHallFloor();
			return returnSuccessInfo("保存成功");
		} catch (Exception e) {
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
