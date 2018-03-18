package com.casesoft.dmc.controller.wms.pl;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRecord;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.model.pl.PlWmsWarehouseBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsWarehouseBindingRelation;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.pl.PlWmsShopBindingRelationService;
import com.casesoft.dmc.extend.third.service.pl.PlWmsWarehouseBindingRelationService;
import com.casesoft.dmc.model.wms.WmsFloor;
import com.casesoft.dmc.model.wms.WmsFloorArea;
import com.casesoft.dmc.model.wms.WmsFloorView;
import com.casesoft.dmc.model.wms.WmsRack;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsFloor;
import com.casesoft.dmc.service.wms.pl.PlWarehouseWmsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Controller
@RequestMapping("/wms/pl/warehouseBind")
public class PlWarehouseWmsBindingController extends BaseController{

    @Autowired
    private PlWmsWarehouseBindingRelationService plWmsWarehouseBindingRelationService;
    @Autowired
    private PlWarehouseWmsViewService plWarehouseWmsViewService;

    private final static int WMSSHOP = 0;
    private final static int WMSFLOORAREA = 1;
    private final static int WMSFLOOR = 2;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/wms/pl/plWmsWarehouseBindingRelation";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public List read(@RequestBody RequestPageData<PlWarehouseWmsView> request) {
        this.logAllRequestParams();
        DataResult dataResult = this.plWarehouseWmsViewService.find(request);
        List<PlWarehouseWmsView> list = (List<PlWarehouseWmsView>) dataResult.getData();
        Map<String, WmsFloorTemp> map = new HashMap<String, WmsFloorTemp>();
        if (CommonUtil.isNotBlank(list)) {
            for (PlWarehouseWmsView plWarehouseWmsView : list) {
                map.put(plWarehouseWmsView.getFloorAreaParentCode(), new WmsFloorTemp(
                        plWarehouseWmsView.getFloorAreaParentCode(), plWarehouseWmsView.getFloorAreaParentName(), "", "", "", "", "", WMSSHOP
                ));
                map.put(plWarehouseWmsView.getFloorAreaId(), new WmsFloorTemp(
                        plWarehouseWmsView.getFloorAreaId(), plWarehouseWmsView.getFloorAreaName(), plWarehouseWmsView.getFloorAreaBarcode(), plWarehouseWmsView.getFloorAreaParentCode(), plWarehouseWmsView.getFloorAreaRemark(),
                        plWarehouseWmsView.getFloorAreaDeviceId(), plWarehouseWmsView.getFloorAreaImage(), WMSFLOORAREA
                ));
                if (CommonUtil.isNotBlank(plWarehouseWmsView.getFloorId())) {
                    map.put(plWarehouseWmsView.getFloorId(), new WmsFloorTemp(
                            plWarehouseWmsView.getFloorId(), plWarehouseWmsView.getFloorName(), plWarehouseWmsView.getFloorBarcode(), plWarehouseWmsView.getFloorAreaId(), plWarehouseWmsView.getFloorRemark(),
                            plWarehouseWmsView.getFloorDeviceID(), plWarehouseWmsView.getFloorImage(), WMSFLOOR
                    ));
                }

            }
        }
        Set<String> set = map.keySet();
        List<WmsFloorTemp> listWms = new ArrayList<WmsFloorTemp>();
        for (String in : set) {
            WmsFloorTemp wms = map.get(in);
            listWms.add(wms);
        }

