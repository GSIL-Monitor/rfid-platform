package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.FittingViewDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.shop.Count;
import com.casesoft.dmc.model.shop.FittingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FittingViewService extends BaseService<FittingView,String> {

    @Autowired
    private FittingViewDao fittingViewDao;

    public List<Count>  countC3(){
        String hql="select new com.casesoft.dmc.model.shop.Count(class3, sum(fT))  from FittingView GROUP BY CLASS3 ";
         return  this.fittingViewDao.find(hql);
    }

    public List<Count> countC10(){
        String hql="select new com.casesoft.dmc.model.shop.Count(class10,sum(fT)) from FittingView GROUP BY CLASS10";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sortShop(){
       // String hql="select new com.casesoft.dmc.model.shop.Count( u.name ,ft.qty) from Unit u left join (select f.ownerId,count(*) qty from FittingView f group by f.ownerId ) ft  where u.id=ft.ownerId ORDER BY ft.qty desc";
        List<Count> count=this.fittingViewDao.creatSQLQuery("select * from (select u.NAME , CASE when ft.qty is null then 0 ELSE ft.qty end  as qty  from (select * from SYS_UNIT su where su.type=4) u"
                        + "   LEFT JOIN (select f.OWNERID,count(*)   qty from SHOP_FITTINGRECORD f group by f.OWNERID ) ft  on u.ID=ft.OWNERID )"
             +"   f ORDER BY f.qty desc").list();
        return count;
    }

    public List<Count> sortColor(){
        String hql="select new com.casesoft.dmc.model.shop.Count(color, sum(fT))  from FittingView  group by color order by sum(fT) desc";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sortStyle(){
        String hql="select new com.casesoft.dmc.model.shop.Count(style,sum(fT)) from FittingView  group by style ORDER BY sum(fT) desc";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sumYear(String Year){
        String hql="SELECT new com.casesoft.dmc.model.shop.Count(TO_CHAR(scanTime, 'yyyy') ,COUNT(*))FROM FittingRecord  where TO_CHAR(scanTime, 'yyyy')="+Year+" GROUP BY TO_CHAR(scanTime, 'yyyy')";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sumMonth(String YM){
        String hql="SELECT new com.casesoft.dmc.model.shop.Count(TO_CHAR(scanTime, 'yyyy-MM') ,COUNT(*))FROM FittingRecord  where TO_CHAR(scanTime, 'yyyy-MM')='"+YM+"' GROUP BY TO_CHAR(scanTime, 'yyyy-MM')";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sumDay(String YMD){
        String hql="SELECT new com.casesoft.dmc.model.shop.Count(TO_CHAR(scanTime, 'yyyy-MM-dd') ,COUNT(*))FROM FittingRecord  where TO_CHAR(scanTime, 'yyyy-MM-dd')='"+YMD+"' GROUP BY TO_CHAR(scanTime, 'yyyy-MM-dd')";
        return this.fittingViewDao.find(hql);
    }

    public List<Count> sumWeek(String Week){
        String hql="SELECT new com.casesoft.dmc.model.shop.Count(TO_CHAR(scanTime, 'yyyy-iw') ,COUNT(*))FROM FittingRecord  where TO_CHAR(scanTime, 'yyyy-iw')='"+Week+"' GROUP BY TO_CHAR(scanTime, 'yyyy-iw')";
        return this.fittingViewDao.find(hql);
    }
    @Override
    public Page<FittingView> findPage(Page<FittingView> page, List<PropertyFilter> filters) {
        return this.fittingViewDao.findPage(page,filters);
    }

    @Override
    public void save(FittingView entity) {

    }

    @Override
    public FittingView load(String id) {
        return null;
    }

    @Override
    public FittingView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<FittingView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<FittingView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(FittingView entity) {

    }

    @Override
    public void delete(FittingView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
