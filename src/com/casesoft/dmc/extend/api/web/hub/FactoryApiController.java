package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.factory.FactoryBillInitUtil;
import com.casesoft.dmc.controller.factory.FactoryBillUtil;
import com.casesoft.dmc.controller.factory.FactoryConstant;
import com.casesoft.dmc.controller.factory.FactoryTaskUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.factory.*;
import com.casesoft.dmc.model.hall.Employee;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.factory.*;
import com.casesoft.dmc.service.hall.EmployeeService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

/**
 * Created by Alvin-PC on 2017/5/16 0016.
 */
@Controller
@RequestMapping(value = "/api/hub/factory", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "工厂模块接口")
public class FactoryApiController extends ApiBaseController {

    @Autowired
    private FactoryInitService factoryInitService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    public FactoryTaskService taskService;
    @Autowired
    public FactoryBillSearchService factoryBillSearchService;
    @Autowired
    private FactoryTokenService factoryTokenService;
    @Autowired
    private PauseReasonService pauseReasonService;
    @Override
    public String index() {
        return null;
    }

    /*
     * 提交打印结果
     * @param billNo
     * @param factoryBillNo
     * @param status
     * @return MessageBox
    * */
    @RequestMapping("/update")
    @ResponseBody
    public MessageBox update(String billNo, String factoryBillNo,Integer status){
        this.logAllRequestParams();
        FactoryInit initBill;
        if(CommonUtil.isBlank(factoryBillNo)){
            initBill = this.factoryInitService.get("billNo", billNo);
        }else{
            initBill = this.factoryInitService.findFactoryInit(billNo, factoryBillNo);
        }
        if(initBill.getStatus() != 2){
            initBill.setStatus(status);
            this.factoryInitService.updateStatus(initBill);
        }
        return returnSuccessInfo("更新成功");
    }

    /*
     * 获取待打印的上传文件办单信息    *
     * @return MessageBox
    * */
    @RequestMapping("/findPrintBill")
    @ResponseBody
    public void findPrintBill() throws Exception{
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<FactoryInit> initBillList = this.factoryInitService.find(filters);
        this.returnSuccess("ok",initBillList);
    }

    /*
     * 获取待打印的上传文件办单明细信息
     * @return MessageBox
    * */
    @RequestMapping("/findPrintBillInfo")
    @ResponseBody
    public void findPrintBillInfo() throws Exception{
        this.logAllRequestParams();
        String billNo = this.getReqParam("filter_EQS_billNo");
        String factoryBillNo = this.getReqParam("filter_EQS_factoryBillNo");
        List<FactoryBillDtl> dtlList;
        if(CommonUtil.isBlank(factoryBillNo)){
            dtlList = factoryInitService.findDtls(billNo,"1");
        }else{
            dtlList = factoryInitService.findFactoryBillDtl(billNo, factoryBillNo, "1");
        }
        this.returnSuccess("ok",dtlList);
    }

    @RequestMapping("/downloadPrintFile")
    @ResponseBody
    public void  downloadPrintFile(String billNo,String deviceId,String factoryBillNo) throws Exception {
        this.logAllRequestParams();
        Device device = CacheManager.getDeviceByCode(deviceId);

        String ownerId;
        if(CommonUtil.isBlank(device)){
            ownerId ="1";
        }else{
            ownerId = device.getOwnerId();
        }
        // 不能为空
        boolean isRfid = true;

        FactoryInit master;
        List<FactoryBillDtl> details  = new ArrayList<FactoryBillDtl>();
        if(CommonUtil.isBlank(factoryBillNo)){
            master = this.factoryInitService.get("billNo", billNo);
            details = this.factoryInitService.findDtls(billNo, ownerId);
        }else{
            master = this.factoryInitService.findFactoryInit(billNo, factoryBillNo);
            details = this.factoryInitService.findFactoryBillDtl(billNo, factoryBillNo, ownerId);
        }
        File inputPath = FactoryBillInitUtil.writeTextFile(details, null, isRfid);

        if (master.getStatus() == 1) {// 已确认状态
            this.factoryInitService.update(FactoryBillInitUtil.epcList, master);
        }

        String filename = inputPath.getName();
        String contentType = "application/zip;charset=utf-8";
        this.outFile(filename,inputPath,contentType);
    }

