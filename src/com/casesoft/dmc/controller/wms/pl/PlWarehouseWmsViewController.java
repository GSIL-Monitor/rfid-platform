package com.casesoft.dmc.controller.wms.pl;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsFloor;
import com.casesoft.dmc.service.wms.pl.PlWarehouseWmsViewService;
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
@RequestMapping("/wms/pl/warehouse")
public class PlWarehouseWmsViewController extends BaseController {

    private static final int WMSSHOP = 0;
    private static final int WMSFLOORAREA = 1;
    private static final int WMSFLOOR = 2;
    @Autowired
    private PlWarehouseWmsViewService plWarehouseWmsViewService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/wms/pl/plWarehouseWms";
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

    @ResponseBody
    @RequestMapping("/faList")
    public List<PlWarehouseWmsFloorArea> faList() throws Exception {
        return this.plWarehouseWmsViewService.findfa();
    }

    @ResponseBody
    @RequestMapping("/flList")
    public List<PlWmsFloor> flList() throws Exception {
        return this.plWarehouseWmsViewService.findfl();
    }


    @ResponseBody
    @RequestMapping("/saveFa")
    public MessageBox saveFa(PlWarehouseWmsFloorArea plWarehouseWmsFloorArea) throws Exception {
        if (!checkWarehouseFloorAreaBarcode(plWarehouseWmsFloorArea.getBarcode(),plWarehouseWmsFloorArea.getId(),plWarehouseWmsFloorArea.getParentCode()).get("valid")){
            return returnFailInfo("条码已存在");
        }
        PlWarehouseWmsFloorArea w = this.plWarehouseWmsViewService.findFaById(plWarehouseWmsFloorArea.getId());
        if (CommonUtil.isBlank(w)) {
            w = new PlWarehouseWmsFloorArea();
            String id = this.plWarehouseWmsViewService.findMaxId("fa");
            w.setId(id);
            w.setCreateDate(new Date());
        }
        w.setSkuQty(plWarehouseWmsFloorArea.getSkuQty());
        w.setBarcode(plWarehouseWmsFloorArea.getBarcode());
        w.setImage(plWarehouseWmsFloorArea.getImage());
        w.setUpdateCode(getCurrentUser().getOwnerId());
        w.setRemark(plWarehouseWmsFloorArea.getRemark());
        w.setParentCode(plWarehouseWmsFloorArea.getParentCode());
        w.setName(plWarehouseWmsFloorArea.getName());
        w.setEnabled(true);
        this.plWarehouseWmsViewService.saveFa(w);
        return returnSuccessInfo("ok",w.getId());
    }

    @ResponseBody
    @RequestMapping("/saveFl")
    public MessageBox saveFl(PlWmsFloor plWmsFloor) throws Exception {
        if (!checkFloorBarcode(plWmsFloor.getBarcode(),plWmsFloor.getId(),plWmsFloor.getParentId()).get("valid")){
            return returnFailInfo("条码已存在");
        }
        PlWmsFloor w = this.plWarehouseWmsViewService.findFlById(plWmsFloor.getId());
        if (CommonUtil.isBlank(w)) {
            w = new PlWmsFloor();
            String id = this.plWarehouseWmsViewService.findMaxId("fl");
            w.setId(id);
            w.setCreateDate(new Date());
        }
        w.setSkuQty(plWmsFloor.getSkuQty());
        w.setParentId(plWmsFloor.getParentId());
        w.setBarcode(plWmsFloor.getBarcode());
        w.setImage(plWmsFloor.getImage());
        w.setUpdateCode(getCurrentUser().getOwnerId());
        w.setRemark(plWmsFloor.getRemark());
        w.setName(plWmsFloor.getName());
        w.setEnabled(true);
        this.plWarehouseWmsViewService.saveFl(w);
        return returnSuccessInfo("ok",w.getId());

    }


    @RequestMapping("/addFa")
    public ModelAndView addFa(String parentId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_WarehouseFloorArea_edit");
        model.addObject("parentCode", parentId);
        model.addObject("shopId", parentId);
        return model;
    }

    @RequestMapping("/addFl")
    public ModelAndView addFl(String parentId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_floor_edit");
        model.addObject("parentId", parentId);
        String shopId = this.plWarehouseWmsViewService.findShopIdByFaId(parentId);
        model.addObject("shopId", shopId);
        return model;
    }


