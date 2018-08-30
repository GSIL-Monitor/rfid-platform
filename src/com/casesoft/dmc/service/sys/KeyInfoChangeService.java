package com.casesoft.dmc.service.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.KeyInfoChangeDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.sys.KeyInfoChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional

public class KeyInfoChangeService extends BaseService<KeyInfoChange, String> {
    @Autowired
    private KeyInfoChangeDao keyInfoChangeDao;

    @Override
    public Page<KeyInfoChange> findPage(Page<KeyInfoChange> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(KeyInfoChange entity) {
        keyInfoChangeDao.save(entity);
    }

    @Override
    public KeyInfoChange load(String id) {
        return null;
    }

    @Override
    public KeyInfoChange get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<KeyInfoChange> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<KeyInfoChange> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(KeyInfoChange entity) {

    }

    @Override
    public void delete(KeyInfoChange entity) {

    }

    @Override
    public void delete(String id) {

    }

    /**
     *
     * @return 返回分析的结果
     */
    public String commonSave(String userId, String method, String objectId,  Map<String, Object> preInfoMap, Map<String, Object> aftInfoMap){
        boolean valueChange = false;
        StringBuilder originalSb = new StringBuilder();
        StringBuilder currentSb = new StringBuilder();
        originalSb.append("原价：");
        currentSb.append("现价：");
        for (Map.Entry<String, Object> entry : preInfoMap.entrySet()) {
            String key = entry.getKey();
            Object preValue = entry.getValue();
            if((Double) aftInfoMap.get(key) != ((Double) preValue).doubleValue()){
                valueChange = true;
                originalSb.append(getPriceName(key)).append(":").append(preValue).append(",");
                currentSb.append(getPriceName(key)).append(":").append(aftInfoMap.get(key)).append(",");
            }
        }

        if(valueChange){
            originalSb.deleteCharAt(originalSb.length()-1);
            currentSb.deleteCharAt(currentSb.length()-1);
            String remark = originalSb.append("\r\n").append(currentSb).append("\r\n").append("操作人：").append(userId).toString();

            KeyInfoChange infoChange = new KeyInfoChange();
            infoChange.setOprId(userId);
            infoChange.setCreatedDate(new Date());
            infoChange.setMethod(method);
            infoChange.setObjectId(objectId);
            infoChange.setPreInfo(JSON.toJSONString(preInfoMap));
            infoChange.setAftInfo(JSON.toJSONString(aftInfoMap));
            infoChange.setRemarks(remark);
            this.save(infoChange);
            return infoChange.getRemarks();
        }
        return "";
    }

    private String getPriceName(String key){
        HashMap<String, String> priceNameMap = new HashMap<>();
        priceNameMap.put("price","吊牌价");
        priceNameMap.put("puPrice","代理价");
        priceNameMap.put("wsPrice","门店价");
        priceNameMap.put("preCast","采购价");

        return priceNameMap.get(key);
    }
}
