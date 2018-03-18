function getMonthFirstDay(formattype){
    var Nowdate=new Date();
    var MonthFirstDay=new Date(Nowdate.getYear(),Nowdate.getMonth(),1);
    return formatDate(MonthFirstDay,formattype);
}
function getToDay(formattype){
    var Nowdate=new Date();
    M=Number(Nowdate.getMonth())+1;
    Nowdate = new Date(Nowdate.getYear()+"/"+M+"/"+Nowdate.getDate());
    return formatDate(Nowdate,formattype);
}
function formatDate(day,formattype){
    var dateString = "";

    var thisyear = formatYear(day.getFullYear());
    var thismonth = appendZero(day.getMonth()+1);
    var thisday = appendZero(day.getDate());
    var thishour = appendZero(day.getHours());
    var thismin  = appendZero(day.getMinutes());
    var thissec  = appendZero(day.getSeconds());

    switch (formattype){
        case 0:
            dateString = thisyear + "年" + thismonth + "月" + thisday + "日";
            break;
        case 1:
            dateString = thisyear + "-" + thismonth + "-" + thisday;
            break;
        case 2:
            dateString = thisyear + "-" + thismonth + "-" + thisday+ " " + appendZero(thishour) + ":" + appendZero(thismin) + ":" + appendZero(thissec);
            break;
        default:
            dateString = thisyear + "-" + thismonth + "-" + thisday;
    }
    return dateString;
}
function formatYear(theYear){
    var tmpYear = parseInt(theYear,10);
    while (tmpYear < 200){
        tmpYear += 1900;
        //if (tmpYear < 1940){
        //    tmpYear += 100;
        //}
    }
    if (tmpYear < this.MinYear){
        tmpYear = this.MinYear;
    }
    if (tmpYear > this.MaxYear){
        tmpYear = this.MaxYear;
    }
    return(tmpYear);
}
function appendZero(n){
    return(("00"+ n).substr(("00"+ n).length-2));
}
//取得两个日期之间的时间差
//参数：interval : y或year-表示取得相差的年份 n或month-表示相差的月份 d或day表示相差的天数 h或hour-表示相差的小时 m或minute-表示相差的分钟 s或second-表示相差的秒数 ms或msecond-表示相差的毫秒数 w或week-表示相差的周数
//      date1:起始日期
//      date2:结束日期
function DateDiff(interval,date1,date2)
{
    var TimeCom1 = new TimeCom(date1);
    var TimeCom2 = new TimeCom(date2);
    var result;
    switch(String(interval).toLowerCase())
    {
        case "y":
        case "year":
            result = TimeCom1.year-TimeCom2.year;
            break;
        case "n":
        case "month":
            result = (TimeCom1.year-TimeCom2.year)*12+(TimeCom1.month-TimeCom2.month);
            break;
        case "d":
        case "day":
            result = Math.round((Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day))/(1000*60*60*24));
            break;
        case "h":
        case "hour":
            result = Math.round((Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day,TimeCom1.hour)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day,TimeCom2.hour))/(1000*60*60));
            break;
        case "m":
        case "minute":
            result = Math.round((Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day,TimeCom1.hour,TimeCom1.minute)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day,TimeCom2.hour,TimeCom2.minute))/(1000*60));
            break;
        case "s":
        case "second":
            result = Math.round((Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day,TimeCom1.hour,TimeCom1.minute,TimeCom1.second)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day,TimeCom2.hour,TimeCom2.minute,TimeCom2.second))/1000);
            break;
        case "ms":
        case "msecond":
            result = Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day,TimeCom1.hour,TimeCom1.minute,TimeCom1.second,TimeCom1.msecond)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day,TimeCom2.hour,TimeCom2.minute,TimeCom2.second,TimeCom1.msecond);
            break;
        case "w":
        case "week":
            result = Math.round((Date.UTC(TimeCom1.year,TimeCom1.month-1,TimeCom1.day)-Date.UTC(TimeCom2.year,TimeCom2.month-1,TimeCom2.day))/(1000*60*60*24)) % 7;
            break;
        default:
            result = "invalid";
    }
    return(result);
}

//取得指定日期的年月日时分秒
//参数：dateValue 是格式形如：2007/04/05
function TimeCom(dateValue){
    var newCom = new Date(dateValue);
    this.year = newCom.getYear();
    this.month = newCom.getMonth()+1;
    this.day = newCom.getDate();
    this.hour = newCom.getHours();
    this.minute = newCom.getMinutes();
    this.second = newCom.getSeconds();
    this.msecond = newCom.getMilliseconds();
    this.week = newCom.getDay();
}