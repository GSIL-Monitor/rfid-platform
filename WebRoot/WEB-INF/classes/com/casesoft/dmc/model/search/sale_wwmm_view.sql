select 
(select distinct dayQty from sale_count_view b where b.scan_day=a.lastweekday and b.wareh_id=a.wareh_id)  as lastWeekDayQty,
(select distinct weekQty from sale_count_view b where  b.wareh_id=a.wareh_id and a.week-1=b.week and a.year =b.year
)  as lastWeekQty,
(select distinct b.monthQty from sale_count_view b where b.wareh_id=a.wareh_id and
 ltrim( to_char(to_date(a.lastMonthday,'YYYY-MM-DD'), 'mm'),'0')=b.month  
and ltrim( to_char(to_date(a.lastMonthday,'YYYY-MM-DD'), 'yyyy'),'0')=b.year) as lastmonthQty,

(select distinct b.yearQty from sale_count_view b where b.wareh_id=a.wareh_id and
  a.year-1 =b.year) as lastyearQty,

(select distinct b.monthQty from sale_count_view b where b.wareh_id=a.wareh_id and
 ltrim( to_char(to_date(a.lastyearday,'YYYY-MM-DD'), 'mm'),'0')=b.month  
and ltrim( to_char(to_date(a.lastyearday,'YYYY-MM-DD'), 'yyyy'),'0')=b.year) as lastyearmonthQty,
(select distinct weekQty from sale_count_view b where  b.wareh_id=a.wareh_id and a.week=b.week and a.year-1 =b.year
)  as lastyearWeekQty,
a.* from sale_count_view a order by a.scan_day;