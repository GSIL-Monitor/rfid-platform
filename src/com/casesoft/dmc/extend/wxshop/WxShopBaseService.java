package com.casesoft.dmc.extend.wxshop;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
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
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.service.push.pushBaseInfo;
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
public class WxShopBaseService implements pushBaseInfo {
    @Autowired
    private NoPushStyleDao noPushStyleDao;
    @Autowired
    private NoPushProductDao noPushProductDao;
    @Autowired
    private StyleDao styleDao;
    @Autowired
    private ProductDao productDao;


    @Override
    public Boolean WxShopStyle(Style style, List<Product> saveList) {
        try {
            HttpProtocolHandler instance7 = HttpProtocolHandler.getInstance();
            HttpRequest httpRequest7=new HttpRequest(HttpResultType.STRING);
            httpRequest7.setMethod("post");
            String wxshop_Path = PropertyUtil.getValue("wxshop_Path");
            if(CommonUtil.isNotBlank( wxshop_Path)){
                httpRequest7.setUrl(wxshop_Path+"/v2/product/saveForRfid");
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
                String stringResult = httpResponse.getStringResult();
                if(CommonUtil.isNotBlank(stringResult)) {
                    Map<String, Object> map = JSONUtil.getMap4Json(stringResult);
                    Integer errorCode = Integer.parseInt(map.get("errorCode") + "");

                    if (errorCode == 200) {
                        String floorHql = "update Product f set f.push = 'Y' where f.id in (" + idmap + ")";
                        this.productDao.batchExecute(floorHql);
                        return true;
                    } else {

                        NoPushStyle noPushStyle = new NoPushStyle();
                        noPushStyle.setId(style.getId());
                        noPushStyle.setIspush(style.getIspush());
                        noPushStyle.setIsUse(style.getIsUse());
                        noPushStyle.setPushsuccess("N");
                        noPushStyle.setRemark(style.getRemark());
                        noPushStyle.setSeqNo(style.getSeqNo());
                        noPushStyle.setStyleEname(style.getStyleEname());
                        noPushStyle.setBrandCode(style.getBrandCode());
                        noPushStyle.setBrandName(style.getBrandName());
                        noPushStyle.setClass1(style.getClass1());
                        noPushStyle.setClass2(style.getClass2());
                        noPushStyle.setClass3(style.getClass3());
                        noPushStyle.setClass4(style.getClass4());
                        noPushStyle.setClass5(style.getClass5());
                        noPushStyle.setClass6(style.getClass6());
                        noPushStyle.setClass7(style.getClass7());
                        noPushStyle.setClass8(style.getClass8());
                        noPushStyle.setClass9(style.getClass9());
                        noPushStyle.setClass10(style.getClass10());
                        noPushStyle.setExpDate(style.getExpDate());
                        noPushStyle.setIdent(style.getIdent());
                        noPushStyle.setImage(style.getImage());
                        noPushStyle.setIsReexp(style.getIsReexp());
                        noPushStyle.setPreCast(style.getPreCast());
                        noPushStyle.setOprId(style.getOprId());
                        noPushStyle.setPrice(style.getPrice());
                        noPushStyle.setPuPrice(style.getPuPrice());
                        noPushStyle.setSalesDate(style.getSalesDate());
                        noPushStyle.setSampleCode(style.getSampleCode());
                        noPushStyle.setSizeSortId(style.getSizeSortId());
                        noPushStyle.setSizeSortName(style.getSizeSortName());
                        noPushStyle.setStyleId(style.getStyleId());
                        noPushStyle.setStyleName(style.getStyleName());
                        noPushStyle.setThumb(style.getThumb());
                        noPushStyle.setUpdateTime(style.getUpdateTime());
                        noPushStyle.setWsPrice(style.getWsPrice());
                        List<NoPushProduct> list = new ArrayList<NoPushProduct>();
                        for (Product product : saveList) {
                            NoPushProduct noPushProduct = new NoPushProduct();
                            noPushProduct.setId(product.getId());
                            noPushProduct.setBoxQty(product.getBoxQty());
                            noPushProduct.setCode(product.getCode());
                            noPushProduct.setCollocation(product.getCollocation());
                            noPushProduct.setColorId(product.getColorId());
                            noPushProduct.setImage(product.getImage());
                            noPushProduct.setImages(product.getImages());
                            noPushProduct.setIsDeton(product.getIsDeton());
                            noPushProduct.setIsSample(product.getIsSample());
                            noPushProduct.setIsUse(product.getIsUse());
                            noPushProduct.setPush("N");
                            noPushProduct.setRemark(product.getRemark());
                            noPushProduct.setSizeId(product.getSizeId());
                            noPushProduct.setSizeSortId(product.getSizeSortId());
                            noPushProduct.setStyleId(product.getStyleId());
                            noPushProduct.setVersion(product.getVersion());
                            noPushProduct.setBarcode(product.getBarcode());
                            noPushProduct.setBrandCode(product.getBrandCode());
                            noPushProduct.setBrandName(product.getBrandName());
                            noPushProduct.setClass1(product.getClass1());
                            noPushProduct.setClass1Name(product.getClass1Name());
                            noPushProduct.setClass2(product.getClass2());
                            noPushProduct.setClass2Name(product.getClass2Name());
                            noPushProduct.setClass3(product.getClass3());
                            noPushProduct.setClass3Name(product.getClass3Name());
                            noPushProduct.setClass4(product.getClass4());
                            noPushProduct.setClass4Name(product.getClass4Name());
                            noPushProduct.setClass5(product.getClass5());
                            noPushProduct.setClass5Name(product.getClass5Name());
                            noPushProduct.setClass6(product.getClass6());
                            noPushProduct.setClass6Name(product.getClass6Name());
                            noPushProduct.setClass7(product.getClass7());
                            noPushProduct.setClass7Name(product.getClass7Name());
                            noPushProduct.setClass8(product.getClass8());
                            noPushProduct.setClass8Name(product.getClass8Name());
                            noPushProduct.setClass9(product.getClass9());
                            noPushProduct.setClass9Name(product.getClass9Name());
                            noPushProduct.setClass10(product.getClass10());
                            noPushProduct.setClass10Name(product.getClass10Name());
                            noPushProduct.setColorName(product.getColorName());
                            noPushProduct.setEan(product.getEan());
                            noPushProduct.setOprId(product.getOprId());
                            noPushProduct.setPreCast(product.getPreCast());
                            noPushProduct.setPrice(product.getPrice());
                            noPushProduct.setPuPrice(product.getPuPrice());
                            noPushProduct.setSizeName(product.getSizeName());
                            noPushProduct.setStyleName(product.getStyleName());
                            noPushProduct.setSizeSortName(product.getSizeSortName());
                            noPushProduct.setStyleRemark(product.getStyleRemark());
                            noPushProduct.setStyleSortName(product.getStyleSortName());
                            noPushProduct.setUpdateTime(product.getUpdateTime());
                            noPushProduct.setWsPrice(product.getWsPrice());
                            list.add(noPushProduct);
                        }
                        this.noPushStyleDao.saveOrUpdate(noPushStyle);
                        this.noPushProductDao.doBatchInsert(list);

                        return false;
                    }
                }else {
                    return false;
                }
            }else {
                NoPushStyle noPushStyle = new NoPushStyle();
                noPushStyle.setId(style.getId());
                noPushStyle.setIspush(style.getIspush());
                noPushStyle.setIsUse(style.getIsUse());
                noPushStyle.setPushsuccess("N");
                noPushStyle.setRemark(style.getRemark());
                noPushStyle.setSeqNo(style.getSeqNo());
                noPushStyle.setStyleEname(style.getStyleEname());
                noPushStyle.setBrandCode(style.getBrandCode());
                noPushStyle.setBrandName(style.getBrandName());
                noPushStyle.setClass1(style.getClass1());
                noPushStyle.setClass2(style.getClass2());
                noPushStyle.setClass3(style.getClass3());
                noPushStyle.setClass4(style.getClass4());
                noPushStyle.setClass5(style.getClass5());
                noPushStyle.setClass6(style.getClass6());
                noPushStyle.setClass7(style.getClass7());
                noPushStyle.setClass8(style.getClass8());
                noPushStyle.setClass9(style.getClass9());
                noPushStyle.setClass10(style.getClass10());
                noPushStyle.setExpDate(style.getExpDate());
                noPushStyle.setIdent(style.getIdent());
                noPushStyle.setImage(style.getImage());
                noPushStyle.setIsReexp(style.getIsReexp());
                noPushStyle.setPreCast(style.getPreCast());
                noPushStyle.setOprId(style.getOprId());
                noPushStyle.setPrice(style.getPrice());
                noPushStyle.setPuPrice(style.getPuPrice());
                noPushStyle.setSalesDate(style.getSalesDate());
                noPushStyle.setSampleCode(style.getSampleCode());
                noPushStyle.setSizeSortId(style.getSizeSortId());
                noPushStyle.setSizeSortName(style.getSizeSortName());
                noPushStyle.setStyleId(style.getStyleId());
                noPushStyle.setStyleName(style.getStyleName());
                noPushStyle.setThumb(style.getThumb());
                noPushStyle.setUpdateTime(style.getUpdateTime());
                noPushStyle.setWsPrice(style.getWsPrice());
                List<NoPushProduct> list = new ArrayList<NoPushProduct>();
                for (Product product : saveList) {
                    NoPushProduct noPushProduct = new NoPushProduct();
                    noPushProduct.setId(product.getId());
                    noPushProduct.setBoxQty(product.getBoxQty());
                    noPushProduct.setCode(product.getCode());
                    noPushProduct.setCollocation(product.getCollocation());
                    noPushProduct.setColorId(product.getColorId());
                    noPushProduct.setImage(product.getImage());
                    noPushProduct.setImages(product.getImages());
                    noPushProduct.setIsDeton(product.getIsDeton());
                    noPushProduct.setIsSample(product.getIsSample());
                    noPushProduct.setIsUse(product.getIsUse());
                    noPushProduct.setPush("N");
                    noPushProduct.setRemark(product.getRemark());
                    noPushProduct.setSizeId(product.getSizeId());
                    noPushProduct.setSizeSortId(product.getSizeSortId());
                    noPushProduct.setStyleId(product.getStyleId());
                    noPushProduct.setVersion(product.getVersion());
                    noPushProduct.setBarcode(product.getBarcode());
                    noPushProduct.setBrandCode(product.getBrandCode());
                    noPushProduct.setBrandName(product.getBrandName());
                    noPushProduct.setClass1(product.getClass1());
                    noPushProduct.setClass1Name(product.getClass1Name());
                    noPushProduct.setClass2(product.getClass2());
                    noPushProduct.setClass2Name(product.getClass2Name());
                    noPushProduct.setClass3(product.getClass3());
                    noPushProduct.setClass3Name(product.getClass3Name());
                    noPushProduct.setClass4(product.getClass4());
                    noPushProduct.setClass4Name(product.getClass4Name());
                    noPushProduct.setClass5(product.getClass5());
                    noPushProduct.setClass5Name(product.getClass5Name());
                    noPushProduct.setClass6(product.getClass6());
                    noPushProduct.setClass6Name(product.getClass6Name());
                    noPushProduct.setClass7(product.getClass7());
                    noPushProduct.setClass7Name(product.getClass7Name());
                    noPushProduct.setClass8(product.getClass8());
                    noPushProduct.setClass8Name(product.getClass8Name());
                    noPushProduct.setClass9(product.getClass9());
                    noPushProduct.setClass9Name(product.getClass9Name());
                    noPushProduct.setClass10(product.getClass10());
                    noPushProduct.setClass10Name(product.getClass10Name());
                    noPushProduct.setColorName(product.getColorName());
                    noPushProduct.setEan(product.getEan());
                    noPushProduct.setOprId(product.getOprId());
                    noPushProduct.setPreCast(product.getPreCast());
                    noPushProduct.setPrice(product.getPrice());
                    noPushProduct.setPuPrice(product.getPuPrice());
                    noPushProduct.setSizeName(product.getSizeName());
                    noPushProduct.setStyleName(product.getStyleName());
                    noPushProduct.setSizeSortName(product.getSizeSortName());
                    noPushProduct.setStyleRemark(product.getStyleRemark());
                    noPushProduct.setStyleSortName(product.getStyleSortName());
                    noPushProduct.setUpdateTime(product.getUpdateTime());
                    noPushProduct.setWsPrice(product.getWsPrice());
                    list.add(noPushProduct);
                }
                this.noPushStyleDao.saveOrUpdate(noPushStyle);
                this.noPushProductDao.doBatchInsert(list);

                return false;
            }



        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

}
