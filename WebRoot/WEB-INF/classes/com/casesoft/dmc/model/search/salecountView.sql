select  sale_day.wareh_id,sale_day.scan_day,sale_day.lastWeekDay,sale_day.lastMonthDay,sale_day.lastYearDay,sale_day.year,sale_day.quarter,sale_day.month,sale_day.week, sale_day.qty as dayQty,
sale_week.qty as weekQty,sale_month.qty as monthQty,sale_year.qty as yearQty,sale_quarter.qty as quarterQty,
concat(sale_day.wareh_id,sale_day.scan_day) as id
from (select wareh_id,scan_day,
to_char(to_date(scan_day,'yyyy-mm-dd')-7,'yyyy-mm-dd') as lastWeekDay,
to_char(add_months(to_date(scan_day,'yyyy-mm-dd'),-1),'yyyy-mm-dd')as lastMonthDay,
to_char(add_months(to_date(scan_day,'yyyy-mm-dd'),-12),'yyyy-mm-dd')as lastYearDay,
year,quarter,month,week,sum(qty) qty from bill_dtl_sale_view

 group by wareh_id,scan_day,year,quarter,month,week) sale_day 
 inner join
 (select wareh_id,year,week,sum(qty) qty from bill_dtl_sale_view
 group by wareh_id,year,week) sale_week 
 on (sale_day.wareh_id=sale_week.wareh_id and sale_day.week=sale_week.week and sale_day.year=sale_week.year) 
 inner join
 (select wareh_id,year,month,sum(qty) qty from bill_dtl_sale_view
 group by wareh_id,year,month) sale_month
 on (sale_month.wareh_id=sale_week.wareh_id  and sale_month.year=sale_week.year and sale_month.month=sale_day.month)
 inner join
 (select wareh_id,year,sum(qty) qty from bill_dtl_sale_view
 group by wareh_id,year) sale_year
 on (sale_year.wareh_id=sale_week.wareh_id and   sale_year.year=sale_month.year) 
  inner join
 (select wareh_id,year,quarter,sum(qty) qty from bill_dtl_sale_view
 group by wareh_id,year,quarter) sale_quarter
 on (sale_quarter.wareh_id=sale_day.wareh_id and   sale_quarter.quarter=sale_day.quarter and    sale_quarter.year=sale_day.year)
 order by scan_day;