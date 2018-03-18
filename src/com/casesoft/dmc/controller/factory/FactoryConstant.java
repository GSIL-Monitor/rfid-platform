package com.casesoft.dmc.controller.factory;

import java.util.regex.Pattern;

public class FactoryConstant {
	
	public static class illegCharacter{
		public final static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|,]");
	}
	
	public static class Prefix{
		public final static String Tag_Init_prefix = "TIB";
	}	

    public class TaskType {
        public final static String Inbound = "I";
        public final static String Outbound = "O";
        public final static String Pause = "P";
        public final static String Restart = "R";
        public final static String Cancle = "C";
        public final static String Offline = "L";
        public final static String Back= "B";
    }
    public class Category{
    	public final static String SHIRT = "SHIRT";
    	public final static String SUIT="SUIT";
    	public final static String PANTS ="PANTS";
    	public final static String Outerwear ="Outerwear";
    }

    public class Mail{
    	public final static String SendMail="rfid@leverstyle.com";
    	public final static String NoShirtMail="susanne.yang@leverstyle.com";
        public final static String NoShirtMail2="alice.xiao@leverstyle.com";
        public final static String NoShirtMail3="fei.yang@lssztech.com";
        public final static String NoShirtMail4="li.chen@lssztech.com";
    	public final static String ShirtMail="mandy.xu@leverstyle.com";
       /* public final static String SendMail="support@casesoft.com.cn";
        public final static String ShirtMail="1094266828@qq.com";
        public final static String NoShirtMail="alvin.ma@casesoft.com.cn";*/
    }

    public class WorkCalendarStatus{

        public final static int Holiday = 0;//休息日
        public final static int WorkAllDay = 1;//全天班
        public final static int MorningWork = 2;//上午班

    }
    public class WorkTime{
        public final static String MorningStartTime = "08:45:00";
        public final static String MorningEndTime = "12:15:00";
        public final static String AfternoonStartTime = "13:15:00";
        public final static String AfternoonEndTime = "17:45:00";
    }
}
