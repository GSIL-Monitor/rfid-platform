package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.hall.HallInventory;
import com.casesoft.dmc.model.hall.HallInventoryDetail;
import com.casesoft.dmc.service.hall.HallInventoryDetailImpl;
import com.casesoft.dmc.service.hall.HallInventoryDetailService;
import com.casesoft.dmc.service.hall.HallInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;

/**
 * Created by session on 2017/4/5 0005.
 */

@Controller
@RequestMapping(value = "/hall/hallInventoryDetail")
public class HallInventoryDetailController extends BaseController implements IBaseInfoController<HallInventoryDetail>{

	@Autowired
	HallInventoryDetailService hallInventoryDetailService;

	@Autowired
	HallInventoryService hallInventoryService;

	@Autowired
	HallInventoryDetailImpl hallInventoryDetailImpl;

	public String index() {
		return null;
	}


	@RequestMapping(value = "/page")
	@ResponseBody
	@Override
	public Page<HallInventoryDetail> findPage(Page<HallInventoryDetail> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();

		page =this.hallInventoryDetailService.findPage(page,filters);
		return page;
	}

	@Override
	public List<HallInventoryDetail> list() throws Exception {
		return null;
	}

	@Override
	public MessageBox save(HallInventoryDetail entity) throws Exception {
		return null;
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
