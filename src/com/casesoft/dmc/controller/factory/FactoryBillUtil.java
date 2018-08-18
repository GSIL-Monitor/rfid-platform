package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.factory.*;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.factory.FactoryBillSearchService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class FactoryBillUtil {
    private static int maxRowIndex = 59999;

    public static void covertToVo(List<FactoryBriefBill> billList,
                                  FactoryBillSearchService factoryBillSearchService) {
        for(FactoryBriefBill bill:billList){
            List<FactoryRecord> recordList = factoryBillSearchService.findRecordListByCode(bill.getCode());
            for(FactoryRecord r:recordList){
               /* Token t = CacheManager.getFactoryTokenByToken(r.getToken());*/
               /* String tokenName=t.getName();
                r.setTokenName(tokenName+((r.getIsOutSource().equals("Y"))?"外包  ":" "));
                if(t.getTypes().split(",").length == 1){

                }else{
                    r.setTokenName(r.getTokenName()+findTypeName(r.getType()));
                }*/
                User opertor = CacheManager.getUserById(r.getOperator());
                if(CommonUtil.isNotBlank(opertor)){
                    r.setOperatorName(opertor.getName());
                }
            }
            bill.setRecordList(recordList);

        }
    }

    public static String findTypeName(String type) {
        String typeName = "";
        switch (type) {
            case FactoryConstant.TaskType.Inbound:
                typeName = "开始";
                break;
            case FactoryConstant.TaskType.Pause:
                typeName = "暂停";
                break;
            case FactoryConstant.TaskType.Restart:
                typeName = "恢复扫描";
                break;
            case FactoryConstant.TaskType.Outbound:
                typeName = "结束";
                break;
            case FactoryConstant.TaskType.Back:
                typeName = "返工";
                break;
            case FactoryConstant.TaskType.Offline:
                typeName = "离线";
                break;
        }
        return typeName;
    }

    public static void coverToBill(List<FactoryBillView> rows) throws Exception {
        for(FactoryBillView f : rows){
            if(CommonUtil.isNotBlank(f.getProgress())){
                Integer token = Integer.parseInt(f.getProgress().split("-")[0]);
                String type = f.getProgress().split("-")[1];
               /* String tokenName = CacheManager.getFactoryTokenByToken(token).getName();*/
               /* f.setProgressName(tokenName);*/
               /* if((CacheManager.getFactoryTokenByToken(token).getIsLast().equals("N"))){
                    f.setProgressName(tokenName + findTypeName(type));
                }*/
                if(type.equals(FactoryConstant.TaskType.Back)){
                   /* f.setProgressName(tokenName + findTypeName(type));*/
                }
                if(CommonUtil.isNotBlank(f.getOutDate())){
                    String startDay="";
                    startDay = CommonUtil.getDateString(f.getBillDate(),"yyyy-MM-dd");
                    String endDay = CommonUtil.getDateString(f.getOutDate(), "yyyy-MM-dd");

                    int totDay = FactoryUtil.getTotWorkDay(startDay, endDay);
                    f.setTotDay(totDay);
                }
                if(CommonUtil.isNotBlank(f.getScheduleStartDate()) &&  CommonUtil.isNotBlank(f.getScheduleEndDate())){

                    int totDay = FactoryUtil.getTotWorkDay(f.getScheduleStartDate(), f.getScheduleEndDate());
                    f.setSchedulePeriod(totDay);
                }
            }
        }
    }
    
    public static File writeBillFile(List<FactoryBillView> list) throws Exception {

        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("办单管理");
        HSSFCellStyle style = taskExcelBook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

        String path = Constant.Folder.Report_File_Folder +"\\";// 创建目录
        new File(path+path).mkdirs();
        if (CommonUtil.isBlank(list)) {
            sheet = taskExcelBook.createSheet();
            File file = new File(path+"办单管理" + CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss") + ".xls");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            taskExcelBook.write(fos);
            fos.flush();
            fos.close();
            return file;
        }
        initBillExcelWidth(sheet);
        HSSFRow row = null;
        row = sheet.createRow(0);
        initExcelBillCell(row, style);
        int count = 1;
        int index = 0;
        for (FactoryBillView bill : list) {
            if (index > maxRowIndex) {
                sheet = taskExcelBook.createSheet("办单管理" + count);
                initBillExcelWidth(sheet);
                row = sheet.createRow(0);
                initExcelBillCell(row, style);
                count++;
                index = 0;
            }
            row = sheet.createRow(++index);
            initExcelBillData(row, bill, style);
        }
        File file = new File(path+"办单管理" + CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss") + ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }

    private static void initBillExcelWidth(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 20 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 20 * 256);
        sheet.setColumnWidth(13, 20 * 256);
        sheet.setColumnWidth(14, 20 * 256);
    }

    private static void initExcelBillCell(HSSFRow row, HSSFCellStyle style) {
        HSSFCell cell = null;
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("组别");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("开单人");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("客户");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("季度");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("男/女装");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("工厂");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("办单单号");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("发单日期");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("打印日期");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("办类");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("衫型");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("办单件数");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("办期");
        cell.setCellStyle(style);


        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("办单进度");
        cell.setCellStyle(style);

        cell = row.createCell(14);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("洗水类型");
        cell.setCellStyle(style);

    }

    private static void initExcelBillData(HSSFRow row, FactoryBillView bill, HSSFCellStyle style) {
        HSSFCell cell = null;


        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getGroupId()) ? "" : bill.getGroupId());
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getBillOperator()) ? "" : bill.getBillOperator());
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getCustomerId()) ? "" : bill.getCustomerId());
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getSeason()) ? "" : bill.getSeason());
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getSex()) ? "" : bill.getSex());
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getFactory()) ? "" : bill.getFactory());
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(bill.getBillNo());
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(CommonUtil.getDateString(bill.getBillDate(), "yyyy-MM-dd"));
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(CommonUtil.getDateString(bill.getPrintDate(), "yyyy-MM-dd"));
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getType()) ? "" : bill.getType());
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getShirtType()) ? "" : bill.getShirtType());
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(bill.getBillQty());
        /**/
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(CommonUtil.getDateString(bill.getEndDate(), "yyyy-MM-dd"));
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        if (CommonUtil.isNotBlank(bill.getProgress())) {
            Integer token = Integer.parseInt(bill.getProgress().split("-")[0]);
            String type = bill.getProgress().split("-")[1];
           /* String tokenName = CacheManager.getFactoryTokenByToken(token).getName();*/
          /*  String progress = tokenName;*/
            /*if ((CacheManager.getFactoryTokenByToken(token).getTypes()).split(",").length > 1) {
                progress = tokenName + findTypeName(type);
            }
            if (type.equals(FactoryConstant.TaskType.Back)) {
                progress = tokenName + findTypeName(type);
            }*/
          /*  cell.setCellValue(progress);*/
        }

        cell.setCellStyle(style);

        cell = row.createCell(14);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CommonUtil.isBlank(bill.getWashType()) ? "" : bill.getWashType());
        cell.setCellStyle(style);
    }

    public static List<BillSchedule> covertToBillSchedule(List<FactoryBill> billList, List<BillSchedule> scheduleList, User user) {
        String updateDate =  CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
        List<BillSchedule> list = new ArrayList<BillSchedule>();

        for(FactoryBill bill : billList){
            bill.setIsSchedule("Y");
            for(BillSchedule schedule : scheduleList){
                BillSchedule billSchedule = new BillSchedule();
                billSchedule.setSchedule(schedule.getSchedule());
                billSchedule.setToken(schedule.getToken());
                billSchedule.setBillNo(bill.getBillNo());
                billSchedule.setBillDate(bill.getBillDate());
                billSchedule.setEndDate(bill.getEndDate());
                billSchedule.setUploadNo(bill.getUploadNo());
                billSchedule.setUpdateId(user.getCode());
                billSchedule.setUpdateTime(updateDate);
              /* if( CacheManager.getFactoryTokenByToken(schedule.getToken()).getIsLast().equals("Y")){
                   billSchedule.setType("I");
               }else{
                   billSchedule.setType("O");
               }*/
               list.add(billSchedule);
            }
        }
        return list;
    }

    @SuppressWarnings({ "null", "deprecation" })
    public static File writeBillScheduleFile(List<FactoryBillView> list, List<BillSchedule> scheduleList) throws Exception {
        Map<String,List<BillSchedule>> billScheduleListMap = new HashMap<String,List<BillSchedule>>();
        for(BillSchedule s: scheduleList){
			/*billScheduleMap.put(s.getBillNo()+s.getBillDate()+s.getEndDate()+s.getUploadNo()+"."+s.getToken()+s.getType(), s);*/
            if(billScheduleListMap.containsKey(s.getBillNo())){
                List<BillSchedule> billScheduleList = billScheduleListMap.get(s.getBillNo());
                billScheduleList.add(s);
                billScheduleListMap.put(s.getBillNo(), billScheduleList);

            }else{
                List<BillSchedule> billScheduleList = new ArrayList<BillSchedule>();
                billScheduleList.add(s);
                billScheduleListMap.put(s.getBillNo(), billScheduleList);
            }

        }



        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("排期记录");
        HSSFCellStyle style = taskExcelBook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中   
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

        HSSFCellStyle redStyle = CreateColorCellStyle(taskExcelBook,"red");
        HSSFCellStyle greentyle = CreateColorCellStyle(taskExcelBook,"green");
        HSSFCellStyle yellowStyle = CreateColorCellStyle(taskExcelBook,"yellow");

        if(CommonUtil.isBlank(list)){
            sheet=taskExcelBook.createSheet();
            File file = new File("排期记录"+CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss")+".xls");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            taskExcelBook.write(fos);
            fos.flush();
            fos.close();
            return file;
        }
        initBillScheduleExcelWidth(sheet);
        HSSFRow row = null;
        row = sheet.createRow(0);
        initExcelBillScheduleCell(row,style);
        int count = 1;
        int index=0;
        for (FactoryBillView bill:list) {

            List<BillSchedule> billScheduleList =billScheduleListMap.get(bill.getBillNo());

            if(billScheduleList == null){
                if(index>maxRowIndex){
                    sheet = taskExcelBook.createSheet("排期记录"+count);
                    initBillScheduleExcelWidth(sheet);
                    row = sheet.createRow(0);
                    initExcelBillScheduleCell(row, style);
                    count++;
                    index = 0;
                }
                row = sheet.createRow(++index);
                initExcelBillScheduleData(row,bill,null,style,redStyle,yellowStyle,greentyle);
            }else{

                int startMerageIndex = index+1;
                int endMerageIndex = index + billScheduleList.size();
                if(endMerageIndex>maxRowIndex){
                    if(index>endMerageIndex){
                        sheet = taskExcelBook.createSheet("排期记录"+count);
                        initBillScheduleExcelWidth(sheet);
                        row = sheet.createRow(0);
                        initExcelBillScheduleCell(row,style);
                        count++;
                        index = 0;
                    }
                }
                for(int j = 0; j < billScheduleList.size();j++){
                    row = sheet.createRow(++index);
                    initExcelBillScheduleData(row,bill,billScheduleList.get(j),style,redStyle,yellowStyle,greentyle);
                }
                for(int k = 0; k < 19;k++){
                    sheet.addMergedRegion(new Region(startMerageIndex, (short) (k), endMerageIndex,
                            (short) (k)));
                }

            }

        }
        File file = new File(CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss")+".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;



    }
    private static void initExcelBillScheduleData(HSSFRow row,
                                                  FactoryBillView bill,BillSchedule billSchedule, HSSFCellStyle style,
                                                  HSSFCellStyle redStyle,HSSFCellStyle yellowStyle,HSSFCellStyle greentyle) throws Exception {

        initExcelBillData(row, bill, style);
        HSSFCell cell = null;

        Date outDate = bill.getOutDate();
        int ct = 0;
        if(CommonUtil.isNotBlank(outDate)){
            cell = row.createCell(15);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(CommonUtil.getDateString(bill.getOutDate(), "yyyy-MM-dd HH:mm:ss"));
            cell.setCellStyle(style);
            if(CommonUtil.isNotBlank(bill.getPrintDate())){
                ct = FactoryUtil.getTotWorkDay(CommonUtil.getDateString(bill.getPrintDate(), "yyyy-MM-dd"), CommonUtil.getDateString(bill.getOutDate(), "yyyy-MM-dd"));
            }else{
                ct = FactoryUtil.getTotWorkDay(CommonUtil.getDateString(bill.getBillDate(), "yyyy-MM-dd"), CommonUtil.getDateString(bill.getOutDate(), "yyyy-MM-dd"));
            }


            cell = row.createCell(16);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(ct+"天");
            cell.setCellStyle(style);
        }
        if(CommonUtil.isNotBlank(bill.getScheduleStartDate()) &&  CommonUtil.isNotBlank(bill.getScheduleEndDate())){

            int totDay = FactoryUtil.getTotWorkDay(bill.getScheduleStartDate(), bill.getScheduleEndDate());
            bill.setSchedulePeriod(totDay);
        }
        if(CommonUtil.isNotBlank(bill.getSchedulePeriod())){
            cell = row.createCell(17);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(bill.getSchedulePeriod()+"天");
            cell.setCellStyle(style);
            if(CommonUtil.isNotBlank(outDate)){
                long status = ct-bill.getSchedulePeriod();


                cell = row.createCell(18);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(status+"天");
                if(status <= 0){
                    cell.setCellStyle(greentyle);
                }else if(status == 1){
                    cell.setCellStyle(yellowStyle);
                }else if(status > 1){
                    cell.setCellStyle(redStyle);
                }
            }
        }

        if(CommonUtil.isNotBlank(billSchedule)) {


            cell = row.createCell(19);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
           /* cell.setCellValue(CacheManager.getFactoryTokenByToken(billSchedule.getToken()).getName());*/
            cell.setCellStyle(style);

            cell = row.createCell(20);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(findTypeName(billSchedule.getType()));
            cell.setCellStyle(style);

            cell = row.createCell(21);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(billSchedule.getSchedule());
            cell.setCellStyle(style);

            cell = row.createCell(22);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(CommonUtil.isBlank(billSchedule.getTaskTime())?"":billSchedule.getTaskTime());
            cell.setCellStyle(style);
            if(CommonUtil.isNotBlank(billSchedule.getTaskTime())){


                long status = CommonUtil.dateInterval(billSchedule.getSchedule(), billSchedule.getTaskTime());

                String val ="";

                cell = row.createCell(23);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                if(status <= 0){
                    val = "<=0天";
                    cell.setCellStyle(greentyle);
                }else if(status == 1){
                    val = "<=1天";
                    cell.setCellStyle(yellowStyle);
                }else if(status > 1){
                    val = ">1天";
                    cell.setCellStyle(redStyle);
                }
                cell.setCellValue(val);




            }
            cell = row.createCell(24);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CommonUtil.isBlank(billSchedule.getRemark())?"":billSchedule.getRemark());
            cell.setCellStyle(style);


        }



    }

    private static HSSFCellStyle CreateColorCellStyle(HSSFWorkbook taskExcelBook, String type) {
        HSSFCellStyle cellStyle = taskExcelBook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        switch (type){
            case "red":
                cellStyle.setFillForegroundColor(HSSFColor.RED.index);
                break;
            case "green":
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                break;
            case "yellow":
                cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
                break;

        }

        return  cellStyle;
    }

    private static void initExcelBillScheduleCell(HSSFRow row,
                                                  HSSFCellStyle style) {
        initExcelBillCell(row, style);
        HSSFCell cell = null;
        cell = row.createCell(15);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("出货日期");
        cell.setCellStyle(style);

        cell = row.createCell(16);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("生产周期");
        cell.setCellStyle(style);

        cell = row.createCell(17);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("排期周期");
        cell.setCellStyle(style);

        cell = row.createCell(18);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("状态");
        cell.setCellStyle(style);

        cell = row.createCell(19);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("流程");
        cell.setCellStyle(style);

        cell = row.createCell(20);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("过程");
        cell.setCellStyle(style);

        cell = row.createCell(21);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("预计完成时间");
        cell.setCellStyle(style);

        cell = row.createCell(22);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("实际时间");
        cell.setCellStyle(style);




        cell = row.createCell(23);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("状态");
        cell.setCellStyle(style);

        cell = row.createCell(24);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("备注");
        cell.setCellStyle(style);

    }


    private static void initBillScheduleExcelWidth(HSSFSheet sheet) {

        initBillExcelWidth(sheet);
        sheet.setColumnWidth(14, 20 * 256);
        sheet.setColumnWidth(15, 20 * 256);
        sheet.setColumnWidth(16, 20 * 256);
        sheet.setColumnWidth(17, 20 * 256);
        sheet.setColumnWidth(18, 20 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 20 * 256);
        sheet.setColumnWidth(21, 20 * 256);
        sheet.setColumnWidth(22, 20 * 256);
        sheet.setColumnWidth(23, 20 * 256);
        sheet.setColumnWidth(24, 50 * 256);

    }

    public static void coverToScehdule(List<BillSchedule> billScheduleList) {
        for(BillSchedule s : billScheduleList){
            /*Token t = CacheManager.getFactoryTokenByToken(s.getToken());
            s.setTokenName(t.getName());
            s.setTokenIndex(t.getSortIndex());*/
        }
        Collections.sort(billScheduleList,new Comparator<BillSchedule>() {
            @Override
            public int compare(BillSchedule o1, BillSchedule o2) {
                return o1.getTokenIndex().compareTo(o2.getTokenIndex());
            }
        });
    }
}
