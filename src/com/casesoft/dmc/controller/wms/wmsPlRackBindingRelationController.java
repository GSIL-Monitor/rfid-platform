package com.casesoft.dmc.controller.wms;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRecord;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.WmsPlRackBindingRelationService;
import com.casesoft.dmc.model.wms.WmsFloor;
import com.casesoft.dmc.model.wms.WmsFloorArea;
import com.casesoft.dmc.model.wms.WmsFloorView;
import com.casesoft.dmc.model.wms.WmsRack;
import com.casesoft.dmc.service.wms.WmsFloorViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by GuoJunwen on 2017/3/14 0014.
 */
@Controller
@RequestMapping("/wms/pl/binding")
public class wmsPlRackBindingRelationController extends BaseController {

    @Autowired
    private WmsFloorViewService wmsFloorViewService;

    @Autowired
    private WmsPlRackBindingRelationService wmsPlRackBindingRelationService;

    private final static int WMSSHOP = 0;
    private final static int WMSFLOORAREA = 1;
    private final static int WMSFLOOR = 2;
    private final static int WMSRACK = 3;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/wms/wmsPlRackBindingRelation";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public List read(@RequestBody RequestPageData<WmsFloorView> request) {
        this.logAllRequestParams();

        DataResult dataResult = this.wmsFloorViewService.find(request);
        List<WmsFloorView> list = (List<WmsFloorView>) dataResult.getData();
        Map<String, WmsFloorTemp> map = new HashMap<String, WmsFloorTemp>();
        if (CommonUtil.isNotBlank(list)) {
            for (WmsFloorView wmsFloorView : list) {
                map.put(wmsFloorView.getFaParentCode(), new WmsFloorTemp(
                        wmsFloorView.getFaParentCode(), wmsFloorView.getFaParentName(), "", "", "", "", "", "", WMSSHOP
                ));
                map.put(wmsFloorView.getFaId(), new WmsFloorTemp(
                        wmsFloorView.getFaId(), wmsFloorView.getFaName(), wmsFloorView.getFaBarcode(), wmsFloorView.getFaParentCode(), wmsFloorView.getFaRemark(),
                        wmsFloorView.getFaDeviceId(), wmsFloorView.getFaImage(), wmsFloorView.getSales().toString(), WMSFLOORAREA
                ));
                if (CommonUtil.isNotBlank(wmsFloorView.getFlId())) {
                    map.put(wmsFloorView.getFlId(), new WmsFloorTemp(
                            wmsFloorView.getFlId(), wmsFloorView.getFlName(), wmsFloorView.getFlBarcode(), wmsFloorView.getFaId(), wmsFloorView.getFlRemark(),
                            wmsFloorView.getFlDeviceID(), wmsFloorView.getFlImage(), wmsFloorView.getSales().toString(), WMSFLOOR
                    ));
                    if (CommonUtil.isNotBlank(wmsFloorView.getRaId())) {
                        map.put(wmsFloorView.getRaId(), new WmsFloorTemp(
                                wmsFloorView.getRaId(), wmsFloorView.getRaName(), wmsFloorView.getRaBarcode(), wmsFloorView.getFlId(), wmsFloorView.getRaRemark(),
                                wmsFloorView.getRaDeviceId(), wmsFloorView.getRaImage(), wmsFloorView.getSales().toString(), WMSRACK
                        ));
                    }
                }

            }
        }
        return new ArrayList<WmsFloorTemp>(map.values());
    }

    @RequestMapping(value = "detail")
    @ResponseBody
    public MessageBox detail(String id, Integer type) {
        List<WmsPlRackBindingRelation> list = new ArrayList<WmsPlRackBindingRelation>();
        switch (type) {
            case WMSSHOP:
                list = this.wmsPlRackBindingRelationService.findByShopId(id);
                break;
            case WMSFLOORAREA:
                list = this.wmsPlRackBindingRelationService.findByFloorAreaId(id);
                break;
            case WMSFLOOR:
                list = this.wmsPlRackBindingRelationService.findByFloorId(id);
                break;
            case WMSRACK:
                list = this.wmsPlRackBindingRelationService.findByRackId(id);
                break;
        }

        //从缓存获取styleName colorName
        //获取图片、条码、名称、区、库位、货架
        if (list!=null){
            for (WmsPlRackBindingRelation wmsPlRackBindingRelation : list) {
                wmsPlRackBindingRelation.setColorName(CacheManager.getColorNameById(wmsPlRackBindingRelation.getColorId()));
                wmsPlRackBindingRelation.setStyleName(CacheManager.getStyleNameById(wmsPlRackBindingRelation.getStyleId()));
                WmsRack wmsRack=this.wmsFloorViewService.findRaById(wmsPlRackBindingRelation.getRackId());
                wmsPlRackBindingRelation.setRackImage(wmsRack.getImage());
                wmsPlRackBindingRelation.setRackBarcode(wmsRack.getBarcode());
                wmsPlRackBindingRelation.setRackName(wmsRack.getName());
                WmsFloor wmsFloor=this.wmsFloorViewService.findFlById(wmsRack.getParentId());
                WmsFloorArea wmsFloorArea=this.wmsFloorViewService.findFaById(wmsFloor.getParentId());
                wmsPlRackBindingRelation.setFloorName(wmsFloor.getName());
                wmsPlRackBindingRelation.setFloorAreaName(wmsFloorArea.getName());
            }

            return returnSuccessInfo("ok", list);
        }else{
            return returnSuccessInfo("绑定信息为空");
        }
    }

    @RequestMapping(value = "unbind")
    @ResponseBody
    public MessageBox unbind(String id) {
        this.wmsPlRackBindingRelationService.delete(id);
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public MessageBox del(String id, Integer type) {

        switch (type) {
            case WMSSHOP:
                this.wmsPlRackBindingRelationService.unbindShop(id);
                break;
            case WMSFLOORAREA:
                this.wmsPlRackBindingRelationService.unbindFloorArea(id);
                break;
            case WMSFLOOR:
                this.wmsPlRackBindingRelationService.unbindFloor(id);
                break;
            case WMSRACK:
                this.wmsPlRackBindingRelationService.unbindRack(id);
                break;

        }
        return returnSuccessInfo("ok");
    }


    @RequestMapping(value = "findViewById")
    @ResponseBody
    public MessageBox findViewById(String id,int type){
        JSONObject obj=new JSONObject();
        List<WmsFloorView> viewList=null;
        WmsFloorView view=null;
        switch (type){
            case WMSSHOP:
                obj.put("shopId",id);
                break;
            case WMSFLOORAREA:
                viewList = this.wmsFloorViewService.findViewByFaId(id);
                view=viewList.get(0);
                if (CommonUtil.isNotBlank(view)){
                    obj.put("shopId",view.getFaParentCode());
                    obj.put("floorArea",view.getFaName());
                    obj.put("floorAreaId",view.getFaId());
                }
                break;
            case WMSFLOOR:
                viewList=this.wmsFloorViewService.findViewByFlId(id);
                view=viewList.get(0);
                if (CommonUtil.isNotBlank(view)){
                    obj.put("shopId",view.getFaParentCode());
                    obj.put("floorArea",view.getFaName());
                    obj.put("floorAreaId",view.getFaId());
                    obj.put("floor",view.getFlName());
                    obj.put("floorId",view.getFlId());
                }
                break;
            case WMSRACK:
                 view =this.wmsFloorViewService.findViewByRaId(id);
                if (CommonUtil.isNotBlank(view)){
                    obj.put("shopId",view.getFaParentCode());
                    obj.put("floorArea",view.getFaName());
                    obj.put("floorAreaId",view.getFaId());
                    obj.put("floor",view.getFlName());
                    obj.put("floorId",view.getFlId());
                    obj.put("rack",view.getRaName());
                    obj.put("rackId",view.getRaId());
                }
                break;
        }

       return returnSuccessInfo("ok",obj);

    }

    @RequestMapping(value = "checkStyle")
    @ResponseBody
    public Map<String, Boolean> checkStyle(String styleId,String rackId){
        List<String> raId=this.wmsPlRackBindingRelationService.findRackByStyleId(styleId);
        Map<String, Boolean> json = new HashMap<>();
        if (raId.size() < 1){
            json.put("valid", true);
        }else{
            if (raId.contains(rackId)){
                json.put("valid", true);
            }else{
                json.put("valid", false);
            }
        }
        return json;
    }

    @RequestMapping(value = "bindRack")
    @ResponseBody
    public MessageBox bindRack(WmsPlRackBindingRelation wmsPlRackBindingRelation){
        if (CommonUtil.isNotBlank(wmsPlRackBindingRelation)){
            String styleId=wmsPlRackBindingRelation.getStyleId();
            if (CommonUtil.isBlank(styleId)){
                return returnFailInfo("款式为空");
            }
            String colorId=wmsPlRackBindingRelation.getColorId();
            if (CommonUtil.isBlank(colorId)){
                return returnFailInfo("颜色为空");
            }
            String rackId=wmsPlRackBindingRelation.getRackId();
            String floorId=wmsPlRackBindingRelation.getFloorId();
            String floorAreaId=wmsPlRackBindingRelation.getFloorAreaId();


            //验证style
            List<String> raId=this.wmsPlRackBindingRelationService.findRackByStyleId(styleId);
            if (raId.size() < 1){
            }else{
                if (raId.contains(rackId)){
                }else{
                    return returnFailInfo("款式已在其他货架绑定");
                }
            }


            WmsPlRackBindingRelation wprbr= this.wmsPlRackBindingRelationService.findRelationById(wmsPlRackBindingRelation.getId());
            if (CommonUtil.isBlank(wprbr)){
                wprbr=new WmsPlRackBindingRelation();
                wprbr.setId(rackId+wmsPlRackBindingRelation.getStyleId()+wmsPlRackBindingRelation.getColorId());
            }

            wprbr.setColorId(wmsPlRackBindingRelation.getColorId());
            wprbr.setStyleId(wmsPlRackBindingRelation.getStyleId());
            wprbr.setRackId(rackId);
            wprbr.setFloorId(floorId);
            wprbr.setFloorAreaId(floorAreaId);
            wprbr.setUpdateDate(new Date());

            //绑定记录
            WmsPlRackBindingRecord wmsPlRackBindingRecord=new WmsPlRackBindingRecord();
            wmsPlRackBindingRecord.setRackId(rackId);
            wmsPlRackBindingRecord.setStyleId(wmsPlRackBindingRelation.getStyleId());
            wmsPlRackBindingRecord.setColorId(wmsPlRackBindingRelation.getColorId());
            wmsPlRackBindingRecord.setUpdateDate(new Date());
            try{
                this.wmsPlRackBindingRelationService.save(wprbr);
                this.wmsPlRackBindingRelationService.saveRackBindingRecord(wmsPlRackBindingRecord);
                return  returnSuccessInfo("绑定成功");
            }catch (Exception e){
                return returnFailInfo("绑定保存失败");
            }
        }else {
            return returnFailInfo("绑定信息为空");
        }
        }


}
