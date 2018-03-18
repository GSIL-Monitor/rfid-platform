package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.factory.FactoryWorkTime;
import com.casesoft.dmc.model.factory.WorkCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvin-PC on 2017/5/8 0008.
 */
public class FactoryUtil {
    public static Double getDutyDaysHours(String strStartDate, String strEndDate)  throws Exception {
        Date startDate = CommonUtil.converStrToDate(strStartDate, "yyyy-MM-dd HH:mm:ss");
        Date endDate = CommonUtil.converStrToDate(strEndDate, "yyyy-MM-dd HH:mm:ss");

        Date startDay = CommonUtil.converStrToDate(strStartDate, "yyyy-MM-dd");
        Date endDay =  CommonUtil.converStrToDate(strEndDate, "yyyy-MM-dd");

        Double totTime = Double.valueOf(0.0D);
        totTime += getStartAndEndWorkTime(startDate,endDate);

        while (startDay.compareTo(endDay) < 0) {

            WorkCalendar calendar = CacheManager.getWorkCalendarByDay(CommonUtil.getDateString(startDate, "yyyy-MM-dd"));
            if(CommonUtil.isNotBlank(calendar)){
                switch (calendar.getStatus()) {
                    case FactoryConstant.WorkCalendarStatus.WorkAllDay:
                        totTime += 8 * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.MorningWork:
                        totTime += 3.5 * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.Holiday:
                        totTime += 0;
                        break;
                }
            }else{
                if (startDay.getDay() == 6) {
                    totTime += 3.5*3600*1000;

                }
                else if ((startDay.getDay() == 0)) {
                    totTime += 0;
                } else {
                    totTime += 8*3600*1000;
                }
            }
            startDay.setDate(startDay.getDate() + 1);

        }

        return totTime / 3600000;
    }



    public static Double getDutyDaysHoursByFactory(String strStartDate, String strEndDate, FactoryWorkTime workTime)  throws Exception{
        Date startDate = CommonUtil.converStrToDate(strStartDate, "yyyy-MM-dd HH:mm:ss");
        Date endDate = CommonUtil.converStrToDate(strEndDate, "yyyy-MM-dd HH:mm:ss");

        Date startDay = CommonUtil.converStrToDate(strStartDate, "yyyy-MM-dd");
        Date endDay =  CommonUtil.converStrToDate(strEndDate, "yyyy-MM-dd");

        Double totTime = Double.valueOf(0.0D);
        totTime += getStartAndEndWorkTime(startDate,endDate,workTime);

        while (startDay.compareTo(endDay) < 0) {

            WorkCalendar calendar = CacheManager.getWorkCalendarByDay(CommonUtil.getDateString(startDate, "yyyy-MM-dd"));
            if(CommonUtil.isNotBlank(calendar)){
                switch (calendar.getStatus()) {
                    case FactoryConstant.WorkCalendarStatus.WorkAllDay:
                        totTime += workTime.getDayTotalTime() * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.MorningWork:
                        totTime += workTime.getMorningTotalTime() * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.Holiday:
                        totTime += 0;
                        break;
                }
            }else{
                if (startDay.getDay() == 6) {
                    totTime += workTime.getMorningTotalTime() * 3600 * 1000;
                }
                else if ((startDay.getDay() == 0)) {
                    totTime += 0;
                } else {
                    totTime += workTime.getDayTotalTime() * 3600 * 1000;
                }
            }
            startDay.setDate(startDay.getDate() + 1);

        }

        return totTime / 3600000;
    }

