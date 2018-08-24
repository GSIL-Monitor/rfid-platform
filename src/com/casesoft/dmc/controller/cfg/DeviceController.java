package com.casesoft.dmc.controller.cfg;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.cfg.DeviceConfig;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.cfg.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/data/device")
public class DeviceController extends BaseController implements IBaseInfoController<Device>{

	@Autowired
	private DeviceService deviceService;
	
	@RequestMapping("/index")
	@Override
	public String index() {
		return "views/data/device";
	}
	
	@RequestMapping("/page")
    @ResponseBody
	@Override
	public Page<Device> findPage(Page<Device> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=deviceService.findPage(page, filters);
        Unit unit=null;
            for (Device device : page.getRows()) {
            	try{
                device.setUnitName(CacheManager.getUnitById(device.getOwnerId()).getName());
                unit = CacheManager.getStorageById(device.getStorageId());
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                if (unit!=null) {
                    device.setStorageName(unit.getName());
                }
            }
		return page;
	}
	@RequestMapping("/list")
	@ResponseBody
	@Override
	public List<Device> list() throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		return this.deviceService.find(filters);
	}


    @RequestMapping(value="/save")
    @ResponseBody
	@Override
	public MessageBox save(Device  device) throws Exception {
        this.logAllRequestParams();
        Device dev =this.deviceService.findUniqueByCode(device.getCode());
        if (CommonUtil.isBlank(dev)){
        	dev =new Device();
            dev.setId(device.getCode());
            dev.setCode(device.getCode());
            dev.setOwnerId(device.getOwnerId());
            dev.setStorageId(device.getStorageId());
            dev.setCreator(this.getCurrentUser().getOwnerId());
            dev.setCreateTime(new Date());
            dev.setRemark(device.getRemark());
            dev.setUpdater(dev.getCreator());
            dev.setUpdateTime(dev.getCreateTime());
			dev.setAppCode(device.getAppCode());
			dev.setMchId(device.getMchId());
			dev.setKey(device.getKey());
			dev.setCallbackUrl(device.getCallbackUrl());
        }else{
        	dev.setAppCode(device.getAppCode());
        	dev.setMchId(device.getMchId());
        	dev.setKey(device.getKey());
        	dev.setCallbackUrl(device.getCallbackUrl());
        	dev.setOwnerId(device.getOwnerId());
            dev.setStorageId(device.getStorageId());
            dev.setRemark(device.getRemark());
        	dev.setUpdater(this.getCurrentUser().getOwnerId());
        	dev.setUpdateTime(new Date());
        }
        device.setLocked(0);
       try {
            this.deviceService.save(dev);
            List<Device> deviceList = new ArrayList<>();
            deviceList.add(dev);
            CacheManager.refreshDeviceCache(deviceList);
            return returnSuccessInfo("保存成功");
        }catch (Exception e){
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

	//region 设备配置相关函数
	@RequestMapping(value="/editConfigPage")
	public ModelAndView editConfigPage(String deviceId) {
		Assert.notNull(deviceId,"设备号不能为空");
		ModelAndView modelAndView = new ModelAndView("/views/data/device_config");
		modelAndView.addObject("deviceId",deviceId);
		return modelAndView;
	}
	@RequestMapping(value="/getConfigList")
	@ResponseBody
	public List<DeviceConfig> getConfigList(String deviceId) {
		Assert.notNull(deviceId,"设备号不能为空");
		List<DeviceConfig> configList = this.deviceService.getDeviceConfig(deviceId);
		return configList;
	}
	@RequestMapping(value="/saveConfig")
	@ResponseBody
	public MessageBox saveConfig(DeviceConfig deviceConfig) {
		if(CommonUtil.isBlank(deviceConfig.getId())) {
			deviceConfig.setId(new GuidCreator().toString());
		}
		deviceConfig.setUpdater(this.getCurrentUser().getCode());
		deviceConfig.setUpdateTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
		this.deviceService.saveConfig(deviceConfig);
		return this.returnSuccessInfo("保存成功",deviceConfig);
	}
	//endregion

}
