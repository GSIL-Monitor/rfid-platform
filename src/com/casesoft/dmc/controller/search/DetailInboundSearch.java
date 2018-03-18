package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.dao.search.DetailInboundDao;
import com.casesoft.dmc.model.search.DetailInboundView;
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
@RequestMapping("/search/detailInboundViewSearch")
public class DetailInboundSearch {
	
	@Autowired
	DetailInboundDao detailInboundDao;
	@RequestMapping(value = "/index")
    public String index(){
    	return "/views/search/detailInboundViewSearch";
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataResult = detailInboundDao.getList(request);
        for(DetailInboundView dtl :(List<DetailInboundView>) dataResult.getData()){

            if(CommonUtil.isNotBlank(dtl.getDestid())){
                Unit dest = CacheManager.getUnitByCode(dtl.getDestid());
                if(CommonUtil.isNotBlank(dest)){
                    dtl.setDestName(dest.getName());
                }
            }
            if(CommonUtil.isNotBlank(dtl.getOrigid())){
                Unit orig = CacheManager.getUnitByCode(dtl.getOrigid());
                if(CommonUtil.isNotBlank(orig)){
                    dtl.setOrigName(orig.getName());
                }
            }
            if(CommonUtil.isNotBlank(dtl.getDestUnitId())){
                Unit destUnit = CacheManager.getUnitByCode(dtl.getDestUnitId());
                if(CommonUtil.isNotBlank(destUnit)){
                    dtl.setDestUnitName(destUnit.getName());
                }
            }
        }
        return dataResult;
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
