package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.hall.IHallInventoryDao;
import com.casesoft.dmc.model.hall.HallInventory;
import com.casesoft.dmc.service.hall.HallInventoryDetailService;
import com.casesoft.dmc.service.hall.HallInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;

/**
 * Created by session on 2017/3/16 0016.
 */

@Controller
@RequestMapping(value = "/hall/sampleInventory")
public class HallInventoryController extends BaseController {

	@Autowired
	IHallInventoryDao iHallInventoryDao;

	@Autowired
	HallInventoryService hallInventoryService;

	@Autowired
	HallInventoryDetailService hallInventoryDetailService;

	@RequestMapping(value = "/index")
	public String index() {
		return "/views/hall/sampleInventory";
	}

	@RequestMapping(value = "/check")
	@ResponseBody
	public MessageBox check(String taskId){
		HallInventory hallInventory=this.hallInventoryService.findByTaskId(taskId);
		if(CommonUtil.isNotBlank(hallInventory)){
			hallInventory.setIsCheck("CK");
		}
		try{
			this.hallInventoryService.save(hallInventory);
			return returnSuccessInfo("审核成功");
		}catch (Exception e){
			e.printStackTrace();
			return returnFailInfo("审核失败");
		}
	}

	@RequestMapping(value = "/detail")
	@ResponseBody
	public ModelAndView showdetails(String taskId){

		HallInventory hallInventory =this.hallInventoryService.findByTaskId(taskId);
		if(CommonUtil.isNotBlank(hallInventory.getOwnerId())){
			if(CommonUtil.isNotBlank(CacheManager.getUnitByCode(hallInventory.getOwnerId()))){
				hallInventory.setOwnerName(CacheManager.getUnitByCode(hallInventory.getOwnerId()).getName());
			}
		}
		if(CommonUtil.isNotBlank(hallInventory.getFloor())){
			/*if(CommonUtil.isNotBlank(CacheManager.getFloorByCode(hallInventory.getFloor()))){
				hallInventory.setFloorName(CacheManager.getFloorByCode(hallInventory.getFloor()).getName());
			}*/
		}
		ModelAndView mav=new ModelAndView("/views/hall/sampleInventoryDetail");
		mav.addObject("HallInventory",hallInventory);

		return mav;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public
	@ResponseBody
	DataSourceResult read(@RequestBody DataSourceRequest request) {
		this.logAllRequestParams();

		DataSourceResult dataSourceResult = iHallInventoryDao.getList(request);

		if (request.getGroup().size() == 0) {
			for (HallInventory HI : (List<HallInventory>) dataSourceResult.getData()) {
				if (CommonUtil.isNotBlank(HI.getOwnerId())) {
					if (CommonUtil.isNotBlank(CacheManager.getUnitByCode(HI.getOwnerId()))) {
						HI.setOwnerName(CacheManager.getUnitByCode(HI.getOwnerId()).getName());
					} else {
						HI.setOwnerName("[" + HI.getOwnerId() + "]");
					}
				}
				if (CommonUtil.isNotBlank(HI.getFloor())) {
					/*if (CommonUtil.isNotBlank(CacheManager.getFloorByCode(HI.getFloor()))) {
						HI.setFloorName(CacheManager.getFloorByCode(HI.getFloor()).getName());
					} else {
						String floorName = "[" + HI.getFloor() + "]";
						HI.setFloorName(floorName);
					}*/
				}
			}
		}
		return dataSourceResult;
	}

	@RequestMapping(value = "/export", method = RequestMethod.POST)
	@ResponseBody
	public void export(String fileName, String base64, String contentType, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		response.setContentType(contentType);
		byte[] data = DatatypeConverter.parseBase64Binary(base64);

		response.setContentLength(data.length);
		response.getOutputStream().write(data);
		response.flushBuffer();
	}

}
