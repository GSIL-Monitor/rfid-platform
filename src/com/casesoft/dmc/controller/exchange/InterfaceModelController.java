package com.casesoft.dmc.controller.exchange;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.exchange.DataSrcInfo;
import com.casesoft.dmc.model.exchange.InterfaceModel;
import com.casesoft.dmc.model.exchange.InterfaceModelDtl;
import com.casesoft.dmc.service.exchange.DataSourceService;
import com.casesoft.dmc.service.exchange.InterfaceModelService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-06-06.
 */
@Controller
@RequestMapping("/exchange/interfaceModel")
public class InterfaceModelController extends BaseController implements IBaseInfoController<InterfaceModel> {

    @Autowired
    private InterfaceModelService interfaceModelService;
    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/exchange/interfaceModel";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<InterfaceModel> findPage(Page<InterfaceModel> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.interfaceModelService.findPage(page, filters);
        return page;
    }

    @RequestMapping(value = "/showEdit")
    @ResponseBody
    public ModelAndView showEdit(String code){
        ModelAndView model=new ModelAndView();
        model.setViewName("/views/exchange/interfaceModel_edit");
        InterfaceModel interfaceModel=this.interfaceModelService.findByCode(code);
        model.addObject("InterfaceModel",interfaceModel);
        model.addObject("isEdit","Y");
        return model;
    }

    @Override
    public List<InterfaceModel> list() throws Exception {
        return null;
    }


    @Override
    public MessageBox save(InterfaceModel entity) throws Exception {
        try {
            this.interfaceModelService.save(entity);
        }catch (Exception e){
            return returnFailInfo("error",e.getMessage());
        }
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public MessageBox saveInterfaceModel(String origDsId,String origTableName,String destDsId,String destTableName)throws Exception {
        InterfaceModel interfaceModel=new InterfaceModel();
        String code=this.interfaceModelService.findMaxCode();
        interfaceModel.setCode(code);
        interfaceModel.setDestDs(destDsId);
        interfaceModel.setDestTable(destTableName);
        interfaceModel.setOrigDs(origDsId);
        interfaceModel.setOrigTable(origTableName);
        MessageBox saveMsg=save(interfaceModel);
        if (!saveMsg.getSuccess()){
            return saveMsg;
        }
        MessageBox origMsg=getColumns(origDsId,origTableName);
        if (!origMsg.getSuccess()){
            return origMsg;
        }
        MessageBox destMsg=getColumns(destDsId,destTableName);
        if (!destMsg.getSuccess()){
            return destMsg;
        }
        JSONObject  columnList =new JSONObject();
        columnList.put("orig",origMsg.getResult());
        columnList.put("dest",destMsg.getResult());
        columnList.put("modelCode",code);
        return returnSuccessInfo("ok",columnList);
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

    @RequestMapping(value = "/getTables")
    @ResponseBody
    public MessageBox getTables(String dataSourceId) {
        if (CommonUtil.isBlank(dataSourceId)){
            return returnSuccessInfo("ok");
        }
        List tableList = new ArrayList();
        DataSrcInfo dataSrcInfo = this.dataSourceService.findById(dataSourceId);
        String user = dataSrcInfo.getDbUser().trim();
        String password = dataSrcInfo.getDbPass().trim();
        String ip = dataSrcInfo.getDsIp().trim();
        String port = dataSrcInfo.getDsPort().trim();
        String dbName = dataSrcInfo.getDbName().trim();
        String getAllTableSQL = "";
        Connection conn = null;
        switch (dataSrcInfo.getType()) {
            case Constant.DataSource.MYSQL:
                try {
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, user, password);
                    getAllTableSQL = String.format(ExcgConstant.Mysql.Select_All_Table_Name, dbName);
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
            case Constant.DataSource.ORACLE:
                try {
                    String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    getAllTableSQL = ExcgConstant.Oracle.Select_All_Table_Name;
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
            case Constant.DataSource.SQL_Server:
                try {
                    String url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + dbName;
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    getAllTableSQL = ExcgConstant.SqlServer.Select_All_Table_Name;
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
        }

        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement(getAllTableSQL);
            result = pstmt.executeQuery();
            while (result.next()) {
                tableList.add(result.getString("table_name"));
            }
        } catch (SQLException e) {
            return returnFailInfo("error", e.getMessage());
        } finally {
            try {
                closeDB(result, pstmt, conn);
            } catch (Exception e) {
                return returnFailInfo("error", e.getMessage());
            }
        }

        return returnSuccessInfo("ok", tableList);
    }

    @RequestMapping(value = "/get2Columns")
    @ResponseBody
    public MessageBox get2Columns(String origDsId,String origTableName,String destDsId,String destTableName){
        MessageBox origMsg=getColumns(origDsId,origTableName);
        if (!origMsg.getSuccess()){
            return origMsg;
        }
        MessageBox destMsg=getColumns(destDsId,destTableName);
        if (!destMsg.getSuccess()){
            return destMsg;
        }
        JSONObject  columnList =new JSONObject();
        columnList.put("orig",origMsg.getResult());
        columnList.put("dest",destMsg.getResult());
        return returnSuccessInfo("ok",columnList);
    }


    @RequestMapping(value = "/getColumns")
    @ResponseBody
    public MessageBox getColumns(String dataSourceId, String tableName) {
        List tableList = new ArrayList();
        DataSrcInfo dataSrcInfo = this.dataSourceService.findById(dataSourceId);
        String user = dataSrcInfo.getDbUser().trim();
        String password = dataSrcInfo.getDbPass().trim();
        String ip = dataSrcInfo.getDsIp().trim();
        String port = dataSrcInfo.getDsPort().trim();
        String dbName = dataSrcInfo.getDbName().trim();
        String getColumnsSQL = "";
        Connection conn = null;
        switch (dataSrcInfo.getType()) {
            case Constant.DataSource.MYSQL:
                try {
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, user, password);
                    getColumnsSQL = String.format(ExcgConstant.Mysql.Select_All_Column_Name, dbName, tableName);
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
            case Constant.DataSource.ORACLE:
                try {
                    String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    getColumnsSQL = String.format(ExcgConstant.Oracle.Select_All_Column_Name, tableName);
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
            case Constant.DataSource.SQL_Server:
                try {
                    String url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + dbName;
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    getColumnsSQL = String.format(ExcgConstant.SqlServer.Select_All_Column_Name, tableName);
                } catch (Exception e) {
                    return returnFailInfo("error", e.getMessage());
                }
                break;
        }

        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement(getColumnsSQL);
            result = pstmt.executeQuery();
            while (result.next()) {
                tableList.add(result.getString("column_name"));
            }
        } catch (SQLException e) {
            return returnFailInfo("error", e.getMessage());
        } finally {
            try {
                closeDB(result, pstmt, conn);
            } catch (Exception e) {
                return returnFailInfo("error", e.getMessage());
            }
        }
        return returnSuccessInfo("ok", tableList);
    }

    public void closeDB(ResultSet result, PreparedStatement pstmt, Connection conn) throws Exception {
        if (result != null)
            result.close();
        if (pstmt != null)
            pstmt.close();
        if (conn != null)
            conn.close();
    }


    @RequestMapping(value = "/saveDtl")
    @ResponseBody
    public MessageBox saveDtl(InterfaceModelDtl interfaceModelDtl){
        if (CommonUtil.isBlank(interfaceModelDtl.getId())){
            String id=this.interfaceModelService.findDtlMaxId();
            interfaceModelDtl.setId(id);
        }
        try{
            this.interfaceModelService.saveDtl(interfaceModelDtl);
            return returnSuccessInfo("ok");
        } catch (Exception e) {
            return returnFailInfo("error", e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteDtl")
    @ResponseBody
    public MessageBox deleteDtl(String id){
        this.interfaceModelService.deleteDtl(id);
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/listDtl")
    @ResponseBody
    public List<InterfaceModelDtl> listDtl(String modelCode){
        this.logAllRequestParams();
        List<InterfaceModelDtl> listDtl=this.interfaceModelService.findDtl(modelCode);
        return listDtl;

    }

}
