package com.casesoft.dmc.controller.location;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.location.AdministrativeRegion;
import com.casesoft.dmc.model.location.Regional;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.location.RegionalService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */
@Controller
@RequestMapping(value = "/location/Regional")
public class RegionalController extends BaseController implements IBaseInfoController<Regional> {
    @Autowired
    private RegionalService regionalService;
    @Autowired
    private UnitService unitService;
    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<Regional> findPage(Page<Regional> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.regionalService.findPage(page,filters);

        return page;
    }

    @Override
    public List<Regional> list() throws Exception {
        return null;
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Regional entity) throws Exception {
        AdministrativeRegion administrativeRegion = this.regionalService.findAdministrativeRegion(entity.getProvince());
        entity.setProvinceName(administrativeRegion.getName());
        AdministrativeRegion administrativeRegion1 = this.regionalService.findAdministrativeRegion(entity.getCity());
        entity.setCityName(administrativeRegion1.getName());
        AdministrativeRegion administrativeRegion2 = this.regionalService.findAdministrativeRegion(entity.getArea());
        entity.setAreaName(administrativeRegion2.getName());
        this.regionalService.save(entity);
        return returnSuccessInfo("保存成功");
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
    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/location/regional";
    }
    @RequestMapping(value="/findprovince")
    @ResponseBody
    public MessageBox findprovince(){
        List<AdministrativeRegion> AdministrativeRegions = this.regionalService.findprovince();
        return returnSuccessInfo("查询成功",AdministrativeRegions);
    }
    @RequestMapping(value="/findcity")
    @ResponseBody
    public MessageBox findcity(String provinceid){
        List<AdministrativeRegion> findcity = this.regionalService.findcity(provinceid);
        return returnSuccessInfo("查询成功",findcity);
    }
    @RequestMapping(value="/findarea")
    @ResponseBody
    public MessageBox findarea(String cityid){
        List<AdministrativeRegion> findarea = this.regionalService.findarea(cityid);
        return returnSuccessInfo("查询成功",findarea);
    }
    @RequestMapping(value="/findallUnit")
    @ResponseBody
    public MessageBox findallUnit(){
        List<Unit> units = this.regionalService.findallUnit();
        return returnSuccessInfo("查询成功",units);
    }
    @RequestMapping(value="/findallRegional")
    @ResponseBody
    public MessageBox findallRegional(){
        List<Regional> Regionalall = this.regionalService.getAll();
        return  returnSuccessInfo("查询成功",Regionalall);
    }
    @RequestMapping(value="/pushMessage")
    @ResponseBody
    public MessageBox pushMessage(String id){
        Unit unit = this.unitService.getunitbyId(id);
        if(CommonUtil.isNotBlank(unit.getAreasId())&&CommonUtil.isNotBlank(unit.getOwnerids())){
            this.regionalService.pushMessage(id,unit);
        }
        return returnSuccessInfo("查询成功","推送成功");
    }

}
