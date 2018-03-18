package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.FactoryCategory;
import com.casesoft.dmc.model.factory.Token;
import com.casesoft.dmc.service.factory.FactoryTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GuoJunwen on 2017-05-04.
 */
@Controller
@RequestMapping("/factory/token")
public class FactoryTokenController extends BaseController implements IBaseInfoController<Token> {

    @Autowired
    private FactoryTokenService factoryTokenService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/factory/factoryToken";
    }

    @RequestMapping("/findToken")
    @ResponseBody
    public void findToken() throws Exception{
        this.logAllRequestParams();
        List<Token> list = this.factoryTokenService.findAllToken();
        this.returnSuccess("ok",list);
    }
    @RequestMapping("/findTokenById")
    @ResponseBody
    public MessageBox findTokenById(Integer token) throws Exception{
       return returnSuccessInfo("ok",CacheManager.getFactoryTokenByToken(token));
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<Token> findPage(Page<Token> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.factoryTokenService.findPage(page,filters);
        for (Token t:page.getRows()){
            Token lastToken=CacheManager.getFactoryTokenByToken(t.getLastToken());
            if (CommonUtil.isNotBlank(lastToken)){
                t.setLastTokenName(lastToken.getName());
            }
        }
        return page;
    }


    @RequestMapping("/list")
    @ResponseBody
    @Override
    public List<Token> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Token> tokenList=this.factoryTokenService.find(filters);
        return tokenList;
    }

    @RequestMapping("/startUsing")
    @ResponseBody
    public MessageBox startUsing(Integer token){
       this.factoryTokenService.startUsing(token);
       return returnSuccessInfo("ok");
    }


    @RequestMapping("/stopUsing")
    @ResponseBody
    public MessageBox stopUsing(Integer token){
        this.factoryTokenService.stopUsing(token);
        return returnSuccessInfo("ok");
    }

    /**
     * 交换两个token的顺序
     * @param firstToken
     * @param secondToken
     * @return
     */
    @RequestMapping("/exchangeSortIndex")
    @ResponseBody
    public MessageBox exchangeSortIndex(Integer firstToken,Integer secondToken){
        Token firstT=this.factoryTokenService.findToken(firstToken);
        Token secondT=this.factoryTokenService.findToken(secondToken);
        int firstSortIndex=firstT.getSortIndex();
        if(CommonUtil.isNotBlank(secondT)){
            int secondSortIndex=secondT.getSortIndex();
            firstT.setSortIndex(secondSortIndex);
            secondT.setSortIndex(firstSortIndex);
            try{
                this.factoryTokenService.save2Token(firstT,secondT);
                return returnSuccessInfo("ok");
            }catch (Exception e){
               return returnFailInfo("error");
            }
        }else{
            return returnFailInfo("error");
        }
    }


    @RequestMapping("/save")
    @ResponseBody
    @Override
    public MessageBox save(Token token) throws Exception {
        if(CommonUtil.isBlank(token.getToken())){
            token.setToken(this.factoryTokenService.findMaxToken()+1);
            token.setSortIndex(this.factoryTokenService.findMaxSortIndex()+1);
            token.setLocked(0);
        }else{
            token.setSortIndex(CacheManager.getFactoryTokenByToken(token.getToken()).getSortIndex());
            token.setLocked(CacheManager.getFactoryTokenByToken(token.getToken()).getLocked());
        }
        try{
            token.setName(token.getName().trim());
            this.factoryTokenService.save(token);
            CacheManager.refreshFactoryToken();
            return returnSuccessInfo("ok");
        }catch (Exception e){
            return returnFailInfo("error");
        }
    }

    @RequestMapping("/findCategory")
    @ResponseBody
    public void findCategory() throws Exception{
        this.logAllRequestParams();
        List<FactoryCategory> list = this.factoryTokenService.findAllCategory();
        this.returnSuccess("ok",list);
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

    @RequestMapping("/checkName")
    @ResponseBody
    public Map<String,Boolean> checkName(String name,String oldName){
        Map<String,Boolean> json = new HashMap<>();
        if (CommonUtil.isBlank(oldName)){
            Token token=this.factoryTokenService.findByName(name.trim());
            if(CommonUtil.isNotBlank(token)){
                json.put("valid",false);
            }else{
                json.put("valid",true);
            }
        }else{
            if (name.trim().equals(oldName)){
                json.put("valid",true);
            }else{
                Token token=this.factoryTokenService.findByName(name.trim());
                if(CommonUtil.isNotBlank(token)){
                    json.put("valid",false);
                }else{
                    json.put("valid",true);
                }
            }
        }

        return json;
    }
}
