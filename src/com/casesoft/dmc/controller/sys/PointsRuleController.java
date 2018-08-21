package com.casesoft.dmc.controller.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.PointsRule;
import com.casesoft.dmc.service.sys.PointsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/11/3.
 */
@Controller
@RequestMapping("/sys/pointsRule")
public class PointsRuleController extends BaseController implements IBaseInfoController<PointsRule> {

    @Autowired
    private PointsRuleService pointsRuleService;

    //    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/sys/pointsRuleManagement";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/sys/pointsRuleManagement");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<PointsRule> findPage(Page<PointsRule> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.pointsRuleService.findPage(page, filters);
        return page;
    }

    @Override
    public List<PointsRule> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(PointsRule entity) throws Exception {
        return null;
    }

    @RequestMapping("/saveRule")
    @ResponseBody
    public MessageBox save(String pointsRuleStr, String userId) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            PointsRule pointsRule = JSON.parseObject(pointsRuleStr, PointsRule.class);
            pointsRule.setOprId(userId);
            if(CommonUtil.isBlank(pointsRule.getId())){
                if (pointsRule.isDefaultRule()) {
                    pointsRule.setUnitId("All");
                    pointsRule.setStartDate(new Date());
                    pointsRule.setEndDate(new Date());
                    pointsRule.setId("default-All-" + sdf.format(pointsRule.getStartDate()) + "-" + pointsRule.getUnitPoints());
                } else {
                    pointsRule.setId(pointsRule.getUnitId() + "-" + sdf.format(pointsRule.getStartDate())
                            + "-" + sdf.format(pointsRule.getEndDate()) + "-" + pointsRule.getUnitPoints());
                }
                this.pointsRuleService.save(pointsRule);
            }else {
                PointsRule ruleById = this.pointsRuleService.findRuleById(pointsRule.getId());
                ruleById.setStatus(pointsRule.getStatus());
                ruleById.setDefaultRule(pointsRule.isDefaultRule());
                ruleById.setUnitPoints(pointsRule.getUnitPoints());
                ruleById.setRemark(pointsRule.getRemark());
                this.pointsRuleService.save(ruleById);
            }

            return new MessageBox(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "保存失败");
        }
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

    @RequestMapping("/rulesCheck")
    @ResponseBody
    public MessageBox rulesCheck(String pointsRuleStr) {
        PointsRule pointsRule = JSON.parseObject(pointsRuleStr, PointsRule.class);
        if (pointsRule.isDefaultRule()) {
            PointsRule defaultRule = this.pointsRuleService.findDefaultRule();
            if (CommonUtil.isBlank(defaultRule)) {
                return new MessageBox(true, "无其他开启的默认规则");
            } else {
                return new MessageBox(false, "已有一个开启的默认规则", defaultRule.getId());
            }
        } else {
            List<PointsRule> ruleList = this.pointsRuleService.findRulesByDate(pointsRule.getStartDate(), pointsRule.getEndDate(), pointsRule.getUnitId(), pointsRule.getId());
            if (CommonUtil.isBlank(ruleList)) {
                return new MessageBox(true, "无冲突规则");
            } else {
                int size = ruleList.size();
                StringBuilder conflictRuleIdsStr = new StringBuilder();
                for (int i = 0; i < size ; i++) {
                    conflictRuleIdsStr.append(ruleList.get(i).getId());
                    if(i < size-1){
                        conflictRuleIdsStr.append(",");
                    }
                }
                return new MessageBox(false, "有" + size + "个规则与当前规则冲突", conflictRuleIdsStr.toString());
            }
        }
    }

    @RequestMapping("/turnOffConflictRule")
    @ResponseBody
    public MessageBox turnOffConflictRule(String ruleIdStr){
        try{
            String[] ruleIdArray = ruleIdStr.split(",");
            List<String> ruleIdList = Arrays.asList(ruleIdArray);

            String ruleIdSqlStr = TaskUtil.getSqlStrByList(ruleIdList, PointsRule.class, "id");
            List<PointsRule> ruleList = this.pointsRuleService.findRulesByIds(ruleIdSqlStr);
            for (PointsRule pointsRule : ruleList) {
                pointsRule.setStatus(0);
            }
            this.pointsRuleService.updateRules(ruleList);
            return new MessageBox(true,"成功关闭冲突规则");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true,"关闭冲突规则失败");
        }
    }
}
