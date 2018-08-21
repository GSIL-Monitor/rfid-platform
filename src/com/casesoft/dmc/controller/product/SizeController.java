package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.SizeSort;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.product.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/prod/size")
public class SizeController extends BaseController implements
        IBaseInfoController<Size> {

    @Autowired
    private SizeService sizeService;


    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/prod/size";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<SizeSort> find(Page<SizeSort> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.sizeService.find(page, filters);
        return page;
    }

    @RequestMapping(value = "/listSizeSort")
    @ResponseBody
    public List<SizeSort> getSizeSort(Page<SizeSort> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.
                getRequest());
        List<SizeSort> sizeSort = this.sizeService.findSort(filters);
        return sizeSort;

    }
    @RequestMapping(value = "/listSort")
    @ResponseBody
    public List<SizeSort> getSizeSort(){
        try {
            List<SizeSort> sizeSortList = this.sizeService.findSizeSort();
            return sizeSortList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<Size> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Size> size = this.sizeService.find(filters);
        return size;
    }

    @RequestMapping(value = "/searchSizeMap")
    @ResponseBody
    public Map<String, List<Size>> searchSizeMap() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Size> sizeList = this.sizeService.find(filters);
        Map<String, List<Size>> sizeMap = new HashMap<>();
        for (Size s : sizeList) {
            SizeSort sizeSort = CacheManager.getSizeSortById(s.getSortId());
            if (sizeMap.containsKey("["+s.getSortId()+"]"+ sizeSort.getSortName())) {
                List<Size> sizes = sizeMap.get("["+s.getSortId()+"]"+ sizeSort.getSortName());
                sizes.add(s);
                sizeMap.put("["+s.getSortId()+"]"+ sizeSort.getSortName(), sizes);

            } else {
                List<Size> sizes = new ArrayList<>();
                sizes.add(s);
                sizeMap.put("["+s.getSortId()+"]"+ sizeSort.getSortName(), sizes);
            }
        }
        return sizeMap;
    }

    @RequestMapping(value = "/searchById")
    @ResponseBody
    public List<Size> searchById(String sortId) throws Exception {
        this.logAllRequestParams();
        List<Size> size = this.sizeService.getSizeBySortId(sortId);
        return size;
    }

    @RequestMapping(value = "/checkBySortNo")
    @ResponseBody
    public Map<String, Boolean> checkBySortNo(String sortNo) {
//        SizeSort ss = this.sizeService.findSortBySortNo(sortNo);
        SizeSort ss = CacheManager.getSizeSortById(sortNo);
        Map<String, Boolean> json = new HashMap<>();
        if (CommonUtil.isNotBlank(ss)) {
            json.put("valid", false);
        } else {
            json.put("valid", true);
        }
        return json;
    }

    @RequestMapping(value = "/checkBySizeId")
    @ResponseBody
    public Map<String, Boolean> checkBySizeId(String sizeId) {
        // Size s = this.sizeService.findSizeBySizeId(sizeId);
        Size s = CacheManager.getSizeById(sizeId);
        Map<String, Boolean> json = new HashMap<>();
        if (CommonUtil.isNotBlank(s)) {
            json.put("valid", false);
        } else {
            json.put("valid", true);
        }
        CacheManager.refreshSizeSortCache();
        return json;
    }

    @RequestMapping(value = "/sizeSortSave")
    @ResponseBody
    public MessageBox sortSave(SizeSort sizeSort) throws Exception {
        this.logAllRequestParams();
        SizeSort ss = CacheManager.getSizeSortById(sizeSort.getId());
        if (CommonUtil.isBlank(ss)) {
            ss = new SizeSort();
            Integer maxSeqNo = this.sizeService.findMaxSeqNoInSizeSortBySortNo();
            ss.setSeqNo(maxSeqNo + 1);
            ss.setId(CommonUtil.convertIntToString(maxSeqNo + 1,2));
            ss.setIsUse("Y");
            ss.setSortNo(ss.getId());
        }
        ss.setSortName(sizeSort.getSortName());
        ss.setRemark(sizeSort.getRemark());
        ss.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.sizeService.saveSort(ss);
        CacheManager.refreshSizeSortCache();
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Size size) throws Exception {
        this.logAllRequestParams();
        String oldId = size.getId();
        size.setSizeId(size.getSizeName());
        Size s = CacheManager.getSizeById(size.getSizeId());
        if (CommonUtil.isBlank(s)) {
            s = new Size();
            s.setId(size.getSizeId());
            Integer maxSeqNo = this.sizeService.findMaxSeqNoInSizeBySortId(size.getSortId());
            s.setSeqNo(maxSeqNo);
            s.setIsUse("Y");
        }
        User u = getCurrentUser();
        s.setOprId(u.getCode());
        s.setSizeName(size.getSizeName());
        s.setSizeId(size.getSizeId());
        s.setSortId(size.getSortId());
        s.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.sizeService.saveAndDelete(s,oldId);
        CacheManager.refreshSizeCache();
        return returnSuccessInfo("ok",s);
    }

    @RequestMapping("/changeSortStatus")
    @ResponseBody
    public MessageBox changeSortStatus(String sortNo, String status) {
        this.logAllRequestParams();
        try {
            SizeSort ss = CacheManager.getSizeSortById(sortNo);
            ss.setIsUse(status);
            this.sizeService.saveSort(ss);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }

    @RequestMapping("/changeSizeStatus")
    @ResponseBody
    public MessageBox changeSizeStatus(String sizeId, String status) {
        this.logAllRequestParams();
        try {
            Size s = CacheManager.getSizeById(sizeId);
            s.setIsUse(status);
            this.sizeService.save(s);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void exportExcel() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Size> findPage(Page<Size> page) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
