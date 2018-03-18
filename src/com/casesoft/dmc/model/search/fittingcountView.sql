select  fitting_day.wareh_id,fitting_day.scan_day,fitting_day.lastWeekDay,fitting_day.lastMonthDay,fitting_day.lastYearDay,fitting_day.year,fitting_day.quarter,fitting_day.month,fitting_day.week, fitting_day.qty as dayQty,
fitting_week.qty as weekQty,fitting_month.qty as monthQty,sale_year.qty as yearQty,sale_quarter.qty as quarterQty,
concat(fitting_day.wareh_id,fitting_day.scan_day) as id
from (select wareh_id,scan_day,
to_char(to_date(scan_day,'yyyy-mm-dd')-7,'yyyy-mm-dd') as lastWeekDay,
to_char(add_months(to_date(scan_day,'yyyy-mm-dd'),-1),'yyyy-mm-dd')as lastMonthDay,
to_char(add_months(to_date(scan_day,'yyyy-mm-dd'),-12),'yyyy-mm-dd')as lastYearDay,
year,quarter,month,week,sum(qty) qty from FITTING_DTL_VIEW

 group by wareh_id,scan_day,year,quarter,month,week) fitting_day 
 inner join
 (select wareh_id,year,month,week,sum(qty) qty from FITTING_DTL_VIEW
 group by wareh_id,year,month,week) fitting_week 
 on (fitting_day.wareh_id=fitting_week.wareh_id and fitting_day.week=fitting_week.week and fitting_day.year=fitting_week.year) 
 inner join
 (select wareh_id,year,month,sum(qty) qty from FITTING_DTL_VIEW
 group by wareh_id,year,month) fitting_month
 on (fitting_month.wareh_id=fitting_week.wareh_id and fitting_month.month=fitting_day.month and fitting_month.year=fitting_week.year)
 inner join
 (select wareh_id,year,sum(qty) qty from FITTING_DTL_VIEW
 group by wareh_id,year) sale_year
 on (sale_year.wareh_id=fitting_week.wareh_id and   sale_year.year=fitting_month.year) 
  inner join
 (select wareh_id,year,quarter,sum(qty) qty from FITTING_DTL_VIEW
 group by wareh_id,year,quarter) sale_quarter
 on (sale_quarter.wareh_id=fitting_day.wareh_id and   sale_quarter.quarter=fitting_day.quarter and    sale_quarter.year=fitting_day.year)
 --------------------------------------------
 select 
(select distinct dayQty from fitting_count_view b where b.scan_day=a.lastweekday and b.wareh_id=a.wareh_id)  as lastWeekDayQty,
(select distinct b.qty from fitting_count_week_view b where  b.wareh_id=a.wareh_id and a.week-1=b.week and a.year =b.year
)  as lastWeekQty,
(select distinct b.qty from fitting_count_month_view b where b.wareh_id=a.wareh_id and
 ltrim( to_char(to_date(a.lastMonthday,'YYYY-MM-DD'), 'mm'),'0')=b.month  
and ltrim( to_char(to_date(a.lastMonthday,'YYYY-MM-DD'), 'yyyy'),'0')=b.year) as lastmonthQty,
(select distinct b.Qty from fitting_count_year_view b where b.wareh_id=a.wareh_id and
  a.year-1 =b.year) as lastyearQty,
(select distinct b.monthQty from fitting_count_view b where b.wareh_id=a.wareh_id and
 ltrim( to_char(to_date(a.lastyearday,'YYYY-MM-DD'), 'mm'),'0')=b.month  
and ltrim( to_char(to_date(a.lastyearday,'YYYY-MM-DD'), 'yyyy'),'0')=b.year) as lastyearmonthQty,
(select distinct qty from fitting_count_week_view b where  b.wareh_id=a.wareh_id and a.week=b.week and a.year-1 =b.year
)  as lastyearWeekQty,
a."WAREH_ID",a."SCAN_DAY",a."LASTWEEKDAY",a."LASTMONTHDAY",a."LASTYEARDAY",a."YEAR",a."QUARTER",a."MONTH",a."WEEK",a."DAYQTY",a."WEEKQTY",a."MONTHQTY",a."YEARQTY",a."QUARTERQTY",a."ID" from fitting_count_view a