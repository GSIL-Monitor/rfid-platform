package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.NoPushProductDao;
import com.casesoft.dmc.dao.product.NoPushStyleDao;
import com.casesoft.dmc.dao.product.ProductDao;
import com.casesoft.dmc.dao.product.StyleDao;
import com.casesoft.dmc.extend.api.web.epay.alipay.config.AlipayConfig;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpProtocolHandler;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpRequest;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResponse;
import com.casesoft.dmc.extend.api.web.epay.alipay.util.httpClient.HttpResultType;
import com.casesoft.dmc.model.product.NoPushProduct;
import com.casesoft.dmc.model.product.NoPushStyle;

import org.apache.commons.httpclient.NameValuePair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/4.
 */
@Service
@Transactional
public class NoPushProductService extends AbstractBaseService<NoPushProduct, String> {
    @Autowired
    private NoPushProductDao noPushProductDao;
    @Autowired
    private NoPushStyleDao noPushStyleDao;

    @Autowired
    private StyleDao styleDao;
    @Autowired
    private ProductDao productDao;
    @Override
    public Page<NoPushProduct> findPage(Page<NoPushProduct> page, List<PropertyFilter> filters) {
        return this.noPushProductDao.findPage(page,filters);
    }

    @Override
    public void save(NoPushProduct entity) {

    }

    @Override
    public NoPushProduct load(String id) {
        return null;
    }

    @Override
    public NoPushProduct get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<NoPushProduct> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<NoPushProduct> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(NoPushProduct entity) {

    }

    @Override
    public void delete(NoPushProduct entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<NoPushProduct> findNoPushProductByStyleid(String styleid){
        String hql="from NoPushProduct t where t.styleId=?";
        List<NoPushProduct> list = this.noPushProductDao.find(hql, new Object[]{styleid});
        return list;
    }
    public void WxShopStyle(NoPushStyle style, List<NoPushProduct> saveList) {
        try {
            HttpProtocolHandler instance7 = HttpProtocolHandler.getInstance();
            HttpRequest httpRequest7=new HttpRequest(HttpResultType.STRING);
            httpRequest7.setMethod("post");
            String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
            httpRequest7.setUrl(wxshop_Path+"/v2/product/saveForRfid");
           // httpRequest7.setUrl("http://ancientstone.mobi/v2/product/saveForRfid");
            int allpas=7+saveList.size()*3+1;
            NameValuePair[] allnameValuePair = new NameValuePair[allpas];
            NameValuePair nameValuePadescription=new NameValuePair();
            nameValuePadescription.setName("description");
            nameValuePadescription.setValue(style.getStyleName());
            allnameValuePair[0]=nameValuePadescription;
            NameValuePair nameValueParecommend=new NameValuePair();
            nameValueParecommend.setName("recommend");
            nameValueParecommend.setValue("0");
            allnameValuePair[1]=nameValueParecommend;
            NameValuePair nameValuePastatus=new NameValuePair();
            nameValuePastatus.setName("status");
            nameValuePastatus.setValue("FORSALE");
            allnameValuePair[2]=nameValuePastatus;
            NameValuePair nameValuePacategory=new NameValuePair();
            nameValuePacategory.setName("category");
            nameValuePacategory.setValue("");
            allnameValuePair[3]=nameValuePacategory;
            NameValuePair nameValuePaname=new NameValuePair();
            nameValuePaname.setName("name");
            nameValuePaname.setValue(style.getStyleName());
            allnameValuePair[4]=nameValuePaname;
            NameValuePair nameValuerfidStyleId=new NameValuePair();
            nameValuerfidStyleId.setName("rfidStyleId");
            nameValuerfidStyleId.setValue(style.getStyleId());
            allnameValuePair[5]=nameValuerfidStyleId;
            NameValuePair nameValueprice=new NameValuePair();
            nameValueprice.setName("price");
            nameValueprice.setValue(style.getPrice()+"");
            allnameValuePair[6]=nameValueprice;
            NameValuePair nameValueoriginalPrice=new NameValuePair();
            nameValueoriginalPrice.setName("originalPrice");
            nameValueoriginalPrice.setValue(style.getPreCast()+"");
            allnameValuePair[7]=nameValueoriginalPrice;
            int sum=7;
            String idmap="";
            for(int i=0;i<saveList.size();i++){
                sum++;
                NameValuePair nameValueoprice=new NameValuePair();
                nameValueoprice.setName("skus["+i+"].price");
                nameValueoprice.setValue("0");
                allnameValuePair[sum]=nameValueoprice;
                sum++;
                NameValuePair nameValueamount=new NameValuePair();
                nameValueamount.setName("skus["+i+"].amount");
                nameValueamount.setValue("0");
                allnameValuePair[sum]=nameValueamount;
                sum++;
                NameValuePair nameValuespec=new NameValuePair();
                nameValuespec.setName("skus["+i+"].spec");
                nameValuespec.setValue(saveList.get(i).getColorId()+","+saveList.get(i).getSizeId());
                allnameValuePair[sum]=nameValuespec;
                if(i==0){
                    idmap+="'"+saveList.get(i).getId()+"'";
                }else{
                    idmap+=",'"+saveList.get(i).getId()+"'";
                }
            }


            httpRequest7.setCharset(AlipayConfig.input_charset);
            httpRequest7.setParameters(allnameValuePair);
            HttpResponse httpResponse;
            httpResponse = instance7.execute(httpRequest7, "", "");
            if(CommonUtil.isNotBlank(httpResponse)) {
                String stringResult = httpResponse.getStringResult();
                if (CommonUtil.isNotBlank(stringResult)) {
                    Map<String, Object> map = JSONUtil.getMap4Json(stringResult);
                    Integer errorCode = Integer.parseInt(map.get("errorCode") + "");

                    if (errorCode == 200) {
                        String floorHql = "update Product f set f.push = 'Y' where f.id in (" + idmap + ")";
                        this.productDao.batchExecute(floorHql);
                        String Hql = "update NoPushStyle f set f.pushsuccess = 'Y' where f.id = '" + style.getId() + "'";
                        this.noPushStyleDao.batchExecute(Hql);

                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();

        }
    }


}
