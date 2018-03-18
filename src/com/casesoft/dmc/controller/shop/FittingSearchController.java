package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.shop.FittingDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.Count;
import com.casesoft.dmc.model.shop.FittingView;
import com.casesoft.dmc.model.shop.FittingViewData;
import com.casesoft.dmc.service.shop.FittingViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/shop/fittingSearch")
public class FittingSearchController extends BaseController implements IBaseInfoController<FittingView> {


    @Autowired
     private  FittingDao fittingDao;
    @Autowired
    private FittingViewService fittingViewService;

    private int N=6;//排行取前几条

    @RequestMapping(value = "/index")
    public String index(){
        return "/views/shop/fittingSearch";
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult read(@RequestBody DataSourceRequest request) {

        DataSourceResult dataSourceResult =fittingDao.getList(request);
        for(FittingView fv:(List<FittingView>)dataSourceResult.getData()){
            PropertyKey pkc10=CacheManager.getPropertyKey("C10-R-"+fv.getClass10());
            if (pkc10!=null){
                fv.setClass10(pkc10.getName());
            }
            PropertyKey pkc4=CacheManager.getPropertyKey("C4-E-"+fv.getClass4());
            if (pkc4!=null){
                fv.setClass4(pkc4.getName());
            }
            PropertyKey pkc3=CacheManager.getPropertyKey("C3-D-"+fv.getClass3());
            if (pkc3!=null){
                fv.setClass3(pkc3.getName());
            }
            PropertyKey pkc1=CacheManager.getPropertyKey("C1-A-"+fv.getBrand());
            if (pkc1!=null){
                fv.setBrand(pkc1.getName());
            }
        }
        return dataSourceResult;
    }

    @RequestMapping(value = "/getEChartData")
    @ResponseBody
    public MessageBox getEChartData(){
        //大类
       List<Count> countC3 = this.fittingViewService.countC3();
      for (Count cc3:countC3){
          PropertyKey pkc3=CacheManager.getPropertyKey("C3-D-"+cc3.getName());
          if (pkc3!=null){
              cc3.setName(pkc3.getName());
          }
      }
        //季节
        List<Count> countC10 = this.fittingViewService.countC10();
        for (Count cc10:countC10){
            PropertyKey pkc10=CacheManager.getPropertyKey("C10-R-"+cc10.getName());

            if (pkc10!=null){
                cc10.setName(pkc10.getName());
            }
        }
        //门店排行
        List<Count> sortShop=this.fittingViewService.sortShop();
        List<Count> temp=new ArrayList<>();
        if (CommonUtil.isNotBlank(sortShop)){

            if (sortShop.size()>N){
                for (int i=0;i<N;i++){
                    temp.add(sortShop.get(i));
                }
            }else{
                for (int i=0;i<sortShop.size();i++){
                    temp.add(sortShop.get(i));
                }
            }
            sortShop=temp;
        };
        //颜色排行
        List<Count> sortColor=this.fittingViewService.sortColor();
        if (CommonUtil.isNotBlank(sortColor)){
            temp=new ArrayList<>();
            if (sortColor.size()>N){
                for (int i=0;i<N;i++){
                    temp.add(sortColor.get(i));
                }
            }
            else{
                for (int i=0;i<sortColor.size();i++){
                    temp.add(sortColor.get(i));
                }
            }
            sortColor=temp;
        }
        //款式排行
        List<Count> sortStyle=this.fittingViewService.sortStyle();
        if (CommonUtil.isNotBlank(sortStyle)){
            temp=new ArrayList<>();
            if (sortStyle.size()>N){
                for (int i=0;i<N;i++){
                    temp.add(sortStyle.get(i));
                }
            }
            else{
                for (int i=0;i<sortStyle.size();i++){
                    temp.add(sortStyle.get(i));
                }
            }
            sortStyle=temp;
        }

        //获取当前日期
        Date currentTime = new Date();

        //一年试衣总数
        List<Count> sumYear=this.fittingViewService.sumYear(new SimpleDateFormat("yyyy").format(currentTime));
        if (CommonUtil.isBlank(sumYear)){
            sumYear=new ArrayList<Count>();
            sumYear.add(new Count(new SimpleDateFormat("yyyy").format(currentTime),0L));
        }
        //一个月试衣总数
        List<Count> sumMonth=this.fittingViewService.sumMonth(new SimpleDateFormat("yyyy-MM").format(currentTime));
        if (CommonUtil.isBlank(sumMonth)){
            sumMonth=new ArrayList<Count>();
            sumMonth.add(new Count(new SimpleDateFormat("yyyy-MM").format(currentTime),0L));
        }
        //一天试衣总数
        List<Count> sumDay=this.fittingViewService.sumDay(new SimpleDateFormat("yyyy-MM-dd").format(currentTime));
        if (CommonUtil.isBlank(sumDay)){
            sumDay=new ArrayList<Count>();
            sumDay.add(new Count(new SimpleDateFormat("yyyy-MM-dd").format(currentTime),0L));
        }
        //一周试衣总数
        String ThisWeek=new SimpleDateFormat("yyyy-w").format(currentTime);
        String week[]=ThisWeek.split("-");
        if (week[1].length()==1){
            week[1]='0'+week[1];
        }
        ThisWeek=week[0]+"-"+week[1];
        List<Count> sumWeek=this.fittingViewService.sumWeek(ThisWeek);
        if (CommonUtil.isBlank(sumWeek)){
            sumWeek=new ArrayList<Count>();
            sumWeek.add(new Count(new SimpleDateFormat("yyyy-w").format(currentTime),0L));
        }
        //所有数据
        FittingViewData fittingViewData =new FittingViewData(countC3,countC10,sortShop,sortStyle,sumYear,sumMonth,sumDay,sumWeek,sortColor);
        return returnSuccessInfo("ok",fittingViewData);
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    void export(String fileName, String base64, String contentType,
                HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }

    @Override
    public Page<FittingView> findPage(Page<FittingView> page) throws Exception {
        return null;
    }

    @Override
    public List<FittingView> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(FittingView entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