    @RequestMapping("/editFa")
    public ModelAndView editFa(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_WarehouseFloorArea_edit");
        PlWarehouseWmsFloorArea fa = this.plWarehouseWmsViewService.findFaById(id);
        model.addObject("floorarea", fa);
        String shopId = this.plWarehouseWmsViewService.findShopIdByFaId(id);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/editFl")
    public ModelAndView editFl(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/pl/pl_floor_edit");
        PlWmsFloor fl = this.plWarehouseWmsViewService.findFlById(id);
        fl.setParentName(this.plWarehouseWmsViewService.findFaById(fl.getParentId()).getName());
        model.addObject("floor", fl);
        String shopId = this.plWarehouseWmsViewService.findShopIdByFlId(id);
        model.addObject("shopId", shopId);
        return model;
    }


    @RequestMapping(value = "/getRackFloorFloorAreaList")
    @ResponseBody
    public MessageBox getFloorFloorAreaList() {
        List<PlWarehouseWmsFloorArea> wmsFloorAreaList = this.plWarehouseWmsViewService.findfa();
        List<PlWmsFloor> wmsFloorList = this.plWarehouseWmsViewService.findfl();
        JSONObject obj=new JSONObject();
        obj.put("floorarea",wmsFloorAreaList);
        obj.put("floor",wmsFloorList);
        return returnSuccessInfo("ok",obj);
    }

    @RequestMapping(value = "/findFaByShopId")
    @ResponseBody
    public MessageBox findFaByShopId(String shopId) {
        List<PlWarehouseWmsFloorArea> wmsFloorAreaList=this.plWarehouseWmsViewService.findFaByShopId(shopId);
        return returnSuccessInfo("ok",wmsFloorAreaList);
    }

    @RequestMapping(value = "/findFlByFaId")
    @ResponseBody
    public MessageBox findFlByFaId(String floorAreaId) {
        List<PlWmsFloor> wmsFloorList=this.plWarehouseWmsViewService.findFlByFaId(floorAreaId);
        return returnSuccessInfo("ok",wmsFloorList);
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

    /**
     * 检验库位barcode在同一店铺下是否唯一
     * @param barcode
     * @param id
     * @param parentId
     * @return {"valid",true or false}
     * @throws Exception
     */
    @RequestMapping(value = "/checkFloorBarcode")
    @ResponseBody
    public Map<String, Boolean> checkFloorBarcode(String barcode, String id,String parentId) throws Exception {
        barcode=barcode.toUpperCase();
        String shopId= this.plWarehouseWmsViewService.findShopIdByFaId(parentId);
        Map<String, Boolean> json = new HashMap<>();
        PlWmsFloor plWmsFloor=this.plWarehouseWmsViewService.findFlById(id);
        if (CommonUtil.isBlank(id)){
            //第一次增加
            if (this.plWarehouseWmsViewService.findFlBarcode(barcode, shopId)){
                json.put("valid", false);
            }else{
                json.put("valid", true);
            }
        }else{
            if (plWmsFloor!=null){
                //编辑
                if (this.plWarehouseWmsViewService.findFlBarcode(barcode, shopId)){
                    //存在
                    if (barcode.equals(plWmsFloor.getBarcode())){
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
                if (this.plWarehouseWmsViewService.findFlBarcode(barcode, shopId)){
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
     * 检验仓库区barcode在同一店铺下是否唯一
     * @param barcode
     * @param id
     * @param shopId
     * @return {"valid",true or false}
     * @throws Exception
     */
    @RequestMapping(value = "/checkWarehouseFloorAreaBarcode")
    @ResponseBody
    public Map<String, Boolean> checkWarehouseFloorAreaBarcode(String barcode, String id,String shopId) throws Exception {
        barcode=barcode.toUpperCase();
        Map<String, Boolean> json = new HashMap<>();
        if (CommonUtil.isBlank(id)){
            //第一次增加
            if (this.plWarehouseWmsViewService.findFaBarcode(barcode,shopId)){
                json.put("valid", false);
            }else{
                json.put("valid", true);
            }
        }else{
            PlWarehouseWmsFloorArea plWarehouseWmsFloorArea=this.plWarehouseWmsViewService.findFaById(id);
            if (plWarehouseWmsFloorArea!=null){
                //编辑
                if (this.plWarehouseWmsViewService.findFaBarcode(barcode,shopId)){
                    //存在
                    if (barcode.equals(plWarehouseWmsFloorArea.getBarcode())){
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
                if (this.plWarehouseWmsViewService.findFaBarcode(barcode,shopId)){
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
     *  删除同一仓库下的区、区、库位
     * @param id
     * @param type
     * @return MessageBox
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public MessageBox del(String id, Integer type) {

        switch (type) {
            case WMSSHOP:
                this.plWarehouseWmsViewService.deleteWarehouse(id);
                break;
            case WMSFLOORAREA:
                this.plWarehouseWmsViewService.deleteFloorArea(id);
                break;
            case WMSFLOOR:
                this.plWarehouseWmsViewService.deleteFloor(id);
                break;

        }
        return returnSuccessInfo("ok");
    }

}
