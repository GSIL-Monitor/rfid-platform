package com.casesoft.dmc.extend.third.controller;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.service.ISynErpService;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.extend.third.service.DayThirdStockService;
import com.casesoft.dmc.extend.third.service.ThirdStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by john on 2017-02-22.
 */
@RequestMapping("/third/daystock")
@Controller
public class DayThirdStockController extends BaseController{

    @Autowired
    private DayThirdStockService dayThirdStockService;
    @Autowired
    private ThirdStockService thirdStockService;
    @Autowired
    private ISynErpService synErpService;

    @Override
    public String index() {
        return null;
    }
     @ResponseBody
    @RequestMapping("/syn")
    public MessageBox synDayThirdStock(){
         dayThirdStockService.freshMaterializedView();
     /*   List<DayThirdStock> dayThirdStockList=new ArrayList<>();
            Date sysDate=new Date();
            String begin=CommonUtil.getDateString(sysDate,"yyyy-MM-dd");
            for(int i=1;i<=5;i++){
                try{
                    List<DayThirdStock> dayThirdStocks =  synErpService.synchronizeDayThirdStock("",CommonUtil.reduceDay(begin,i));
                    if(CommonUtil.isNotBlank(dayThirdStocks)){
                        dayThirdStockList.addAll(dayThirdStocks);
                    }
                    System.out.println(CommonUtil.reduceDay(begin,i) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
      if(CommonUtil.isNotBlank(dayThirdStockList)){
          this.dayThirdStockService.doBatchInsert(dayThirdStockList);
          System.out.println(dayThirdStockList.size());

      }*/
     return this.returnSuccessInfo("");
    }
    @ResponseBody
    @RequestMapping("/synStock")
    public MessageBox synThirdStock(){

        List<ThirdStock> dayThirdStockList=  this.synErpService.synchronizeThirdStock("1");

            this.thirdStockService.doBatchInsert(dayThirdStockList);
            System.out.println(dayThirdStockList.size());
        return this.returnSuccessInfo("");
    }
}
