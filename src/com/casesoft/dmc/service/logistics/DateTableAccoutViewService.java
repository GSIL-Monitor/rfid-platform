package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.DateTableAccoutViewDao;
import com.casesoft.dmc.model.logistics.DateTableAccoutView;
import com.casesoft.dmc.model.search.DateStockDetail;
import com.casesoft.dmc.model.search.DetailStockView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2017/12/25.
 */
@Service
@Transactional
public class DateTableAccoutViewService implements IBaseService<DateTableAccoutView,String> {
    @Autowired
    private DateTableAccoutViewDao dateTableAccoutViewDao;
    @Override
    public Page<DateTableAccoutView> findPage(Page<DateTableAccoutView> page, List<PropertyFilter> filters) {
        return this.dateTableAccoutViewDao.findPage(page,filters);
    }

    @Override
    public void save(DateTableAccoutView entity) {

    }

    @Override
    public DateTableAccoutView load(String id) {
        return null;
    }

    @Override
    public DateTableAccoutView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DateTableAccoutView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DateTableAccoutView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DateTableAccoutView entity) {

    }

    @Override
    public void delete(DateTableAccoutView entity) {

    }

    @Override
    public void delete(String id) {

    }
    //查询两个时间内的对账流水记录
    public List<DateTableAccoutView> findStatementsInTime(Date startDate, Date endDate) {
        String hql = "from DateTableAccoutView where billDate between ? and ?";
        return this.dateTableAccoutViewDao.find(hql,new Object[]{startDate,endDate});
    }

    public Map<String,Double> findtiltle(String startDate,String endDate){
       /* try {*/

            //Date startDates=CommonUtil.converStrToDate(startDate,"yyyy-mm-dd");
            //Date endDates=CommonUtil.converStrToDate(endDate,"yyyy-mm-dd");
            //String hql="select sum(actPrice) from DateTableAccoutView where billDate <= to_date('"+endDate+"','yyyy-MM-dd') and  billDate >=to_date('"+startDate+"','yyyy-MM-dd') and billType='销售订单'";
            String salehql="select sum(actPrice) from DateTableAccoutView where to_char(billDate,'yyyy-MM-dd')=? and billType='销售订单'";
            Double saleNum = this.dateTableAccoutViewDao.findUnique(salehql,new Object[]{startDate});
            if(CommonUtil.isBlank(saleNum)){
                saleNum=0.0D;
            }
            String saleRetrunhql="select sum(actPrice) from DateTableAccoutView where to_char(billDate,'yyyy-MM-dd')=? and billType='销售退货申请单'";
            Double saleRetrunNum = this.dateTableAccoutViewDao.findUnique(saleRetrunhql,new Object[]{startDate});
            if(CommonUtil.isBlank(saleRetrunNum)){
                saleRetrunNum=0.0D;
            }
            String storedvaluehql="select sum(payPrice) from DateTableAccoutView where to_char(billDate,'yyyy-MM-dd')=? and billType in('收款','储值')";
            Double storedvalueNum = this.dateTableAccoutViewDao.findUnique(storedvaluehql,new Object[]{startDate});
            if(CommonUtil.isBlank(storedvalueNum)){
                storedvalueNum=0.0D;
            }
            String memberhql="select sum(diffPrice) from DateTableAccoutView where to_char(billDate,'yyyy-MM-dd')=? and billType in('销售订单','销售退货申请单') ";
            Double memberNum = this.dateTableAccoutViewDao.findUnique(memberhql,new Object[]{startDate});
            if(CommonUtil.isBlank(memberNum)){
                memberNum=0.0D;
            }
            Map<String,Double> map =new HashMap<String,Double>();
            map.put("saleNum",saleNum);
            map.put("saleRetrunNum",saleRetrunNum);
            map.put("storedvalueNum",storedvalueNum);
            map.put("memberNum",memberNum);
            return map;
        /*} catch (ParseException e) {
            e.printStackTrace();
            return null;
        }*/

    }

    public String savedateStockDetail( User currentUser){
        //查询仓库
        try{


            String hql="from Unit t where t.type=9 and t.ownerId=?";
            List<Unit> Units = this.dateTableAccoutViewDao.find(hql, new Object[]{currentUser.getOwnerId()});
            String parameter="";
            for(int i=0;i<Units.size();i++){
                 if(i==0){
                     parameter+="'"+Units.get(i).getId()+"'";
                 }else{
                     parameter+=",'"+Units.get(i).getId()+"'";
                 }
            }
            String findhql="from DetailStockView t where t.warehId in ("+parameter+")";
            List<DetailStockView> DetailStockViews = this.dateTableAccoutViewDao.find(findhql);
            List<DateStockDetail> list=new ArrayList<DateStockDetail>();
            Date date=new Date();
            String dates = CommonUtil.getDateString(date, "yyyy-MM-dd");

            this.dateTableAccoutViewDao.batchExecute("delete from DateStockDetail t where to_char(t.billDate,'yyyy-MM-dd')='"+dates+"' and t.warehId in ("+parameter+")");
            for(DetailStockView detailStockView:DetailStockViews){
                DateStockDetail dateStockDetail=new DateStockDetail();

                String dateString = CommonUtil.getDateString(date, "yyyy-MM-dd HH:mm:ss");
                dateStockDetail.setId(dateString+"_"+detailStockView.getWarehId()+"_"+detailStockView.getSku());
                dateStockDetail.setBillDate(date);
                dateStockDetail.setColorname(detailStockView.getColorname());
                //dateStockDetail.setInsotretype(detailStockView.getInsotretype());
                dateStockDetail.setInStockPrice(detailStockView.getInStockPrice());
                dateStockDetail.setOwnerId(detailStockView.getOwnerId());
                dateStockDetail.setPrecast(detailStockView.getPrecast());
                dateStockDetail.setPuprice(detailStockView.getPuprice());
                dateStockDetail.setQty(detailStockView.getQty());
                dateStockDetail.setSku(detailStockView.getSku());
                //dateStockDetail.setStockprice(detailStockView.getStockprice());
                dateStockDetail.setWarehId(detailStockView.getWarehId());
                dateStockDetail.setWarehName(detailStockView.getWarehName());
                dateStockDetail.setWarehType(detailStockView.getWarehType());
                dateStockDetail.setWsprice(detailStockView.getWsprice());
                dateStockDetail.setBrandCode(detailStockView.getBrandCode());
                dateStockDetail.setClass1(detailStockView.getClass1());
                dateStockDetail.setClass2(detailStockView.getClass2());
                dateStockDetail.setClass3(detailStockView.getClass3());
                dateStockDetail.setClass4(detailStockView.getClass4());
                dateStockDetail.setClass5(detailStockView.getClass5());
                dateStockDetail.setClass6(detailStockView.getClass6());
                dateStockDetail.setClass7(detailStockView.getClass7());
                dateStockDetail.setClass8(detailStockView.getClass8());
                dateStockDetail.setClass9(detailStockView.getClass9());
                dateStockDetail.setClass10(detailStockView.getClass10());
                dateStockDetail.setColorId(detailStockView.getColorId());
                dateStockDetail.setPrice(detailStockView.getPrice());
                dateStockDetail.setProdRemark(detailStockView.getProdRemark());
                dateStockDetail.setSizeId(detailStockView.getSizeId());
                dateStockDetail.setSizeSortId(detailStockView.getSizeSortId());
                dateStockDetail.setStyleEName(detailStockView.getStyleEName());
                dateStockDetail.setStyleId(detailStockView.getStyleId());
                dateStockDetail.setStyleName(detailStockView.getStyleName());
                list.add(dateStockDetail);
            }
            this.dateTableAccoutViewDao.doBatchInsert(list);
            return "结算成功";
        }catch (Exception e){
            return e.getMessage();
        }

    }

    public void allUnitdateStockDetail(){
        //查询仓库
        try{


            String hql="from Unit t where t.type=9";
            List<Unit> Units = this.dateTableAccoutViewDao.find(hql);
            String parameter="";
            for(int i=0;i<Units.size();i++){
                if(i==0){
                    parameter+="'"+Units.get(i).getId()+"'";
                }else{
                    parameter+=",'"+Units.get(i).getId()+"'";
                }
            }
            String findhql="from DetailStockView t where t.warehId in ("+parameter+")";
            List<DetailStockView> DetailStockViews = this.dateTableAccoutViewDao.find(findhql);
            List<DateStockDetail> list=new ArrayList<DateStockDetail>();
            Date date=new Date();
            String dates = CommonUtil.getDateString(date, "yyyy-MM-dd");

            this.dateTableAccoutViewDao.batchExecute("delete from DateStockDetail t where to_char(t.billDate,'yyyy-MM-dd')='"+dates+"' and t.warehId in ("+parameter+")");
            for(DetailStockView detailStockView:DetailStockViews){
                DateStockDetail dateStockDetail=new DateStockDetail();

                String dateString = CommonUtil.getDateString(date, "yyyy-MM-dd HH:mm:ss");
                dateStockDetail.setId(dateString+"_"+detailStockView.getWarehId()+"_"+detailStockView.getSku());
                dateStockDetail.setBillDate(date);
                dateStockDetail.setColorname(detailStockView.getColorname());
                //dateStockDetail.setInsotretype(detailStockView.getInsotretype());
                dateStockDetail.setInStockPrice(detailStockView.getInStockPrice());
                dateStockDetail.setOwnerId(detailStockView.getOwnerId());
                dateStockDetail.setPrecast(detailStockView.getPrecast());
                dateStockDetail.setPuprice(detailStockView.getPuprice());
                dateStockDetail.setQty(detailStockView.getQty());
                dateStockDetail.setSku(detailStockView.getSku());
                //dateStockDetail.setStockprice(detailStockView.getStockprice());
                dateStockDetail.setWarehId(detailStockView.getWarehId());
                dateStockDetail.setWarehName(detailStockView.getWarehName());
                dateStockDetail.setWarehType(detailStockView.getWarehType());
                dateStockDetail.setWsprice(detailStockView.getWsprice());
                dateStockDetail.setBrandCode(detailStockView.getBrandCode());
                dateStockDetail.setClass1(detailStockView.getClass1());
                dateStockDetail.setClass2(detailStockView.getClass2());
                dateStockDetail.setClass3(detailStockView.getClass3());
                dateStockDetail.setClass4(detailStockView.getClass4());
                dateStockDetail.setClass5(detailStockView.getClass5());
                dateStockDetail.setClass6(detailStockView.getClass6());
                dateStockDetail.setClass7(detailStockView.getClass7());
                dateStockDetail.setClass8(detailStockView.getClass8());
                dateStockDetail.setClass9(detailStockView.getClass9());
                dateStockDetail.setClass10(detailStockView.getClass10());
                dateStockDetail.setColorId(detailStockView.getColorId());
                dateStockDetail.setPrice(detailStockView.getPrice());
                dateStockDetail.setProdRemark(detailStockView.getProdRemark());
                dateStockDetail.setSizeId(detailStockView.getSizeId());
                dateStockDetail.setSizeSortId(detailStockView.getSizeSortId());
                dateStockDetail.setStyleEName(detailStockView.getStyleEName());
                dateStockDetail.setStyleId(detailStockView.getStyleId());
                dateStockDetail.setStyleName(detailStockView.getStyleName());
                list.add(dateStockDetail);
            }
            this.dateTableAccoutViewDao.doBatchInsert(list);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
