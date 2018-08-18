package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.hall.IHallTaskDetailViewDao;
import com.casesoft.dmc.model.hall.HallTaskDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;

/**
 * Created by session on 2017/3/17 0017.
 */

@Controller
@RequestMapping(value = "/hall/borrowBack")
public class BorrowBackController extends BaseController {

	@Autowired
	IHallTaskDetailViewDao iHallTaskDetailViewDao;

	@RequestMapping(value = "/index")
	@Override
	public String index() {
		return "/views/hall/sampleBorrowBack";
	}


	@RequestMapping("/list")
	public
	@ResponseBody
	DataSourceResult read(@RequestBody DataSourceRequest request) {
		this.logAllRequestParams();

		DataSourceResult dataSourceResult = iHallTaskDetailViewDao.getList(request);

		if (request.getGroup().size() == 0) {
			for (HallTaskDetailView htdv : (List<HallTaskDetailView>) dataSourceResult.getData()) {
				if (CommonUtil.isNotBlank(CacheManager.getUnitById(htdv.getOwnerId()))) {
					htdv.setUnitName(CacheManager.getUnitById(htdv.getOwnerId()).getName());
				} else {
					htdv.setUnitName("[" + htdv.getOwnerId() + "]");
				}

			/*	if (CommonUtil.isNotBlank(CacheManager.getUnitById(htdv.getCustomerId()))) {
					htdv.setCustomerName(CacheManager.getUnitById(htdv.getCustomerId()).getName());
				} else if (CommonUtil.isNotBlank(CacheManager.getEmployeeById(htdv.getCustomerId()))) {
					htdv.setCustomerName(CacheManager.getEmployeeById(htdv.getCustomerId()).getName());
				} else {
					htdv.setCustomerName("[" + htdv.getCustomerId() + "]");
				}*/
				if(CommonUtil.isNotBlank(CacheManager.getUnitById(htdv.getBackOwnerId()))){
					htdv.setBackUnitName(CacheManager.getUnitByCode(htdv.getBackOwnerId()).getName());
				} else {
					htdv.setBackUnitName("["+htdv.getBackUnitName()+"]");
				}
				if (CommonUtil.isNotBlank(CacheManager.getStyleById(htdv.getStyleId()))) {
					htdv.setStyleName(CacheManager.getStyleNameById(htdv.getStyleId()));
				} else {
					htdv.setStyleName("[" + htdv.getStyleId() + "]");
				}

				/*if (CommonUtil.isNotBlank(CacheManager.getFloorByCode(htdv.getFloor()))) {
					htdv.setFloorName(CacheManager.getFloorByCode(htdv.getFloor()).getName());
				} else {
					htdv.setFloorName("[" + htdv.getFloor() + "]");
				}*/

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
