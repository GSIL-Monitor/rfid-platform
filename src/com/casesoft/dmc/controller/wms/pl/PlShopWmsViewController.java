package com.casesoft.dmc.controller.wms.pl;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlShopWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlShopWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsRack;
import com.casesoft.dmc.service.wms.pl.PlShopWmsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Controller
@RequestMapping("/wms/pl/rack")
public class PlShopWmsViewController extends BaseController {

    private static final int WMSSHOP = 0;
    private static final int WMSFLOORAREA = 1;
    private static final int WMSRACK = 3;

    @Autowired
    private PlShopWmsViewService plShopWmsViewService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/wms/pl/plRackWms";
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
                        plShopWmsView.getFloorAreaParentCode(), plShopWmsView.getFloorAreaParentName(), "", "", "", "", "", WMSSHOP
                ));
                map.put(plShopWmsView.getFloorAreaId(), new WmsFloorTemp(
                        plShopWmsView.getFloorAreaId(), plShopWmsView.getFloorAreaName(), plShopWmsView.getFloorAreaBarcode(), plShopWmsView.getFloorAreaParentCode(), plShopWmsView.getFloorAreaRemark(),
                        plShopWmsView.getFloorAreaDeviceId(), plShopWmsView.getFloorAreaImage(), WMSFLOORAREA
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


    @ResponseBody
    @RequestMapping("/faList")
    public List<PlShopWmsFloorArea> faList() throws Exception {
//        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
//                .getRequest());
        return this.plShopWmsViewService.findfa();
    }


    @ResponseBody
    @RequestMapping("/raList")
    public List<PlWmsRack> raList() throws Exception {
        return this.plShopWmsViewService.findra();
    }

    @ResponseBody
    @RequestMapping("/saveFa")
    public MessageBox saveFa(PlShopWmsFloorArea plShopWmsFloorArea) throws Exception{
        if (!checkShopFloorAreaBarcode(plShopWmsFloorArea.getBarcode(),plShopWmsFloorArea.getId(),plShopWmsFloorArea.getParentCode()).get("valid")){
            return returnFailInfo("条码已存在");
        }
        PlShopWmsFloorArea w = this.plShopWmsViewService.findFaById(plShopWmsFloorArea.getId());
        String id="";
        if (CommonUtil.isBlank(w)) {
            w = new PlShopWmsFloorArea();
            id = this.plShopWmsViewService.findMaxId("fa");
            w.setId(id);
            w.setCreateDate(new Date());
        }
        w.setSkuQty(plShopWmsFloorArea.getSkuQty());
        w.setBarcode(plShopWmsFloorArea.getBarcode());
        w.setImage(plShopWmsFloorArea.getImage());
        w.setUpdateCode(getCurrentUser().getOwnerId());
        w.setRemark(plShopWmsFloorArea.getRemark());
        w.setParentCode(plShopWmsFloorArea.getParentCode());
        w.setName(plShopWmsFloorArea.getName());
        w.setEnabled(true);
        this.plShopWmsViewService.saveFa(w);
        return returnSuccessInfo("ok",CommonUtil.isBlank(w.getId())?id:w.getId());
    }


    @ResponseBody
    @RequestMapping("/saveRa")
    public MessageBox saveRa(PlWmsRack plWmsRack) throws Exception{
        if (!checkRackBarcode(plWmsRack.getBarcode(), plWmsRack.getId(), plWmsRack.getParentId()).get("valid")){
            return returnFailInfo("条码已存在");
        }
        PlWmsRack w = this.plShopWmsViewService.findRaById(plWmsRack.getId());
        String id="";
        if (CommonUtil.isBlank(w)) {
            w = new PlWmsRack();
            id = this.plShopWmsViewService.findMaxId("ra");
            w.setId(id);
            w.setCreateDate(new Date());
        }
            w.setName(plWmsRack.getName());
            w.setParentId(plWmsRack.getParentId());
            w.setSkuQty(plWmsRack.getSkuQty());
            w.setBarcode(plWmsRack.getBarcode());
            w.setImage(plWmsRack.getImage());
            w.setUpdateCode(getCurrentUser().getOwnerId());
            w.setRemark(plWmsRack.getRemark());
            w.setEnabled(true);
            this.plShopWmsViewService.saveRa(w);

        return returnSuccessInfo("ok",CommonUtil.isBlank(w.getId())?id:w.getId());

    }

    @RequestMapping("/addFa")
    public ModelAndView addFa(String parentId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("views/wms/pl/pl_RackFloorArea_edit");
        model.addObject("parentId", parentId);
        model.addObject("shopId", parentId);
        return model;
    }

    @RequestMapping("/addRa")
    public ModelAndView addRa(String parentId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_rack_edit");
        model.addObject("parentId", parentId);
        String parentName=this.plShopWmsViewService.findFaById(parentId).getName();
        model.addObject("parentName", parentName);
        String shopId = this.plShopWmsViewService.findShopIdByFaId(parentId);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/editFa")
    public ModelAndView editFa(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_RackFloorArea_edit");
        PlShopWmsFloorArea fa = this.plShopWmsViewService.findFaById(id);
        model.addObject("floorarea", fa);
        String shopId = this.plShopWmsViewService.findShopIdByFaId(id);
        model.addObject("shopId", shopId);
        return model;
    }


    @RequestMapping("/editRa")
    public ModelAndView editRa(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_rack_edit");
        PlWmsRack ra = this.plShopWmsViewService.findRaById(id);
        ra.setParentName(this.plShopWmsViewService.findFaById(ra.getParentId()).getName());
        model.addObject("rack", ra);
        String shopId = this.plShopWmsViewService.findShopIdByRaId(id);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping(value = "/getRackFloorAreaList")
    @ResponseBody
    public MessageBox getRackFloorFloorAreaList() {
        List<PlShopWmsFloorArea> wmsFloorAreaList = this.plShopWmsViewService.findfa();
        List<PlWmsRack> wmsRackList = this.plShopWmsViewService.findra();
        JSONObject obj = new JSONObject();
        obj.put("floorarea", wmsFloorAreaList);
        obj.put("rack", wmsRackList);
        return returnSuccessInfo("ok", obj);
    }

    @RequestMapping(value = "/findFaByShopId")
    @ResponseBody
    public MessageBox findFaByShopId(String shopId) {
        List<PlShopWmsFloorArea> wmsFloorAreaList = this.plShopWmsViewService.findFaByShopId(shopId);
        return returnSuccessInfo("ok", wmsFloorAreaList);
    }

    @RequestMapping(value = "/findRaByFaId")
    @ResponseBody
    public MessageBox findRaByFlId(String floorAreaId) {
        List<PlWmsRack> wmsRackList = this.plShopWmsViewService.findRaByFaId(floorAreaId);
        return returnSuccessInfo("ok", wmsRackList);
    }

    @RequestMapping(value = "/saveImg")
    @ResponseBody
    public MessageBox saveImg(MultipartFile file, String barcode, String flag) throws Exception {
        this.logAllRequestParams();
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String fileName = file.getOriginalFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = barcode + "_" + System.currentTimeMillis() + "." + fileSuffix;
        String fileParant = "";
        if ("fa".equals(flag)) {
            fileParant = "/images/wms/floorarea/";
        } else if ("fl".equals(flag)) {
            fileParant = "/images/wms/floor/";
        } else {
            fileParant = "/images/wms/rock/";
        }
        File folder = new File(rootPath + fileParant);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File f = new File(folder, fileName);
        file.transferTo(f);
        String result = fileParant + fileName;

        return returnSuccessInfo(result);
    }

    @RequestMapping(value = "/deleteImg")
    @ResponseBody
    public MessageBox deleteImg(String image) throws Exception {
        try {
            String sPath = this.getSession().getServletContext().getRealPath("/") + image;
            File file = new File(sPath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            returnFailInfo("删除文件失败");
        }
        return returnSuccessInfo("删除成功");
    }

    @RequestMapping(value = "/checkRackBarcode")
    @ResponseBody
    public Map<String, Boolean> checkRackBarcode(String barcode, String id,String parentId) throws Exception {
        barcode=barcode.toUpperCase();
        String shopId= this.plShopWmsViewService.findShopIdByFaId(parentId);
        Map<String, Boolean> json = new HashMap<>();
        PlWmsRack plWmsRack=this.plShopWmsViewService.findRaById(id);
        if (CommonUtil.isBlank(id)){
            //第一次增加
            if (this.plShopWmsViewService.findRaBarcode(barcode,shopId)){
                json.put("valid", false);
            }else{
                json.put("valid", true);
            }
        }else{
            if (plWmsRack!=null){
                //编辑
                if (this.plShopWmsViewService.findRaBarcode(barcode,shopId)){
                    //存在
                    if (barcode.equals(plWmsRack.getBarcode())){
                        //和原来一样
                        json.put("valid", true);
                    }else{
                        //和原来不一样
                        json.put("valid", false);
                    }
                }else{
                    //不存在
                    json.put("valid", true);
                }
            }else{
                //增加
                if (this.plShopWmsViewService.findRaBarcode(barcode,shopId)){
                    //存在
                    json.put("valid", false);
                }else{
                    //不存在
                    json.put("valid", true);
                }
            }
        }

        return json;
    }

    @RequestMapping(value = "/checkShopFloorAreaBarcode")
    @ResponseBody
    public Map<String, Boolean> checkShopFloorAreaBarcode(String barcode, String id,String shopId) throws Exception {
        barcode=barcode.toUpperCase();
        Map<String, Boolean> json = new HashMap<>();
        if (CommonUtil.isBlank(id)){
            //第一次增加
            if (this.plShopWmsViewService.findFaBarcode(barcode,shopId)){
                json.put("valid", false);
            }else{
                json.put("valid", true);
            }
        }else{
            PlShopWmsFloorArea plShopWmsFloorArea=this.plShopWmsViewService.findFaById(id);
            if (plShopWmsFloorArea!=null){
                //编辑
                if (this.plShopWmsViewService.findFaBarcode(barcode,shopId)){
                    //存在
                    if (barcode.equals(plShopWmsFloorArea.getBarcode())){
                        //和原来一样
                        json.put("valid", true);
                    }else{
                        //和原来不一样
                        json.put("valid", false);
                    }
                }else{
                    //不存在
                    json.put("valid", true);
                }
            }else{
                //增加
                if (this.plShopWmsViewService.findFaBarcode(barcode,shopId)){
                    //存在
                    json.put("valid", false);
                }else{
                    //不存在
                    json.put("valid", true);
                }
            }

        }

      return json;
    }

    /**
     * 删除同一店下的区、区、货架
     * @param id
     * @param type
     * @return MessageBox
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public MessageBox del(String id, Integer type) {

        switch (type) {
            case WMSSHOP:
                this.plShopWmsViewService.deleteShop(id);
                break;
            case WMSFLOORAREA:
                this.plShopWmsViewService.deleteFloorArea(id);
                break;
            case WMSRACK:
                this.plShopWmsViewService.deleteRack(id);
                break;

        }
        return returnSuccessInfo("ok");
    }
}
