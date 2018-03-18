package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailInventoryDao;
import com.casesoft.dmc.model.search.DetailInventoryView;
import com.casesoft.dmc.model.sys.Unit;
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

@Controller
@RequestMapping("/search/detailInventoryViewSearch")
public class DetailInventorySearch {
	
	@Autowired
	DetailInventoryDao detailInventoryDao;
	@RequestMapping(value = "/index")
    public String index(){
    	return "/views/search/detailInventoryViewSearch";
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataSourceResult = detailInventoryDao.getList(request);
        for(DetailInventoryView dtl : (List<DetailInventoryView>) dataSourceResult.getData()){
            Unit warehHouse = CacheManager.getUnitByCode(dtl.getWarehId());
            if(CommonUtil.isNotBlank(warehHouse)){
                dtl.setWarehName(warehHouse.getName());
            }
        }
        return dataSourceResult;
    }
    
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    void export(String fileName, String base64, String contentType,
            HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
    
}