    private static Double getStartAndEndWorkTime(Date startDate, Date endDate, FactoryWorkTime workTime) throws Exception {
        Double totTime = Double.valueOf(0.0D);

        if (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
                startDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += (workTime.getMorningTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += (workTime.getDayTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }
        }else if ((startDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())) {
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (workTime.getMorningTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (workTime.getDayTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }

        }else if ((startDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())) {
            startDate = CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
        }else if ((startDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+workTime.getMorningTotalTime()*3600*1000);
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+workTime.getMorningTotalTime()*3600*1000);
                totTime += (workTime.getMorningTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+workTime.getMorningTotalTime()*3600*1000);
                totTime += (workTime.getDayTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }

        }else{
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){

                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){

                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += (workTime.getMorningTotalTime()*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                if(CommonUtil.getDateString(startDate, "yyyy-MM-dd").equals(CommonUtil.getDateString(endDate, "yyyy-MM-dd"))){
                    totTime += (endDate.getTime() - startDate.getTime());
                }else{
                    totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
                }

            }
        }

        return totTime;
    }

    public static int getTotWorkDay(String startDay, String endDay) throws Exception {
        int totDay=1;
        Date startDate = CommonUtil.converStrToDate(startDay,"yyyy-MM-dd");
        Date endDate = CommonUtil.converStrToDate(endDay,"yyyy-MM-dd");
        startDate.setDate(startDate.getDate() + 1);
        while (startDate.compareTo(endDate) < 0) {

            WorkCalendar day = CacheManager.getWorkCalendarByDay(CommonUtil.getDateString(startDate, "yyyy-MM-dd"));
            if(CommonUtil.isNotBlank(day)){
                if (CommonUtil.isBlank(day.getStatus())){
                    day.setStatus(0);
                }
                if(day.getStatus() != FactoryConstant.WorkCalendarStatus.Holiday){
                    totDay += 1;
                }
            }else{
                if ((startDate.getDay() != 0)) {
                    totDay += 1;
                }
            }
            startDate.setDate(startDate.getDate() + 1);

        }
        return totDay;
    }

    public static Double getDutyDaysHours(String strStartDate, String strEndDate, String dataFormat, List<WorkCalendar> calendarList)  throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(dataFormat);

        Date startDate = df.parse(strStartDate);
        Date endDate = df.parse(strEndDate);

        Double totTime = Double.valueOf(0.0D);

        for(WorkCalendar calendar : calendarList) {

            if(!calendar.getDay().equals(strEndDate.substring(0,10))) {
                if(calendar.getDay().equals(strStartDate.substring(0,10))) {
                    totTime += getStartAndEndWorkTime(startDate,endDate);
                }
                switch (calendar.getStatus()) {
                    case FactoryConstant.WorkCalendarStatus.WorkAllDay:
                        totTime += 8 * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.MorningWork:
                        totTime += 3.5 * 3600 * 1000;
                        break;
                    case FactoryConstant.WorkCalendarStatus.Holiday:
                        totTime += 0;
                        break;
                }

            }
        }
        return totTime / 3600000;
    }



    private static Double getStartAndEndWorkTime(Date startDate, Date endDate) throws Exception {
        Double totTime = Double.valueOf(0.0D);

        if (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
                startDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += (3.5*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += (8*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }
        }else if ((startDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())) {
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (3.5*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
                totTime += (8*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }

        }else if ((startDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())) {
            startDate = CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
        }else if ((startDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                (startDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+3.5*3600*1000);
                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){
                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+3.5*3600*1000);
                totTime += (3.5*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                totTime += -(startDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(startDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()+3.5*3600*1000);
                totTime += (8*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
            }

        }else{
            if (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()){
                totTime +=0;
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime())){

                totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningStartTime)).getTime());
            }else if ((endDate.getTime() > CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.MorningEndTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime())){

                endDate = CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime));
            }else if ((endDate.getTime() >= CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime()) &&
                    (endDate.getTime() < CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime())) {
                totTime += (3.5*3600*1000+endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonStartTime)).getTime());
            }else{
                if(CommonUtil.getDateString(startDate, "yyyy-MM-dd").equals(CommonUtil.getDateString(endDate, "yyyy-MM-dd"))){
                    totTime += (endDate.getTime() - startDate.getTime());
                }else{
                    totTime += (endDate.getTime() - CommonUtil.converStrToDate(CommonUtil.getDateString(endDate, "yyyy-MM-dd " + FactoryConstant.WorkTime.AfternoonEndTime)).getTime());
                }

            }
        }

        return totTime;
    }
}