        dataResult.setData(listWms);
        return listWms;
    }

    @RequestMapping(value = "detail")
    @ResponseBody
    public MessageBox detail(String id, Integer type) {
        List<PlWmsWarehouseBindingRelation> list = new ArrayList<PlWmsWarehouseBindingRelation>();
        switch (type) {
            case WMSSHOP:
                list = this.plWmsWarehouseBindingRelationService.findByShopId(id);
                break;
            case WMSFLOORAREA:
                list = this.plWmsWarehouseBindingRelationService.findByFloorAreaId(id);
                break;
            case WMSFLOOR:
                list = this.plWmsWarehouseBindingRelationService.findByFloorId(id);
                break;
        }

        //从缓存获取styleName colorName
        //获取图片、条码、名称、区、库位
        if (list!=null){
            for (PlWmsWarehouseBindingRelation plWmsWarehouseBindingRelation : list) {
                plWmsWarehouseBindingRelation.setColorName(CacheManager.getColorNameById(plWmsWarehouseBindingRelation.getColorId()));
                plWmsWarehouseBindingRelation.setStyleName(CacheManager.getStyleNameById(plWmsWarehouseBindingRelation.getStyleId()));
                PlWmsFloor plWmsFloor=this.plWarehouseWmsViewService.findFlById(plWmsWarehouseBindingRelation.getFloorId());
                PlWarehouseWmsFloorArea plWarehouseWmsFloorArea=this.plWarehouseWmsViewService.findFaById(plWmsFloor.getParentId());
                plWmsWarehouseBindingRelation.setFloorName(plWmsFloor.getName());
                plWmsWarehouseBindingRelation.setFloorImage(plWmsFloor.getImage());
                plWmsWarehouseBindingRelation.setFloorBarcode(plWmsFloor.getBarcode());
                plWmsWarehouseBindingRelation.setWarehouseFloorAreaName(plWarehouseWmsFloorArea.getName());
            }

            return returnSuccessInfo("ok", list);
        }else{
            return returnSuccessInfo("绑定信息为空");
        }
    }

    @RequestMapping(value = "unbind")
    @ResponseBody
    public MessageBox unbind(String id) {
        this.plWmsWarehouseBindingRelationService.delete(id);
        return returnSuccessInfo("ok");
    }


    @RequestMapping(value = "findViewById")
    @ResponseBody
    public MessageBox findViewById(String id,int type){
        JSONObject obj=new JSONObject();
        List<PlWarehouseWmsView> viewList=null;
        PlWarehouseWmsView view=null;
        switch (type){
            case WMSSHOP:
                obj.put("shopId",id);
                break;
            case WMSFLOORAREA:
                viewList = this.plWarehouseWmsViewService.findViewByFaId(id);
                view=viewList.get(0);
                if (CommonUtil.isNotBlank(view)){
                    obj.put("shopId",view.getFloorAreaParentCode());
                    obj.put("floorArea",view.getFloorAreaName());
                    obj.put("floorAreaId",view.getFloorAreaId());
                }
                break;
            case WMSFLOOR:
                viewList=this.plWarehouseWmsViewService.findViewByFlId(id);
                view=viewList.get(0);
                if (CommonUtil.isNotBlank(view)){
                    obj.put("shopId",view.getFloorAreaParentCode());
                    obj.put("floorArea",view.getFloorAreaName());
                    obj.put("floorAreaId",view.getFloorAreaId());
                    obj.put("floor",view.getFloorName());
                    obj.put("floorId",view.getFloorId());
                }
                break;
        }

        return returnSuccessInfo("ok",obj);

    }

    @RequestMapping(value = "checkStyle")
    @ResponseBody
    public Map<String, Boolean> checkStyle(String styleId,String floorId){
        List<String> floorIds=this.plWmsWarehouseBindingRelationService.findFloorByStyleId(styleId);
        Map<String, Boolean> json = new HashMap<>();
        if (floorIds.size() < 1){
            json.put("valid", true);
        }else{
            if (floorIds.contains(floorId)){
                json.put("valid", true);
            }else{
                json.put("valid", false);
            }
        }
        return json;
    }

    @RequestMapping(value = "bindWarehouse")
    @ResponseBody
    public MessageBox bindRack(PlWmsWarehouseBindingRelation plWmsWarehouseBindingRelation){
        if (CommonUtil.isNotBlank(plWmsWarehouseBindingRelation)){
            String styleId=plWmsWarehouseBindingRelation.getStyleId();
            if (CommonUtil.isBlank(styleId)){
                return returnFailInfo("款式为空");
            }
            String colorId=plWmsWarehouseBindingRelation.getColorId();
            if (CommonUtil.isBlank(colorId)){
                return returnFailInfo("颜色为空");
            }
            String floorId=plWmsWarehouseBindingRelation.getFloorId();
            String floorAreaId=plWmsWarehouseBindingRelation.getWarehouseFloorAreaId();

            //验证style
            List<String> floorIds=this.plWmsWarehouseBindingRelationService.findFloorByStyleId(styleId);
            if (floorIds.size() < 1){
            }else{
                if (floorIds.contains(floorId)){
                }else{
                    return returnFailInfo("款式已在其他货架绑定");
                }
            }


            PlWmsWarehouseBindingRelation wprbr= this.plWmsWarehouseBindingRelationService.findRelationById(plWmsWarehouseBindingRelation.getId());
            if (CommonUtil.isBlank(wprbr)){
                wprbr=new PlWmsWarehouseBindingRelation();
                wprbr.setId(floorId+plWmsWarehouseBindingRelation.getStyleId()+plWmsWarehouseBindingRelation.getColorId());
            }

            wprbr.setColorId(plWmsWarehouseBindingRelation.getColorId());
            wprbr.setStyleId(plWmsWarehouseBindingRelation.getStyleId());
            wprbr.setFloorId(floorId);
            wprbr.setWarehouseFloorAreaId(floorAreaId);
            wprbr.setUpdateDate(new Date());

            //绑定记录
            PlWmsWarehouseBindingRecord plWmsWarehouseBindingRecord=new PlWmsWarehouseBindingRecord();
            plWmsWarehouseBindingRecord.setFloorId(floorId);
            plWmsWarehouseBindingRecord.setStyleId(plWmsWarehouseBindingRelation.getStyleId());
            plWmsWarehouseBindingRecord.setColorId(plWmsWarehouseBindingRelation.getColorId());
            plWmsWarehouseBindingRecord.setUpdateDate(new Date());
            try{
                this.plWmsWarehouseBindingRelationService.save(wprbr);
                this.plWmsWarehouseBindingRelationService.saveWarehouseBindingRecord(plWmsWarehouseBindingRecord);
                return  returnSuccessInfo("绑定成功");
            }catch (Exception e){
                return returnFailInfo("绑定保存失败");
            }
        }else {
            return returnFailInfo("绑定信息为空");
        }
    }

}
