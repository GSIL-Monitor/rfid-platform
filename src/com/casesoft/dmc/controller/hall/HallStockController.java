package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.hall.ISampleDao;
import com.casesoft.dmc.extend.api.web.hub.SampleUtil;
import com.casesoft.dmc.model.hall.HallTask;
import com.casesoft.dmc.model.hall.Sample;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.hall.HallTaskService;
import com.casesoft.dmc.service.hall.SampleService;
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
@RequestMapping(value = "/hall/hallStock")
public class HallStockController extends BaseController {

	@Autowired
	private ISampleDao iSampleDao;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private HallTaskService hallTaskService;

	@RequestMapping(value = "/index")
	@Override
	public String index() {
		return "/views/hall/hallStock";
	}

	@RequestMapping(value = "/list")
	public
	@ResponseBody
	DataSourceResult read(@RequestBody DataSourceRequest request) {
		this.logAllRequestParams();

		DataSourceResult dataSourceResult = this.iSampleDao.getList(request);


		if (request.getGroup().size() == 0) {
			for (Sample s : (List<Sample>) dataSourceResult.getData()) {
				if (CommonUtil.isNotBlank(s.getOwnerId())) {
					if (CommonUtil.isNotBlank(CacheManager.getUnitById(s.getOwnerId()))) {
						s.setUnitName(CacheManager.getUnitById(s.getOwnerId()).getName());
					} else {
						s.setUnitName("[" + s.getOwnerId() + "]");
					}
				}
				if(CommonUtil.isNotBlank(CacheManager.getStyleById(s.getStyleId()))){
					s.setStyleName(CacheManager.getStyleNameById(s.getStyleId()));
				} else {
					s.setStyleName("["+s.getStyleId()+"]");
				}
				if (CommonUtil.isNotBlank(s.getFloor())) {
					/*if (CommonUtil.isNotBlank(CacheManager.getFloorByCode(s.getFloor()))) {
						s.setFloorName(CacheManager.getFloorByCode(s.getFloor()).getName());
					} else {
						s.setFloorName("[" + s.getFloor() + "]");
					}*/
				}
			}
		}
		return dataSourceResult;
	}

	@RequestMapping(value = "/ApplyDamage")
	@ResponseBody
	public MessageBox supplyDamage(String code) {
		Sample sampleInfo = this.sampleService.findByCode(code);
		Integer preStatus =sampleInfo.getStatus();
		sampleInfo.setPreStatus(preStatus);
		HallTask task =null;
		if (CommonUtil.isNotBlank(sampleInfo)) {
			if(sampleInfo.getStatus() == HallConstant.SampleStatus.Borrowed) {
				task =new HallTask();
//				sampleInfo.setStatus(HallConstant.SampleStatus.Bad);
				User User = (User) this.getSession().getAttribute(Constant.Session.User_Session);
				SampleUtil.covertToBackTask(sampleInfo, task, this.hallTaskService, User, HallConstant.BackStatus.BadTag);
				task.setOwnerId(this.getCurrentUser().getOwnerId());
			}
			sampleInfo.setStatus(HallConstant.SampleStatus.Bad);
		}
		try {
			this.sampleService.update(sampleInfo,task);
			return returnSuccessInfo("申请成功");
		} catch (Exception e) {
			return returnFailInfo("申请失败");
		}
	}

	@RequestMapping(value = "/confirmDamage")
	@ResponseBody
	public MessageBox confirmDamage(String code) {
		Sample sampleInfo = this.sampleService.findByCode(code);
		Integer preStatus =sampleInfo.getStatus();
		sampleInfo.setPreStatus(preStatus);
		HallTask task =null;
		if (CommonUtil.isNotBlank(sampleInfo)) {


			if(sampleInfo.getStatus() == HallConstant.SampleStatus.Borrowed) {
				task=new HallTask();
				sampleInfo.setStatus(HallConstant.SampleStatus.Lost);//已报损状态
				User User = (User) this.getSession().getAttribute(Constant.Session.User_Session);
				SampleUtil.covertToBackTask(sampleInfo, task, this.hallTaskService, User, HallConstant.BackStatus.BackBad);
				task.setOwnerId(this.getCurrentUser().getOwnerId());
			}
			sampleInfo.setStatus(HallConstant.SampleStatus.Badding);
		}
		try {
			this.sampleService.update(sampleInfo,task);
			return returnSuccessInfo("报损成功");
		} catch (Exception e) {
			return returnFailInfo("报损失败");
		}

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
