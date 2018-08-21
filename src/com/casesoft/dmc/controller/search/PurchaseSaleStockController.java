package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.search.PurchaseSaleStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.search.PurchaseSaleStockService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvin on 2018/2/3.
 */
@RequestMapping(value = "/search/purchaseSaleStock")
@Controller
public class PurchaseSaleStockController extends BaseController implements IBaseInfoController<PurchaseSaleStock>{

    @Autowired
    private PurchaseSaleStockService purchaseSaleStockService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<PurchaseSaleStock> findPage(Page<PurchaseSaleStock> page) throws Exception {
        this.logAllRequestParams();
        String rootPath = session.getServletContext().getRealPath("");
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.purchaseSaleStockService.findPage(page, filters);
        PurchaseSaleStockUtil.convertToPurchaseSaleSotck(page.getRows(),rootPath,"");
        return page;
    }
    @RequestMapping(value = "/listWS")
    @ResponseBody
    @Override
    public List<PurchaseSaleStock> list() throws Exception {
        this.logAllRequestParams();
        String rootPath = session.getServletContext().getRealPath("");
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<PurchaseSaleStock> list = this.purchaseSaleStockService.find(filters);
        PurchaseSaleStockUtil.convertToPurchaseSaleSotck(list,rootPath,"");
        return list;
    }

    @Override
    public MessageBox save(PurchaseSaleStock entity) throws Exception {
        return null;
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
        this.logAllRequestParams();

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/search/purchaseSaleStock");
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/exportExcel")
    @ResponseBody
    public void exportExcels(String warehId,String sku) throws Exception {
        this.logAllRequestParams();
        String rootPath = session.getServletContext().getRealPath("");
        List<PurchaseSaleStock> PurchaseSaleStocks=this.purchaseSaleStockService.exportExcels(warehId,sku);
        for (PurchaseSaleStock d : PurchaseSaleStocks) {
            if (CommonUtil.isBlank(d.getImgUrl())) {
                //没有图片设置默认图片
                d.setImgUrl("/product/photo/noImg.png");
            }
        }
        PurchaseSaleStockUtil.convertToPurchaseSaleSotck(PurchaseSaleStocks, rootPath,ImgUtil.ImgExt.small);

        ExportParams params = new ExportParams("进销存", "sheet1", ExcelType.XSSF);
        String path = Constant.Folder.Report_File_Folder;
        String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
        File files=new File(path + "\\进销存-" +  dateString);

        if(!files.exists())
            files.mkdirs();
        File file =new File(files,"进销存-" +  dateString + ".xlsx");
        //Workbook workbook = ExcelExportUtil.exportExcel(params, PurchaseSaleStock.class, PurchaseSaleStocks);
        Workbook workbook = ExcelExportUtil.exportBigExcel(params, PurchaseSaleStock.class, PurchaseSaleStocks);
        ExcelExportUtil.closeExportBigExcel();
        //String dateString = CommonUtil.getDateString(new Date(), "yyyyMMdd HH_mm_ss");
        // FileOutputStream fos = new FileOutputStream(path + "\\销售明细-" +  dateString + ".xlsx");
        FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
        workbook.write(fos);
        fos.close();
        FileWriter fileWriter=new FileWriter(file.getName(),true);
        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
        bufferedWriter.close();
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;";
        this.outFile("进销存-" +  dateString+ ".xlsx" , file, contentType);

    }
}