    /*
    * 下载基础epc信息
   * */
    @RequestMapping("/findAllEpc")
    @ResponseBody
    public void findAllEpc() throws Exception{
        this.logAllRequestParams();
        List<InitEpc> epcList = this.factoryInitService.findInitEpcList();
        FactoryBillInitUtil.covertToEcplList(epcList);
        this.returnSuccess("ok",epcList);
    }

   /*
    * 获取员工信息接口
   * */

    @RequestMapping(value="/findEmployeer")
    @ResponseBody
    public MessageBox findEmployeer(){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        for(int i = 0 ; i< filters.size();i++){
            String name = filters.get(i).getPropertyName();
            if(name.equals("typeId")){
                filters.remove(i);
            }
        }
        List<Employee> employeeList = this.employeeService.find(filters);
        return new MessageBox(true,"ok", employeeList);
    }

    /*
    * 登录验证接口
   * */
    @RequestMapping("/loginConfirm")
    public void loginConfirm(){
        this.logAllRequestParams();
        String userCode = this.getReqParam("userCode");
        String password = this.getReqParam("password");
        try {
            User user = this.userService.getUser(userCode, password);
            if (user == null){
                throw new ServiceException("用户名或密码错误");
            }
            this.returnSuccess(new MessageBox(true, "登录成功", user));
        } catch (Exception ex) {
            final String message = ex.getMessage();
            this.returnFailur(message,userCode);
        }

    }







    /**
     * 上传任务信息     *
     * */
    @RequestMapping("/uploadTask")
    public void uploadTask() throws Exception {
        this.logAllRequestParams();
        String taskStr = this.getReqParam("tsk");
        String dtl = this.getReqParam("dtl");
        FactoryTask task = JSON.parseObject(taskStr, FactoryTask.class);
        JSONArray jsonArray = JSON.parseObject(dtl).getJSONArray("epcList");
        if (CommonUtil.isNotBlank(task.getIsBack())) {
            if (task.getIsBack().equals("Y")) {
                task.setType(FactoryConstant.TaskType.Back);
            }
        } else {
            task.setIsBack("N");
        }
        List<String> codeList = new ArrayList<String>();
        for (Object epc : jsonArray) {
            codeList.add(epc.toString());
        }
        MessageBox checkMsg = CheckCodeList(task, codeList);
        if (!checkMsg.getSuccess()) {
            this.returnFailur(checkMsg.getMsg());
            return;
        }
        task.setTaskTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        FactoryTaskUtil.covertToSaveTask(task, taskService);

        /*更新办单进度*/
        this.taskService.save(task);

        updateFactoryBill(task);
        this.returnSuccess("true", "保存成功");
        return;


    }

    private void updateFactoryBill(FactoryTask task) {
        List<InitEpc> epcList = task.getEpcList();
        List<String> billNoStrList = new ArrayList<String>();
        List<String> billDateStrList = new ArrayList<String>();
        List<String> endDateStrList = new ArrayList<String>();
        List<String> uploadNoStrList = new ArrayList<String>();
        for (InitEpc epc : epcList) {
            billNoStrList.add(epc.getBillNo());
            billDateStrList.add(epc.getBillDate());
            endDateStrList.add(epc.getEndDate());
            uploadNoStrList.add(epc.getUploadNo());
        }
        String inBillNo = CommonUtil.getSqlStrByList(billNoStrList, FactoryBill.class, "billNo");
        String inBillDate = CommonUtil.getSqlStrByList(billDateStrList, FactoryBill.class, "billDate");
        String inEndDate = CommonUtil.getSqlStrByList(endDateStrList, FactoryBill.class, "endDate");
        String inUploadNo = CommonUtil.getSqlStrByList(uploadNoStrList, FactoryBill.class, "uploadNo");
        this.taskService.updateBillProgress(inBillNo, inBillDate, inEndDate, inUploadNo, task.getToken() + "-" + task.getType());
        if (task.getType().equals(FactoryConstant.TaskType.Inbound) || task.getType().equals(FactoryConstant.TaskType.Outbound)) {

            inBillNo = CommonUtil.getSqlStrByList(billNoStrList, BillSchedule.class, "billNo");
            inBillDate = CommonUtil.getSqlStrByList(billDateStrList, BillSchedule.class, "billDate");
            inEndDate = CommonUtil.getSqlStrByList(endDateStrList, BillSchedule.class, "endDate");
            inUploadNo = CommonUtil.getSqlStrByList(uploadNoStrList, BillSchedule.class, "uploadNo");
            this.taskService.updateBillSchedule(inBillNo, inBillDate, inEndDate, inUploadNo, task.getToken(), task.getType(), task.getTaskTime());
            if (CacheManager.getFactoryTokenByToken(task.getToken()).getIsLast().equals("Y")) {
                inBillNo = CommonUtil.getSqlStrByList(billNoStrList, FactoryBill.class, "billNo");
                inBillDate = CommonUtil.getSqlStrByList(billDateStrList, FactoryBill.class, "billDate");
                inEndDate = CommonUtil.getSqlStrByList(endDateStrList, FactoryBill.class, "endDate");
                inUploadNo = CommonUtil.getSqlStrByList(uploadNoStrList, FactoryBill.class, "uploadNo");
                this.taskService.updateBill(inBillNo, inBillDate, inEndDate, inUploadNo, task.getTaskTime());
            }
        }
    }

