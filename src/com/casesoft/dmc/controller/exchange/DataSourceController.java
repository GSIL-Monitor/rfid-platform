package com.casesoft.dmc.controller.exchange;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.exchange.DataSrcInfo;
import com.casesoft.dmc.service.exchange.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-06-06.
 */
@Controller
@RequestMapping("/exchange/datasource")
public class DataSourceController extends BaseController implements IBaseInfoController<DataSrcInfo> {

    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/exchange/dataSource";
    }


    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<DataSrcInfo> findPage(Page<DataSrcInfo> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.dataSourceService.findPage(page, filters);
        return page;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<DataSrcInfo> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<DataSrcInfo> list=this.dataSourceService.find(filters);
        return list;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(DataSrcInfo entity) throws Exception {
        this.logAllRequestParams();
        MessageBox msg=connectionTest(entity);
        if (msg.getSuccess()){
            if (CommonUtil.isBlank(entity.getId())){
                String id =this.dataSourceService.findMaxId(entity.getType());
                entity.setId(id);
                entity.setStatus("Y");
            }
            this.dataSourceService.save(entity);
            return returnSuccessInfo("ok");
        }else{
            return returnFailInfo("error","数据库连接无效");
        }

    }

    @Override
    public MessageBox edit(String id) throws Exception {

        return null;
    }

    @RequestMapping(value = "/showEdit")
    @ResponseBody
    public ModelAndView showEdit(String id) throws Exception{
        ModelAndView model=new ModelAndView();
        model.setViewName("/views/exchange/dataSource_edit");
        DataSrcInfo dataSrcInfo=this.dataSourceService.findById(id);
        model.addObject("DataSrcInfo",dataSrcInfo);
        return model;
    }

    @Override
    public MessageBox delete(String id) throws Exception {
        try {
            this.dataSourceService.delete(id);
        }catch (Exception e){
            return returnFailInfo("error",e.getMessage());
        }
        return returnSuccessInfo("ok");
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @RequestMapping(value = "/connectionTest")
    @ResponseBody
    public MessageBox connectionTest(DataSrcInfo dataSrcInfo) throws Exception{

        String user=dataSrcInfo.getDbUser().trim();
        String password=dataSrcInfo.getDbPass().trim();
        String ip=dataSrcInfo.getDsIp().trim();
        String port=dataSrcInfo.getDsPort().trim();
        String dbName=dataSrcInfo.getDbName().trim();
        Connection conn=null;
        switch (dataSrcInfo.getType()){
            case Constant.DataSource.MYSQL:
                try {
                    String url="jdbc:mysql://"+ip+":"+port+"/"+dbName;
                    Class.forName("com.mysql.jdbc.Driver");
                     conn = DriverManager.getConnection(url, user, password);
                    if (!conn.isClosed()){
                        conn.close();
                        return returnSuccessInfo("ok");
                    }
                }catch (Exception e){
                    return returnFailInfo("error",e.getMessage());
                }
                break;
            case Constant.DataSource.ORACLE:
                try{
                    String url="jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    if (!conn.isClosed()){
                        conn.close();
                        return returnSuccessInfo("ok");
                    }
                }catch (Exception e){
                    return returnFailInfo("error",e.getMessage());
                }
                break;
            case Constant.DataSource.SQL_Server:
                try{
                    String url="jdbc:sqlserver://"+ip+":"+port+";databaseName="+dbName;
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    conn = DriverManager.getConnection(url, user, password);
                    if (!conn.isClosed()){
                        conn.close();
                        return returnSuccessInfo("ok");
                    }
                }catch (Exception e){
                    return returnFailInfo("error",e.getMessage());
                }
                break;
        }

        return returnFailInfo("error");
    }

}
