package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.controller.syn.tool.SynTaskUtil;
import com.casesoft.dmc.controller.syn.tool.TaskAdjustUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping( value="/api/hub/task", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class TaskApiController extends ApiBaseController {

    @Autowired
    private TaskService taskService;

    @Override
    public String index() {
        return null;
    }


    @RequestMapping(value = "/uploadTask")
    @ResponseBody
    public MessageBox uploadTaskFileFormDevice(MultipartFile file) throws Exception {
        String inputPath = Constant.rootPath+File.separator+PropertyUtil.getValue("MilanUpload");//上传的目录
        String fileName = file.getOriginalFilename();
        File targetFile = new File(inputPath, fileName);
        inputStreamToFile(file.getInputStream(), targetFile);
        return uploadTask(targetFile, fileName);

    }

    private void inputStreamToFile(InputStream ins, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    public MessageBox uploadTask(File file, String fileName) throws Exception {
        this.logAllRequestParams();
        
        Assert.notNull(file, "file不可为空");
        String inputPath = PropertyUtil.getValue("MilanUpload") + File.separator + PropertyUtil.getValue("tag_name");
        String successFilePath=PropertyUtil.getValue("MilanUploadHistory") +  File.separator + PropertyUtil.getValue("tag_name");
        File uploadFile = new File(inputPath);
        File fileHistory = new File(successFilePath);

        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }
        if(!fileHistory.exists()){
            fileHistory.mkdirs();
        }
        Business bus = null;
        if (file != null && !CommonUtil.isBlank(fileName)) {


            try {
                bus = SynTaskUtil.convertZipToDto(new FileInputStream(file));
                Business business = this.taskService.get("id",bus.getId());

                TaskAdjustUtil.adjustStorageId(bus);
                if (bus == null)
                    return this.returnFailInfo("文件内容为空");
                if(CommonUtil.isNotBlank(business)){
                    return this.returnFailInfo("数据已经上传请返回");
                }
                if (TagFactory.getCurrentTag().isUniqueCodeStock()){
                    MessageBox msgBox = this.taskService.checkEpcStock(bus);
                    if(!msgBox.getSuccess())
                    return msgBox;
                }
                logger.error("解析成功");
                boolean autoUpload = Boolean.parseBoolean(PropertyUtil
                        .getValue("auto_upload_erp"));
                if (!autoUpload) {
                    this.taskService.save(bus);
                } else {
                    this.taskService.save(bus, autoUpload);
                }

            } catch (Exception e) {
                // 解析文件失败，将其存入C:\\MiLanUpload
                FileUtil.moveFile(file, inputPath +  File.separator + fileName);
                logger.error(e.getMessage());
                e.printStackTrace();
                return this.returnFailInfo("服务器处理失败！"+e.getMessage());
            }

            FileUtil.moveFile(file,
                    inputPath.replace("MilanUpload", "MilanUploadHistory")
                            +  File.separator + fileName);
        }else{
            return this.returnFailInfo("文件为空！");
        }


        // 删除云同步数据2014-11-23
        if (CommonUtil.isNotBlank(bus)) {
            this.taskService.deleteCloudBusiness(bus.getId());
        }
        return this.returnSuccessInfo("文件上传成功!",bus);
    }


    /**
     * 提供给设备端检查唯一码是否可以出入库
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkEpcStockWS")
    @ResponseBody
    public MessageBox checkEpcStockWS() throws Exception {
        this.logAllRequestParams();
        if (!TagFactory.getCurrentTag().isUniqueCodeStock()) {
            return this.returnSuccessInfo("检测成功！");
        }

        String uniqueCodeList = this.getReqParam("uniqueCodeList");
        int token = Integer.parseInt(this.getReqParam("token"));
        String deviceId = this.getReqParam("deviceId");

        return this.taskService.checkEpcStock(uniqueCodeList,token,deviceId);

    }

    @RequestMapping(value = "/listRecordByTaskIdWS")
    @ResponseBody
    public  String listRecordByTaskIdWS(String filter_EQS_taskId, String billType) {
        this.logAllRequestParams();
        
        Assert.notNull(filter_EQS_taskId,"filter_EQS_taskId不能为空");
        List<Record> recordList = new ArrayList<>();
        if (CommonUtil.isNotBlank(billType)) {
            recordList = this.taskService.findRecordByTask(filter_EQS_taskId+"-"+billType);
        }else{
            recordList = this.taskService.findRecordByTask(filter_EQS_taskId);
        }
         this.outJsonString(  FastJSONUtil.retainField(recordList,"code"));
        return null;
    }

    public  List<Record> testListRecordByTaskIdWS(String filter_EQS_taskId, String billType) {
        this.logAllRequestParams();

        Assert.notNull(filter_EQS_taskId,"filter_EQS_taskId不能为空");
        return this.taskService.findRecordByTask(filter_EQS_taskId);
    }
}