    private MessageBox CheckCodeList(FactoryTask task, List<String> codeList) throws Exception {
        MessageBox checkMsg = new MessageBox(false, "");
        checkMsg = checkOperatorTask(task);
        if (!checkMsg.getSuccess()) {
            return new MessageBox(false, checkMsg.getMsg());
        }
        checkMsg = new MessageBox(false, "");
        checkMsg = checkLastTaskIsDo(task, codeList);
        if (!checkMsg.getSuccess()) {
            return new MessageBox(false, checkMsg.getMsg());
        }

        List<InitEpc> list = this.taskService.findEpcListBycodes(TaskUtil.getSqlStrByList(codeList, InitEpc.class, "code"));
        for (InitEpc e : list) {
            e.setProgress(task.getToken() + "-" + task.getType());
        }
        task.setEpcList(list);
        task.setQty(codeList.size());
        return new MessageBox(true, "ok");
    }


    private MessageBox checkOperatorTask(FactoryTask task) {
        Employee operator = CacheManager.getEmployeeById(task.getOperator());
        if (CommonUtil.isBlank(operator)) {
            return new MessageBox(false, "错误的员工卡!");
        }else {
            return new MessageBox(true, "ok");
        }
    }

    private MessageBox checkLastTaskIsDo(FactoryTask task, List<String> codeList) {
        List<FactoryRecord> recordList = this.factoryBillSearchService.findRecordByCodeLastTask(
                TaskUtil.getSqlStrByList(codeList, InitEpc.class, "code"));
        StringBuffer errorCodeStr = null;
        String backCodeStr="";
        for (FactoryRecord r : recordList) {
            switch (task.getType()) {
                case FactoryConstant.TaskType.Inbound:
                    if (CommonUtil.isNotBlank(r.getToken())) {
                        if(!(CacheManager.getFactoryTokenByToken(r.getToken()).getTypes().split(",").length == 1) && (r.getType().equals(FactoryConstant.TaskType.Inbound)
                                || r.getType().equals(FactoryConstant.TaskType.Pause) || r.getType().equals(FactoryConstant.TaskType.Offline))){
                            if((r.getType().equals(FactoryConstant.TaskType.Pause) || r.getType().equals(FactoryConstant.TaskType.Offline)) && r.getToken() == task.getToken()){
                                task.setType(FactoryConstant.TaskType.Restart);
                            }else{
                                if(CommonUtil.isBlank(errorCodeStr)){
                                    errorCodeStr = new StringBuffer();
                                }
                                errorCodeStr.append("唯一号:"+r.getCode()+"状态为:"+ CacheManager.getFactoryTokenByToken(r.getToken()).getName());
                                if(CommonUtil.isNotBlank(r.getIsOutSource())){
                                    if(r.getIsOutSource().equals("Y")){
                                        errorCodeStr.append("外包");
                                    }
                                }
                                if((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length >1){
                                    errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                                }
                            }

                        }else if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                            if(r.getToken() != task.getToken()){
                                if (CommonUtil.isBlank(errorCodeStr)) {
                                    errorCodeStr = new StringBuffer();
                                }
                                errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());

                                if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                    errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                                }
                                if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                    errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                                }
                            }else{
                                backCodeStr+= "," + r.getCode();
                            }

                        }
                    } else {
                        if (!CacheManager.getFactoryTokenByToken(task.getToken()).getIsFirst().equals("Y")) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "未流转").append("\r\n");
                        }
                    }

                    break;
                case FactoryConstant.TaskType.Outbound:
                    if (CommonUtil.isNotBlank(r.getToken())) {
                        if (!(r.getToken() == task.getToken() && (r.getType().equals(FactoryConstant.TaskType.Inbound) ||
                                r.getType().equals(FactoryConstant.TaskType.Restart)))) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());
                            if (CommonUtil.isNotBlank(r.getIsOutSource())) {
                                if (r.getIsOutSource().equals("Y")) {
                                    errorCodeStr.append("外包");
                                }
                            }
                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        } else if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());

                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        }
                    } else {
                        if (CommonUtil.isBlank(errorCodeStr)) {
                            errorCodeStr = new StringBuffer();
                        }
                        errorCodeStr.append("唯一号:" + r.getCode() + "未流转").append("\r\n");
                    }

                    break;
                case FactoryConstant.TaskType.Pause:
                case FactoryConstant.TaskType.Offline:
                    if (CommonUtil.isNotBlank(r.getToken())) {
                        if (!(r.getToken() == task.getToken() && (r.getType().equals(FactoryConstant.TaskType.Inbound)
                                || r.getType().equals(FactoryConstant.TaskType.Restart)))) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());
                            if (CommonUtil.isNotBlank(r.getIsOutSource())) {
                                if (r.getIsOutSource().equals("Y")) {
                                    errorCodeStr.append("外包");
                                }
                            }
                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        } else if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());

                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        }
                    } else {
                        if (CommonUtil.isBlank(errorCodeStr)) {
                            errorCodeStr = new StringBuffer();
                        }
                        errorCodeStr.append("唯一号:" + r.getCode() + "未流转").append("\r\n");
                    }
                    break;
                case FactoryConstant.TaskType.Back:
                    if (CommonUtil.isNotBlank(r.getToken())) {
                        if (!(CacheManager.getFactoryTokenByToken(r.getToken()).getTypes().split(",").length == 1) && (!r.getType().equals(FactoryConstant.TaskType.Outbound))) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());
                            if (CommonUtil.isNotBlank(r.getIsOutSource())) {
                                if (r.getIsOutSource().equals("Y")) {
                                    errorCodeStr.append("外包");
                                }
                            }
                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        } else if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + r.getCode() + "状态为:" + CacheManager.getFactoryTokenByToken(r.getToken()).getName());

                            if ((CacheManager.getFactoryTokenByToken(r.getToken()).getTypes()).split(",").length > 1) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                            if (r.getType().equals(FactoryConstant.TaskType.Back)) {
                                errorCodeStr.append(FactoryBillUtil.findTypeName(r.getType())).append("\r\n");
                            }
                        }

                    } else {
                        if (CommonUtil.isBlank(errorCodeStr)) {
                            errorCodeStr = new StringBuffer();
                        }
                        errorCodeStr.append("唯一号:" + r.getCode() + "未流转").append("\r\n");
                    }
                    break;
                default:
                    break;

            }

        }
        Token t = CacheManager.getFactoryTokenByToken(task.getToken());
        if(t.getMultiple().equals("N")){
            if(task.getType().equals(FactoryConstant.TaskType.Inbound)){
                //限制流程扫描次数
                String type = (t.getTypes().split(",").length > 1 ? "O": "I");
                List<String> scanedRecordList = this.factoryBillSearchService.findScanedRecordByCode(TaskUtil.getSqlStrByList(codeList, FactoryRecord.class, "code"),task.getToken(),type);
                for(String code : scanedRecordList ){
                    if(backCodeStr.indexOf(code) == -1) {
                        if (CommonUtil.isBlank(errorCodeStr)) {
                            errorCodeStr = new StringBuffer();
                        }
                        errorCodeStr.append("唯一号:" + code + "流程:" + t.getName() + "已扫描").append("\r\n");
                    }

                }
            }
        }
        if(t.getNecessary().equals("Y") && CommonUtil.isNotBlank(t.getLastToken())){
            if(task.getType().equals(FactoryConstant.TaskType.Inbound)) {
                Token lastToken = CacheManager.getFactoryTokenByToken(t.getLastToken());
                String type = (lastToken.getTypes().split(",").length > 1 ? "O" : "I");
                List<String> lastRecordList = this.factoryBillSearchService.findScanedRecordByCode(TaskUtil.getSqlStrByList(codeList, FactoryRecord.class, "code"), lastToken.getToken(), type);
                if (lastRecordList.size() != codeList.size()) {
                    Map<String, String> recordMap = new HashMap<String, String>();
                    for (String code : lastRecordList) {
                        recordMap.put(code, code);
                    }
                    for (String code : codeList) {
                        if (!recordMap.containsKey(code)) {
                            if (CommonUtil.isBlank(errorCodeStr)) {
                                errorCodeStr = new StringBuffer();
                            }
                            errorCodeStr.append("唯一号:" + code + "上一个必走流程:" + lastToken.getName() + "未扫描").append("\r\n");
                        }
                    }
                }
            }

        }

        if (CommonUtil.isNotBlank(errorCodeStr)) {
            return new MessageBox(false, errorCodeStr.append("无法上传").toString());
        }
        return new MessageBox(true, "ok");
    }


    /**
     * 获取外包待入库单据信息
     * @param billNo
     * */
    @RequestMapping("/getInbound")
    public void getInbound(String billNo) throws Exception{
        this.logAllRequestParams();
        List<FactoryOutSorceBill> billList = this.taskService.findOutSourceBill();
        FactoryTaskUtil.covertToOutSorceBill(billList,this.taskService);
        if(CommonUtil.isBlank(billNo)){
            this.returnSuccess("ok", billList);
        }else{
            List<FactoryOutSorceBill> resultList = new ArrayList<FactoryOutSorceBill>();
            for(FactoryOutSorceBill bill: billList){
                if(bill.getBillNos().contains(billNo)){
                    resultList.add(bill);
                }
            }
            this.returnSuccess("ok", resultList);
        }

    }
    /**
     * 通过唯一码获取办单信息
     * @param filter_INS_code  code,code2
     * */
    @RequestMapping("/findBillByCode")
     public void findBillByCode(String filter_INS_code) throws Exception{
        this.logAllRequestParams();
        String codes = filter_INS_code;
        List<String> codeList = new ArrayList<String>();
        if(CommonUtil.isNotBlank(codes)){
            for(String s :codes.split(",")){
                codeList.add(s);
            }
        }else{
            this.returnFailur("查询唯一码为空");
        }
        List<FactoryBriefBill> billList = this.factoryBillSearchService.findBillInfoByCodes(
                CommonUtil.getSqlStrByList(codeList, InitEpc.class, "code"));
        FactoryBillUtil.covertToVo(billList,this.factoryBillSearchService);
        this.returnSuccess("ok",billList);
    }
    /**
     * 通过单号查办单据信息
     * */

     @RequestMapping("/findBill")
     public void findBill() throws Exception{
        this.logAllRequestParams();
        String billNo = this.getReqParam("filter_EQS_billNo");
        FactoryBill bill = this.factoryBillSearchService.findBillByBillNo(billNo);
        List<FactoryBriefBill> recordList = this.factoryBillSearchService.findRecodListByBillNo(billNo);
        bill.setRecordList(recordList);
        this.returnSuccess("ok", bill);

    }

    /**
     * 获取Token
     * */
    @RequestMapping("/findToken")
    @ResponseBody
    public void findToken() throws Exception{
        this.logAllRequestParams();
        List<Token> list = this.factoryTokenService.findAllToken();
        this.returnSuccess("ok",list);
    }

    /**
     *获取产品
     * */
    @RequestMapping("/findCategory")
    @ResponseBody
    public void findCategory() throws Exception{
        this.logAllRequestParams();
        List<FactoryCategory> list = this.factoryTokenService.findAllCategory();
        this.returnSuccess("ok",list);
    }

    @RequestMapping("/findPauseList")
    @ResponseBody
    public void findPauseList() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<PauseReason> pauseReasonList=this.pauseReasonService.find(filters);
        this.returnSuccess("ok",pauseReasonList);
    }

}
