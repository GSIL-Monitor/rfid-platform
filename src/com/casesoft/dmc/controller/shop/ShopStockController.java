package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailStockDao;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.sys.Unit;
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
 * Created by WingLi on 2017-01-03.
 */
@Controller
@RequestMapping("/shop/shopStock")
public class ShopStockController extends BaseController {
	@Autowired
	DetailStockDao detailStockDao;

	@RequestMapping(value = "/index")
	public String index() {
		String ownerId = this.getCurrentUser().getOwnerId();
		Unit unit = CacheManager.getUnitById(ownerId);
		if (unit.getType() != Constant.UnitType.Shop) {
			// return "/views/error/noAuth";
			this.getRequest().setAttribute("ownerId", ownerId);
		} else {
			this.getRequest().setAttribute("ownerId", unit.getOwnerId());// 门店所属组织
			this.getRequest().setAttribute("shopId", ownerId);
			this.getRequest().setAttribute("shopName", unit.getName());

		}
		return "/views/shop/shopStock";
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult read(
			@RequestBody DataSourceRequest request) {

		DataSourceResult dataSourceResult = detailStockDao.getList(request);
		for (DetailStockView dtl : (List<DetailStockView>) dataSourceResult
				.getData()) {
			Unit warehHouse = CacheManager.getUnitByCode(dtl.getWarehId());
			if (CommonUtil.isNotBlank(warehHouse)) {
				dtl.setWarehName(warehHouse.getName());
			}
		}
		return dataSourceResult;
	}

	@RequestMapping(value = "/export", method = RequestMethod.POST)
	@ResponseBody
	public void export(String fileName, String base64, String contentType,
			HttpServletResponse response) throws IOException {

		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		response.setContentType(contentType);

		byte[] data = DatatypeConverter.parseBase64Binary(base64);

		response.setContentLength(data.length);
		response.getOutputStream().write(data);
		response.flushBuffer();
	}
}
