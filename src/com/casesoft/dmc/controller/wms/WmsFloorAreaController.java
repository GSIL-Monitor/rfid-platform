package com.casesoft.dmc.controller.wms;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsFloorTemp;
import com.casesoft.dmc.extend.third.request.RequestPageData;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;

/**
 * Created by john on 2017-03-08.
 */

@Controller
@RequestMapping("/wms/pl")
public class WmsFloorAreaController extends BaseController {

    @Autowired
    private WmsFloorViewService wmsFloorViewService;

    private static Integer WMSSHOP = 0;
    private static Integer WMSFLOORAREA = 1;
    private static Integer WMSFLOOR = 2;
    private static Integer WMSRACK = 3;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/wms/wmsFloor";
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
    public List<WmsFloorArea> faList() throws Exception {
//        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
//                .getRequest());
        return this.wmsFloorViewService.findfa();
    }

    @ResponseBody
    @RequestMapping("/flList")
    public List<WmsFloor> flList() throws Exception {
//        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
//                .getRequest());
        return this.wmsFloorViewService.findfl();
    }

    @ResponseBody
    @RequestMapping("/raList")
    public List<WmsRack> raList() throws Exception {
//        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
//                .getRequest());
        return this.wmsFloorViewService.findra();
    }

    @ResponseBody
    @RequestMapping("/saveFa")
    public MessageBox saveFa(WmsFloorArea wmsFloorArea) {
        WmsFloorArea w = this.wmsFloorViewService.findFaById(wmsFloorArea.getId());
        if (CommonUtil.isBlank(w)) {
            w = new WmsFloorArea();
            String id = this.wmsFloorViewService.findMaxId("fa");
            w.setId(id);
            w.setCreateDate(new Date());
        }
        w.setSkuQty(wmsFloorArea.getSkuQty());
        w.setSales(wmsFloorArea.getSales());
        w.setBarcode(wmsFloorArea.getBarcode());
        w.setImage(wmsFloorArea.getImage());
        w.setUpdateCode(getCurrentUser().getOwnerId());
        w.setRemark(wmsFloorArea.getRemark());
        w.setParentCode(wmsFloorArea.getParentCode());
        w.setName(wmsFloorArea.getName());
        w.setDeviceId(wmsFloorArea.getDeviceId());
        w.setEnabled(true);
        this.wmsFloorViewService.saveFa(w);
        return returnSuccessInfo("ok");
    }

    @ResponseBody
    @RequestMapping("/saveFl")
    public MessageBox saveFl(WmsFloor wmsFloor) {
        WmsFloor w = this.wmsFloorViewService.findFlById(wmsFloor.getId());
        if (CommonUtil.isBlank(w)) {
            w = new WmsFloor();
            String id = this.wmsFloorViewService.findMaxId("fl");
            w.setId(id);
            w.setCreateDate(new Date());
        }
        w.setSkuQty(wmsFloor.getSkuQty());
        w.setSales(wmsFloor.getSales());
        w.setParentId(wmsFloor.getParentId());
        w.setBarcode(wmsFloor.getBarcode());
        w.setImage(wmsFloor.getImage());
        w.setUpdateCode(getCurrentUser().getOwnerId());
        w.setRemark(wmsFloor.getRemark());
        w.setName(wmsFloor.getName());
        w.setDeviceId(wmsFloor.getDeviceId());
        w.setEnabled(true);
        this.wmsFloorViewService.saveFl(w);
        return returnSuccessInfo("ok");

    }

    @ResponseBody
    @RequestMapping("/saveRa")
    public MessageBox saveRa(WmsRack wmsRack) {
        WmsRack w = this.wmsFloorViewService.findRaById(wmsRack.getId());
        if (CommonUtil.isBlank(w)) {

            String id = this.wmsFloorViewService.findMaxId("ra");
            wmsRack.setId(id);
            wmsRack.setCreateDate(new Date());
            wmsRack.setEnabled(true);
            this.wmsFloorViewService.saveRa(wmsRack);
        } else {
            w.setName(wmsRack.getName());
            w.setParentId(wmsRack.getParentId());
            w.setSkuQty(wmsRack.getSkuQty());
            w.setSales(wmsRack.getSales());
            w.setBarcode(wmsRack.getBarcode());
            w.setImage(wmsRack.getImage());
            w.setUpdateCode(getCurrentUser().getOwnerId());
            w.setRemark(wmsRack.getRemark());
            w.setDeviceId(wmsRack.getDeviceId());
            w.setEnabled(true);
            this.wmsFloorViewService.saveRa(w);
        }

        return returnSuccessInfo("ok");
    }

