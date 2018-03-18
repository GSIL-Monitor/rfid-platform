package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.DeEnCode;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.cfg.DeviceConfig;
import com.casesoft.dmc.model.cfg.DeviceRunLog;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.cfg.DeviceRunLogService;
import com.casesoft.dmc.service.cfg.DeviceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by WinLi on 2017-03-30.
 */
@Controller
@RequestMapping( value = "/api/hub/device", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class DeviceApiController  extends ApiBaseController {
    @Autowired
    private DeviceRunLogService deviceRunLogService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/uploadRunLog.do")
    @ResponseBody
    public MessageBox uploadRunLog(String jsonString) throws Exception {
        this.logAllRequestParams();
        DeviceRunLog runLog = JSON.parseObject(jsonString, DeviceRunLog.class);
        runLog.setUploadTime(new Date());
        String lastDay = CommonUtil.reduceDay(runLog.getLogDate(), 1,"yyyy-MM-dd");
        DeviceRunLog lastDayLog = this.deviceRunLogService.findLastDayLog(runLog.getDeviceId(),lastDay);
        if(null == lastDayLog) {
            this.deviceRunLogService.save(runLog);
        } else {
            runLog.setSmdjHighKm(runLog.getSmdjHighKm().subtract(lastDayLog.getSmdjHighKm()));
            runLog.setSmdjLowKm(runLog.getSmdjLowKm().subtract(lastDayLog.getSmdjLowKm()));
            runLog.setSldjKm(runLog.getSldjKm().subtract(lastDayLog.getSldjKm()));
            runLog.setCldjKm(runLog.getCldjKm().subtract(lastDayLog.getCldjKm()));
            runLog.setQmOnCs(runLog.getQmOnCs().subtract(lastDayLog.getQmOnCs()));
            runLog.setQmOffCs(runLog.getQmOffCs().subtract(lastDayLog.getQmOffCs()));
            runLog.setHmOnCs(runLog.getHmOnCs().subtract(lastDayLog.getHmOnCs()));
            runLog.setHmOffCs(runLog.getHmOffCs().subtract(lastDayLog.getHmOffCs()));

            runLog.setQmAlarmOnCs(runLog.getQmAlarmOnCs().subtract(lastDayLog.getQmAlarmOnCs()));
            runLog.setQmAlarmOffCs(runLog.getQmAlarmOffCs().subtract(lastDayLog.getQmAlarmOffCs()));
            runLog.setHmAlarmOnCs(runLog.getHmAlarmOnCs().subtract(lastDayLog.getHmAlarmOnCs()));
            runLog.setHmAlarmOffCs(runLog.getHmAlarmOffCs().subtract(lastDayLog.getHmAlarmOffCs()));
            this.deviceRunLogService.save(runLog);
        }
        return this.returnSuccessInfo("上传成功");
    }
    @RequestMapping(value = "/getDeviceConfig.do")
    @ResponseBody
    public MessageBox getDeviceConfig(String deviceId) {
        Assert.notNull(deviceId,"设备号不能为空");
        List<DeviceConfig> configList = this.deviceService.getDeviceConfig(deviceId);
        return this.returnSuccessInfo("获取成功",configList);
    }
    @RequestMapping(value = "/validDeviceWS.do")
    @ResponseBody
    public MessageBox validDeviceWS(){
        this.logAllRequestParams();
        try{
            String deviceId=this.getReqParam("deviceId");
            Device oDevice=this.deviceService.get("code",deviceId);
            if(CommonUtil.isNotBlank(oDevice)){
                Unit u = CacheManager.getUnitById(CacheManager.getDeviceByCode(deviceId).getStorageId());
                if(CommonUtil.isNotBlank(u)){
                    oDevice.setStorageName(u.getName());
                    oDevice.setAppCode(DeEnCode.getInstance().encrypt(oDevice.getAppCode()));
                    oDevice.setKey(DeEnCode.getInstance().encrypt(oDevice.getKey()));
                    oDevice.setMchId(DeEnCode.getInstance().encrypt(oDevice.getMchId()));
                    oDevice.setCallbackUrl(DeEnCode.getInstance().encrypt(oDevice.getCallbackUrl()));
                    return this.returnSuccessInfo("验证成功！",oDevice);
                }else{
                    return this.returnFailInfo("验证失败！");
                }
            }else{
                return this.returnFailInfo("设备未注册");
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                return this.returnFailInfo("验证信息不正确");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return this.returnFailInfo("设备未注册");
    }
}
