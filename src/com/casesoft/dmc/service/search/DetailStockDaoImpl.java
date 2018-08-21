package com.casesoft.dmc.service.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailStockDao;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.DetailStockCodeView;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.stock.CodeFirstTime;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Transactional
@Component
public class DetailStockDaoImpl implements DetailStockDao {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CodeFirstTimeService codeFirstTimeService;
    @Autowired
    private UnitService unitService;


    @Override
    public DataSourceResult getList(DataSourceRequest request) {
        valueNew(request);
        DataSourceResult dataSourceResult = request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailStockView.class);
        for (DetailStockView dtl : (List<DetailStockView>) dataSourceResult.getData()) {
            dtl.setInStockPrice((double) Math.round(dtl.getPrice() * dtl.getQty()));
            Unit unit = CacheManager.getUnitById(dtl.getWarehId());
            if(CommonUtil.isNotBlank(unit)){
                dtl.setWarehName(unit.getName());
            }
        }
        return dataSourceResult;
    }

    @Override
    public DataSourceResult getCodeList(DataSourceRequest request) {
        valueNew(request);
        DataSourceResult dataSourceResult = request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailStockCodeView.class);
        List<String> warehouseCodeList = new ArrayList<>();
        for (DetailStockCodeView dtl : (List<DetailStockCodeView>) dataSourceResult.getData()) {
            warehouseCodeList.add(dtl.getCode() + "-" + dtl.getWarehId());
        }
        String idListStr = TaskUtil.getSqlStrByList(warehouseCodeList, CodeFirstTime.class, "id");
        List<CodeFirstTime> firstInStockList = this.codeFirstTimeService.findByIds(idListStr);
        Map<String, CodeFirstTime> firstInStockMap = new HashMap<>();
        for (CodeFirstTime first : firstInStockList) {
            firstInStockMap.put(first.getCode(), first);
        }
        for (DetailStockCodeView dtl : (List<DetailStockCodeView>) dataSourceResult.getData()) {
            CodeFirstTime firstInStock = firstInStockMap.get(dtl.getCode());
            if (CommonUtil.isNotBlank(firstInStock)) {
                dtl.setFirstInStockTime(firstInStock.getFirstTime());
                long days = ((new Date()).getTime() - firstInStock.getFirstTime().getTime()) / (1000 * 3600 * 24);
//			    double days = Math.ceil(((new Date()).getTime() - firstInStock.getFirstTime().getTime()) / (1000 * 3600 * 24));
                dtl.setInStockDays(days);
                Unit unit = CacheManager.getUnitById(dtl.getWarehId());
                if(CommonUtil.isNotBlank(unit)){
                    dtl.setWarehName(unit.getName());
                }
            }
        }

        return dataSourceResult;
    }

    @Override
    public DataSourceResult getCodeList(DataSourceRequest request, HttpSession session) {
        valueNew(request);
        DataSourceResult dataSourceResult = request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailStockCodeView.class);
        String rootPath = session.getServletContext().getRealPath("/");
        List<String> warehouseCodeList = new ArrayList<>();
        for (DetailStockCodeView dtl : (List<DetailStockCodeView>) dataSourceResult.getData()) {
            warehouseCodeList.add(dtl.getCode() + "-" + dtl.getWarehId());
        }
        String idListStr = TaskUtil.getSqlStrByList(warehouseCodeList, CodeFirstTime.class, "id");
        List<CodeFirstTime> firstInStockList = this.codeFirstTimeService.findByIds(idListStr);
        Map<String, CodeFirstTime> firstInStockMap = new HashMap<>();
        for (CodeFirstTime first : firstInStockList) {
            firstInStockMap.put(first.getCode(), first);
        }
        for (DetailStockCodeView dtl : (List<DetailStockCodeView>) dataSourceResult.getData()) {
            String url = StyleUtil.returnImageUrl(dtl.getStyleId(),rootPath);
            dtl.setUrl(url);
            CodeFirstTime firstInStock = firstInStockMap.get(dtl.getCode());
            if (CommonUtil.isNotBlank(firstInStock)) {
                dtl.setFirstInStockTime(firstInStock.getFirstTime());
                long days = ((new Date()).getTime() - firstInStock.getFirstTime().getTime()) / (1000 * 3600 * 24);
                dtl.setInStockDays(days);
                Unit unit = CacheManager.getUnitById(dtl.getWarehId());
                if(CommonUtil.isNotBlank(unit)){
                    dtl.setWarehName(unit.getName());
                }
            }
        }
        return dataSourceResult;
    }

    @Override
    public DataSourceResult getStyleList(DataSourceRequest request, HttpSession session) {
        valueNew(request);
        DataSourceResult dataSourceResult = request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailStockChatView.class);
        String rootPath = session.getServletContext().getRealPath("/");
        for(DetailStockChatView d : (List<DetailStockChatView>) dataSourceResult.getData()){
            /*File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        File photo = new File(photos[0].getPath());
                        if(photo.getName().lastIndexOf(".")==-1){
                            //将无后缀名文件转为图片
                            File newFile = new File(photo.getParentFile(),photo.getName()+".png");
                            boolean flag = photo.renameTo(newFile);
                            if(flag) {
                                d.setUrl("/product/photo/" + d.getStyleId() + "/" + files[0].getName() + "/" + newFile.getName());
                            }else{
                            }
                        }else{
                            d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                        }
                    }
                }
            }*/
            String url = StyleUtil.returnImageUrl(d.getStyleId(), rootPath);
            d.setUrl(url);
        }
        return dataSourceResult;
    }
    @Override
    public DataSourceResult getList(DataSourceRequest request,HttpSession session) {
        valueNew(request);
        DataSourceResult dataSourceResult = request.toDataSourceResult(sessionFactory.getCurrentSession(), DetailStockView.class);
        String rootPath = session.getServletContext().getRealPath("/");
        for (DetailStockView dtl : (List<DetailStockView>) dataSourceResult.getData()) {
            String url = StyleUtil.returnImageUrl(dtl.getStyleId(),rootPath);
            dtl.setUrl(url);
            dtl.setInStockPrice((double) Math.round(dtl.getPrice() * dtl.getQty()));
            Unit unit = CacheManager.getUnitById(dtl.getWarehId());
            if(CommonUtil.isNotBlank(unit)){
                dtl.setWarehName(unit.getName());
            }
        }
        return dataSourceResult;
    }

    public DataSourceRequest valueNew(DataSourceRequest request){
        if(request.getFilter().getFilters().size()>0) {
            Object value = request.getFilter().getFilters().get(0).getValue();
            if (value.equals("DG")) {
                request.getFilter().getFilters().get(0).setField("groupId");
            }
            if (value.equals("JMS")) {
                request.getFilter().getFilters().get(0).setField("groupId");
            }
        }
        return request;
    }
}