    @RequestMapping("/addFa")
    public ModelAndView addFa(String parentId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/floorArea_edit");
        model.addObject("parentId", parentId);
        model.addObject("shopId", parentId);
        return model;
    }

    @RequestMapping("/addFl")
    public ModelAndView addFl(String parentId, String sales) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/floor_edit");
        model.addObject("parentId", parentId);
        model.addObject("sales", sales);
        String shopId = this.wmsFloorViewService.findShopIdByFaId(parentId);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/addRa")
    public ModelAndView addRa(String parentId, String sales) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/rack_edit");
        model.addObject("parentId", parentId);
        model.addObject("sales", sales);
        String shopId = this.wmsFloorViewService.findShopIdByFlId(parentId);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/editFa")
    public ModelAndView editFa(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/floorArea_edit");
        WmsFloorArea fa = this.wmsFloorViewService.findFaById(id);
        model.addObject("floorarea", fa);
        String shopId = this.wmsFloorViewService.findShopIdByFaId(id);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/editFl")
    public ModelAndView editFl(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/floor_edit");
        WmsFloor fl = this.wmsFloorViewService.findFlById(id);
        model.addObject("floor", fl);
        String shopId = this.wmsFloorViewService.findShopIdByFlId(id);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping("/editRa")
    public ModelAndView editRa(String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/views/wms/rack_edit");
        WmsRack ra = this.wmsFloorViewService.findRaById(id);
        model.addObject("rack", ra);
        String shopId = this.wmsFloorViewService.findShopIdByRaId(id);
        model.addObject("shopId", shopId);
        return model;
    }

    @RequestMapping(value = "/getRackFloorFloorAreaList")
    @ResponseBody
    public MessageBox getRackFloorFloorAreaList() {
        List<WmsFloorArea> wmsFloorAreaList = this.wmsFloorViewService.findfa();
        List<WmsFloor> wmsFloorList = this.wmsFloorViewService.findfl();
        List<WmsRack> wmsRackList = this.wmsFloorViewService.findra();
        JSONObject obj=new JSONObject();
        obj.put("floorarea",wmsFloorAreaList);
        obj.put("floor",wmsFloorList);
        obj.put("rack",wmsRackList);
        return returnSuccessInfo("ok",obj);
    }

    @RequestMapping(value = "/findFaByShopId")
    @ResponseBody
    public MessageBox findFaByShopId(String shopId) {
     List<WmsFloorArea> wmsFloorAreaList=this.wmsFloorViewService.findFaByShopId(shopId);
        return returnSuccessInfo("ok",wmsFloorAreaList);
    }

    @RequestMapping(value = "/findFlByFaId")
    @ResponseBody
    public MessageBox findFlByFaId(String floorAreaId) {
        List<WmsFloor> wmsFloorList=this.wmsFloorViewService.findFlByFaId(floorAreaId);
        return returnSuccessInfo("ok",wmsFloorList);
    }

    @RequestMapping(value = "/findRaByFlId")
    @ResponseBody
    public MessageBox findRaByFlId(String floorId) {
        List<WmsRack> wmsRackList=this.wmsFloorViewService.findRaByFlId(floorId);
        return returnSuccessInfo("ok",wmsRackList);
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

    @RequestMapping(value = "/checkBarcode")
    @ResponseBody
    public Map<String, Boolean> checkUniqueCode(String barcode, String flag, Boolean isAdd, String oldBarcode, String shopId) throws Exception {
        Boolean exits = null;
        if (flag.equals("fa")) {
            exits = this.wmsFloorViewService.findFaBarcode(barcode, shopId);
        } else if (flag.equals("fl")) {
            exits = this.wmsFloorViewService.findFlBarcode(barcode, shopId);
        } else {
            exits = this.wmsFloorViewService.findRaBarcode(barcode, shopId);
        }
        Map<String, Boolean> json = new HashMap<>();
        if (exits) {
            if (!isAdd) {
                if (barcode.equals(oldBarcode)) {
                    json.put("valid", true);
                } else {
                    json.put("valid", false);
                }
            } else {
                json.put("valid", false);
            }
        } else {
            json.put("valid", true);
        }
        return json;
    }
}
