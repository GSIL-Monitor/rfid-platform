package com.casesoft.dmc.controller.syn;

import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.syn.BasicConfiguration;
import com.casesoft.dmc.model.syn.Config;
import com.casesoft.dmc.service.syn.BasicConfigurationService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2016-12-19.
 * 定时任务控制
 */
@Controller
@RequestMapping("/timer")
public class QuartzJobController extends BaseController {
    private Logger logger = Logger.getLogger(QuartzJobController.class);

    @Autowired
    private BasicConfigurationService basicConfigurationService;
    private static Scheduler schedulerFactoryBean = (StdScheduler) SpringContextUtil.getApplicationContext()
            .getBean("schedulerFactoryBean");
    @RequestMapping("/runJobs")
    @ResponseBody
    public String runJobs() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQB_enable", "true");
        filters.add(filter);
        filter = new PropertyFilter("EQI_type", "1"
        );
        filters.add(filter);
        List<BasicConfiguration> jobList = this.basicConfigurationService.find(filters);
        try {
            if (CommonUtil.isNotBlank(jobList)
                    && jobList.size() != 0) {
                if (!schedulerFactoryBean.isStarted()) {
                    schedulerFactoryBean.start();
                }
                for (BasicConfiguration job : jobList) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), job.getId());
                    // 获取trigger
                    CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getTrigger(triggerKey);
                    // 不存在，创建一个
                    if (null == trigger) {
                        createSheduler(schedulerFactoryBean, job);
                    } else {// Trigger已存在，那么更新相应的定时设置
                        updateScheduler(schedulerFactoryBean, job, triggerKey, trigger);
                    }
                    job.setConfigState(Config.ConfigState.Running);
                }
                this.basicConfigurationService.save(jobList);
                this.returnSuccess("Timer启动成功！");
            } else {
                this.returnFailur("Timer启动失败!无任务！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.returnFailur("Timer启动失败");
            } catch (Exception ee) {

            }
        }
        return null;
    }
    @RequestMapping("/runOnceJob")
    @ResponseBody
    public String runOnceJob() {
        this.logAllRequestParams();
        try {
            String id = this.getReqParam("job");
            BasicConfiguration basicConfiguration = this.basicConfigurationService.load(id);
            if (CommonUtil.isNotBlank(basicConfiguration)) {
                JobKey jobKey = JobKey.jobKey(basicConfiguration.getId(), basicConfiguration.getId());
                schedulerFactoryBean.triggerJob(jobKey);
                this.returnSuccess("运行成功！");
            } else {
                this.returnFailur("无任务");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.returnFailur("无任务");
            } catch (Exception ee) {

            }
        }
        return null;
    }
    @RequestMapping("/runSingleJob")
    @ResponseBody
    public String runSingleJob() {
        this.logAllRequestParams();
        try {
            String id = this.getReqParam("job");
            BasicConfiguration basicConfiguration = this.basicConfigurationService.load(id);
            if (CommonUtil.isNotBlank(basicConfiguration)) {
                if(!basicConfiguration.getEnable()){
                    this.returnFailur("开启失败！任务已被禁止！");
                    return null;
                }
                TriggerKey triggerKey = TriggerKey.triggerKey(basicConfiguration.getId(), basicConfiguration.getId());
                // 获取trigger
                CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getTrigger(triggerKey);
                if (CommonUtil.isBlank(trigger)) {
                    createSheduler(schedulerFactoryBean, basicConfiguration);
                } else {
                    updateScheduler(schedulerFactoryBean, basicConfiguration, triggerKey, trigger);
                }
                if (!schedulerFactoryBean.isStarted()) {
                    schedulerFactoryBean.start();
                }
                basicConfiguration.setConfigState(Config.ConfigState.Running);
                this.basicConfigurationService.update(basicConfiguration);
                this.returnSuccess("开启成功！");
            }  {
                this.returnFailur("无任务");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.returnFailur("无任务");
            } catch (Exception ee) {

            }
        }
        return null;
    }
    @RequestMapping("/shutdownJobs")
    @ResponseBody
    public String shutdownJobs() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQB_enable", "true");
        filters.add(filter);
        filter = new PropertyFilter("EQI_type", "1");
        filters.add(filter);
        List<BasicConfiguration> jobList = this.basicConfigurationService.find(filters);
        try {
            if (CommonUtil.isNotBlank(jobList)
                    && jobList.size() != 0) {
                for (BasicConfiguration job : jobList) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), job.getId());
                    // 获取trigger
                    CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getTrigger(triggerKey);
                    // 不存在，创建一个
                    if (null != trigger) {
                        schedulerFactoryBean.pauseTrigger(triggerKey);// 停止触发器
                        schedulerFactoryBean.unscheduleJob(triggerKey);// 移除触发器
                        schedulerFactoryBean.deleteJob(trigger.getJobKey());// 删除任务
                    }
                    job.setConfigState(Config.ConfigState.Ready);
                }
                this.basicConfigurationService.save(jobList);
                this.returnSuccess("Timer关闭成功！");
            } else {
                this.returnFailur("Timer关闭失败!无任务！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.returnFailur("Timer关闭失败");
            } catch (Exception ee) {

            }
        }
        return null;
    }
    @RequestMapping("/shutdownSingleJob")
    @ResponseBody
    public String shutdownSingleJob() {
        this.logAllRequestParams();
        try {
            String id = this.getReqParam("job");
            BasicConfiguration basicConfiguration = this.basicConfigurationService.load(id);
            if (CommonUtil.isNotBlank(basicConfiguration)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(basicConfiguration.getId(), basicConfiguration.getId());
                // 获取trigger
                CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getTrigger(triggerKey);
                if (null != trigger) {
                    schedulerFactoryBean.pauseTrigger(triggerKey);// 停止触发器
                    schedulerFactoryBean.unscheduleJob(triggerKey);// 移除触发器
                    schedulerFactoryBean.deleteJob(trigger.getJobKey());// 删除任务
                }
                basicConfiguration.setConfigState(Config.ConfigState.Ready);
                this.basicConfigurationService.update(basicConfiguration);
                this.returnSuccess("关闭成功！");
            } else {
                this.returnFailur("无任务");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.returnFailur("无任务");
            } catch (Exception ee) {

            }

        }
        return null;
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
    private void updateScheduler(Scheduler scheduler, BasicConfiguration job, TriggerKey triggerKey, CronTrigger trigger)
            throws SchedulerException {
        if (!trigger.getCronExpression().equalsIgnoreCase(job.getDescription())) {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getDescription());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.info(job.getId() + "." + job.getId() + " 更新完毕,目前cron表达式为:" + job.getDescription());
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
    public void createSheduler(Scheduler scheduler, BasicConfiguration job) throws Exception {
        // 在工作状态可用时,即job_status = 1 ,开始创建

        // 新建一个基于Spring的管理Job类
        MethodInvokingJobDetailFactoryBean methodInvJobDetailFB = new MethodInvokingJobDetailFactoryBean();
        // 设置Job名称
        methodInvJobDetailFB.setName(job.getId());
        // 定义的任务类为Spring的定义的Bean则调用 getBean方法
        String api[] = job.getApi().split("!");
        methodInvJobDetailFB
                .setTargetObject(SpringContextUtil.getApplicationContext().getBean(api[0]));
        // 设置任务方法
        methodInvJobDetailFB.setTargetMethod(api[1]);
        // 将管理Job类提交到计划管理类
        methodInvJobDetailFB.afterPropertiesSet();
        /** 并发设置 */
        methodInvJobDetailFB.setConcurrent(true);

        JobDetail jobDetail = methodInvJobDetailFB.getObject();// 动态
        jobDetail.getJobDataMap().put("scheduleJob", job);
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getDescription());
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getId(), job.getId())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);// 注入到管理类
        logger.info(job.getId() + "." + job.getId() + "创建完毕");
    }

    @Override
    public String index() {
        return null;
    }
}
