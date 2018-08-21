package com.casesoft.dmc.controller.wms.pl;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.pl.PlWmsShopBindingRelationService;
import com.casesoft.dmc.model.wms.pl.PlShopWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlShopWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsRack;
import com.casesoft.dmc.service.wms.pl.PlShopWmsViewService;
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
@RequestMapping("/wms/pl/rackBind")
public class PlShopWmsBindingController extends BaseController{

    @Autowired
    private PlWmsShopBindingRelationService plWmsShopBindingRelationService;
    @Autowired
    private PlShopWmsViewService plShopWmsViewService;

    private final static int WMSSHOP = 0;
    private final static int WMSFLOORAREA = 1;
    private final static int WMSRACK = 3;
    @RequestMapping("index")
    @Override
    public String index() {
        return "views/wms/pl/plWmsShopBindingRelation";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public List read(@RequestBody RequestPageData<PlShopWmsView> request) {
        this.logAllRequestParams();
        DataResult dataResult = this.plShopWmsViewService.find(request);
        List<PlShopWmsView> list = (List<PlShopWmsView>) dataResult.getData();
        Map<String, WmsFloorTemp> map = new HashMap<String, WmsFloorTemp>();
        if (CommonUtil.isNotBlank(list)) {
            for (PlShopWmsView plShopWmsView : list) {
                map.put(plShopWmsView.getFloorAreaParentCode(), new WmsFloorTemp(
                        plShopWmsView.getFloorAreaParentCode(), plShopWmsView.getFloorAreaParentName(), "", "", "", "", "",WMSSHOP
                ));
                map.put(plShopWmsView.getFloorAreaId(), new WmsFloorTemp(
                        plShopWmsView.getFloorAreaId(), plShopWmsView.getFloorAreaName(), plShopWmsView.getFloorAreaBarcode(), plShopWmsView.getFloorAreaParentCode(), plShopWmsView.getFloorAreaRemark(),
                        plShopWmsView.getFloorAreaDeviceId(), plShopWmsView.getFloorAreaImage(),WMSFLOORAREA
                ));
                if (CommonUtil.isNotBlank(plShopWmsView.getRackId())) {
                    map.put(plShopWmsView.getRackId(), new WmsFloorTemp(
                            plShopWmsView.getRackId(), plShopWmsView.getRackName(), plShopWmsView.getRackBarcode(), plShopWmsView.getFloorAreaId(), plShopWmsView.getRackRemark(),
                            plShopWmsView.getRackDeviceId(), plShopWmsView.getRackImage(), WMSRACK
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
        List<PlWmsShopBindingRelation> list = new ArrayList<PlWmsShopBindingRelation>();
        switch (type) {
            case WMSSHOP:
                list = this.plWmsShopBindingRelationService.findByShopId(id);
                break;
            case WMSFLOORAREA:
                list = this.plWmsShopBindingRelationService.findByFloorAreaId(id);
                break;
            case WMSRACK:
                list = this.plWmsShopBindingRelationService.findByRackId(id);
                break;
        }

        //从缓存获取styleName colorName
        //获取图片、条码、名称、区、货架
        if (list!=null){
            for (PlWmsShopBindingRelation plWmsShopBindingRelation : list) {
                plWmsShopBindingRelation.setColorName(CacheManager.getColorNameById(plWmsShopBindingRelation.getColorId()));
                plWmsShopBindingRelation.setStyleName(CacheManager.getStyleNameById(plWmsShopBindingRelation.getStyleId()));
                PlWmsRack plWmsRack=this.plShopWmsViewService.findRaById(plWmsShopBindingRelation.getRackId());
                plWmsShopBindingRelation.setRackImage(plWmsRack.getImage());
                plWmsShopBindingRelation.setRackBarcode(plWmsRack.getBarcode());
                plWmsShopBindingRelation.setRackName(plWmsRack.getName());
                PlShopWmsFloorArea plShopWmsFloorArea=this.plShopWmsViewService.findFaById(plWmsRack.getParentId());
                plWmsShopBindingRelation.setFloorAreaName(plShopWmsFloorArea.getName());
            }

            return returnSuccessInfo("ok", list);
        }else{
            return returnSuccessInfo("绑定信息为空");
        }
    }

    @RequestMapping(value = "checkStyle")
    @ResponseBody
    public Map<String, Boolean> checkStyle(String styleId,String rackId){
        List<String> raId=this.plWmsShopBindingRelationService.findRackByStyleId(styleId);
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

    @RequestMapping(value = "bindShop")
    @ResponseBody
    public MessageBox bindRack(PlWmsShopBindingRelation plWmsShopBindingRelation){
        if (CommonUtil.isNotBlank(plWmsShopBindingRelation)){
            String styleId=plWmsShopBindingRelation.getStyleId();
            if (CommonUtil.isBlank(styleId)){
                return returnFailInfo("款式为空");
            }
            String colorId=plWmsShopBindingRelation.getColorId();
            if (CommonUtil.isBlank(colorId)){
                return returnFailInfo("颜色为空");
            }
            String rackId=plWmsShopBindingRelation.getRackId();
            String floorAreaId=plWmsShopBindingRelation.getFloorAreaId();

            //验证style
            List<String> raId=this.plWmsShopBindingRelationService.findRackByStyleId(styleId);
            if (raId.size() < 1){
            }else{
                if (raId.contains(rackId)){
                }else{
                    return returnFailInfo("款式已在其他货架绑定");
                }
            }


            PlWmsShopBindingRelation wprbr= this.plWmsShopBindingRelationService.findRelationById(plWmsShopBindingRelation.getId());
            if (CommonUtil.isBlank(wprbr)){
                wprbr=new PlWmsShopBindingRelation();
                wprbr.setId(rackId+plWmsShopBindingRelation.getStyleId()+plWmsShopBindingRelation.getColorId());
            }

            wprbr.setColorId(plWmsShopBindingRelation.getColorId());
            wprbr.setStyleId(plWmsShopBindingRelation.getStyleId());
            wprbr.setRackId(rackId);
            wprbr.setFloorAreaId(floorAreaId);
            wprbr.setUpdateDate(new Date());

            //绑定记录
            PlWmsShopBindingRecord plWmsShopBindingRecord=new PlWmsShopBindingRecord();
            plWmsShopBindingRecord.setRackId(rackId);
            plWmsShopBindingRecord.setStyleId(plWmsShopBindingRelation.getStyleId());
            plWmsShopBindingRecord.setColorId(plWmsShopBindingRelation.getColorId());
            plWmsShopBindingRecord.setUpdateDate(new Date());
            try{
                this.plWmsShopBindingRelationService.save(wprbr);
                this.plWmsShopBindingRelationService.saveShopBindingRecord(plWmsShopBindingRecord);
               /* CacheManager.refreshWmsPlRackBindingRelationCache();*/
                return  returnSuccessInfo("绑定成功");
            }catch (Exception e){
                return returnFailInfo("绑定保存失败");
            }
        }else {
            return returnFailInfo("绑定信息为空");
        }
    }

    @RequestMapping(value = "unbind")
    @ResponseBody
    public MessageBox unbind(String id) {
        this.plWmsShopBindingRelationService.delete(id);
       /* CacheManager.refreshWmsPlRackBindingRelationCache();*/
        return returnSuccessInfo("ok");
    }


}
