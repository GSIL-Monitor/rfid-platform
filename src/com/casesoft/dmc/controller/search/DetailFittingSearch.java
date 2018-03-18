package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailFittingDao;
import com.casesoft.dmc.model.search.DetailFittingView;
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
@RequestMapping("/search/detailFittingViewSearch")
public class DetailFittingSearch {
	
	@Autowired
	DetailFittingDao detailFittingDao;
	@RequestMapping(value = "/index")
    public String index(){
    	return "/views/search/detailFittingViewSearch";
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataSourceResult = detailFittingDao.getList(request);
        for(DetailFittingView dtl : (List<DetailFittingView>) dataSourceResult.getData()){
            Unit shop = CacheManager.getUnitByCode(dtl.getWarehId());
            if(CommonUtil.isNotBlank(shop)){
                dtl.setWarehName(shop.getName());
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
