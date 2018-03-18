package com.casesoft.dmc.extend.third.controller;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.email.SimpleMailService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.PlEmailTemplate;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.PlEmailTemplateService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.sys.Email;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.sys.impl.EmailService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by john on 2017-02-22.
 */
@RequestMapping("/third/playlounge/plEmailTemplate")
@Controller
public class PlEmailTemplateController extends BaseController {
    private static Scheduler schedulerFactoryBean = (StdScheduler) SpringContextUtil.getApplicationContext()
            .getBean("schedulerFactoryBean");
    private static String SYS_EMAIL="support@casesoft.com.cn";
    @Autowired
    private PlEmailTemplateService plEmailTemplateService;
    @Autowired
    private PropertyService propertyService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/third/pl/plEmailTemplate";
    }

    @RequestMapping("/initSelect")
    @ResponseBody
    public MessageBox initSelect(String types) {
        String type[] = types.split(",");
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < type.length; i++) {
            List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType(type[i]);
            if (CommonUtil.isNotBlank(pkList)) {
                jsonObject.put(type[i], pkList);
            }
        }
        return returnSuccessInfo("ok", jsonObject);
    }

    @RequestMapping("/save")
    @ResponseBody
    public MessageBox save(PlEmailTemplate plEmailTemplate) {
        this.logAllRequestParams();
        PlEmailTemplate pl = this.plEmailTemplateService.findById(plEmailTemplate.getId());
        if (CommonUtil.isBlank(pl)) {
            pl = new PlEmailTemplate();
            String id = this.plEmailTemplateService.getMaxId();
            pl.setId(id);
        }

        pl.setTitle(plEmailTemplate.getTitle());
        pl.setShopCode(plEmailTemplate.getShopCode());
        pl.setClass1(plEmailTemplate.getClass1());
        pl.setClass2(plEmailTemplate.getClass2());
        pl.setClass3(plEmailTemplate.getClass3());
        pl.setClass4(plEmailTemplate.getClass4());
        pl.setClass10(plEmailTemplate.getClass10());
        pl.setSendCycle(plEmailTemplate.getSendCycle());
        pl.setFromUser(SYS_EMAIL);
        pl.setToUser(plEmailTemplate.getToUser());
        pl.setWarmLevel(plEmailTemplate.getWarmLevel());
        pl.setUpdateCode(getCurrentUser().getOwnerId());
        pl.setUpdateDate(new Date());
        pl.setRemark(plEmailTemplate.getRemark());
        pl.setStatus(plEmailTemplate.getStatus());
        this.plEmailTemplateService.save(pl);
        return this.returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public DataResult read(@RequestBody RequestPageData<PlEmailTemplate> request) {
        this.logAllRequestParams();
        DataResult PlEmailTemplate = this.plEmailTemplateService.find(request);
        return PlEmailTemplate;
    }

    @RequestMapping("/showEdit")
    @ResponseBody
    public ModelAndView showEdit(String id) {
        ModelAndView model = new ModelAndView();
        PlEmailTemplate plEmailTemplate = this.plEmailTemplateService.findById(id);
        model.addObject("plEmailTemplate", plEmailTemplate);
        model.setViewName("views/third/pl/plEmailTemplate_edit");
        return model;
    }

    /**
     * 启动PL 启动邮件系统
     */
    @ResponseBody
    @RequestMapping("/startEmailTimeTaskWS")
    public MessageBox startEmailTimeTask() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQI_status", "1");
        filters.add(filter);
        if (CommonUtil.isBlank(plEmailTemplateService)) {
            plEmailTemplateService = (PlEmailTemplateService) SpringContextUtil.getBean("plEmailTemplateService");
        }
        List<PlEmailTemplate> plEmailTemplates = this.plEmailTemplateService.find(filters);
        try {
            if (!schedulerFactoryBean.isStarted()) {
                schedulerFactoryBean.start();
            }
            if (CommonUtil.isNotBlank(plEmailTemplates)) {
                for (PlEmailTemplate plEmailTemplate : plEmailTemplates) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(plEmailTemplate.getId(), plEmailTemplate.getId());
                    // 获取trigger
                    CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getTrigger(triggerKey);
                    // 不存在，创建一个
                    if (null == trigger) {
                        createSheduler(schedulerFactoryBean, plEmailTemplate);
                    } else {// Trigger已存在，那么更新相应的定时设置
                        updateScheduler(schedulerFactoryBean, plEmailTemplate, triggerKey, trigger);
                    }
                }

            }
        } catch (Exception e) {
            try {
                this.returnFailur("邮件系统启动失败");
            } catch (Exception ee) {

            }
        }
        return this.returnSuccessInfo("邮件系统开启成功");
    }

    /**
     * 更新相应的定时设置 根据ConfigState做相应的处理
     *
     * @param scheduler
     * @param job
     * @param triggerKey
     * @param trigger
     * @throws SchedulerException
     */
    private void updateScheduler(Scheduler scheduler, PlEmailTemplate job, TriggerKey triggerKey, CronTrigger trigger)
            throws SchedulerException {
        String cron = "";
        if (job.getSendCycle().equalsIgnoreCase("w")) {
            cron = "0 59 4 ? * MON";
        } else {
            cron = "0 15 3 L * ?";
        }
        if (!trigger.getCronExpression().equalsIgnoreCase(cron)) {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.info(job.getId() + "." + job.getId() + " 更新完毕,目前cron表达式为:" + cron);
        }
    }

    /**
     * 创建一个定时任务，并做安排
     *
     * @param scheduler
     * @param job
     * @throws SchedulerException
     * @throws Exception
     */
    public void createSheduler(Scheduler scheduler, PlEmailTemplate job) throws Exception {
        // 在工作状态可用时,即job_status = 1 ,开始创建

        // 新建一个基于Spring的管理Job类
        MethodInvokingJobDetailFactoryBean methodInvJobDetailFB = new MethodInvokingJobDetailFactoryBean();
        // 设置Job名称
        methodInvJobDetailFB.setName(job.getId());
        // 定义的任务类为Spring的定义的Bean则调用 getBean方法

        methodInvJobDetailFB
                .setTargetObject(SpringContextUtil.getApplicationContext().getBean("plEmailTemplateController"));
        // 设置任务方法
        methodInvJobDetailFB.setTargetMethod("sendEmail");
        PlEmailTemplate[] args = new PlEmailTemplate[1];
        args[0] = job;
        methodInvJobDetailFB.setArguments(args);
        // 将管理Job类提交到计划管理类
        methodInvJobDetailFB.afterPropertiesSet();
        /** 并发设置 */
        methodInvJobDetailFB.setConcurrent(true);

        JobDetail jobDetail = methodInvJobDetailFB.getObject();// 动态
        jobDetail.getJobDataMap().put("scheduleJob", job);
        String cron = "";
        if (job.getSendCycle().equalsIgnoreCase("w")) {
            cron = "0 59 4 ? * MON";
        } else {
            cron = "0 15 3 L * ?";
        }
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getId(), job.getId())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);// 注入到管理类
        logger.info(job.getId() + "." + job.getId() + "创建完毕");
    }

    /**
     * 发邮件
     *
     * @param plEmailTemplate
     */
    private void sendEmail(PlEmailTemplate plEmailTemplate) {
        if (CommonUtil.isNotBlank(plEmailTemplate)) {
            EmailService emailService = (EmailService) SpringContextUtil.getBean("emailService");
            Email email = new Email();
            email.setStatus(true);
            email.setTitle(plEmailTemplate.getTitle());
            email.setRecipients(plEmailTemplate.getToUser());
            email.setAddresser(plEmailTemplate.getFromUser());
            email.setContent(PlEmailTemUtils.getEmailTempContent(plEmailTemplate));
            String rootPath = ("d:");
            email.setAdjunctUrl(PlEmailTemUtils.buildEmailFile(rootPath, plEmailTemplate));
            try {
                SimpleMailService simpleMailService = (SimpleMailService) SpringContextUtil.getBean("simpleMailService");
                String content = StringEscapeUtils.unescapeHtml3(email.getContent());
                if (email.getAdjunctUrl() == null) {
                    simpleMailService.sendMsg(email.getAddresser(), email.getRecipients(), email.getTitle(), email.getContent());
                } else {
                    simpleMailService.sendAttachments(email.getAddresser(), email.getRecipients(), email.getTitle(), content, email.getAdjunctUrl());
                }
            } catch (Exception e) {
                e.printStackTrace();
                email.setStatus(false);
            }
            email.setId(UUID.randomUUID().toString());
            email.setSendTime(new Date());
            emailService.save(email);
        }
    }
}
