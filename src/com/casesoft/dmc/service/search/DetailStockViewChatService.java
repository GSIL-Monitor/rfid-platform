package com.casesoft.dmc.service.search;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.search.DetailStockChatViewDao;
import com.casesoft.dmc.dao.search.DetailStockViewDao;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.search.DetailStockCodeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */
@Service
@Transactional
public class DetailStockViewChatService implements IBaseService<DetailStockChatView, String> {
    @Autowired
    private DetailStockViewDao detailStockViewDao;
    @Autowired
    private DetailStockChatViewDao detailStockChatViewDao;

    @Override
    public Page<DetailStockChatView> findPage(Page<DetailStockChatView> page, List<PropertyFilter> filters) {
        // 原始方法
        return this.detailStockChatViewDao.findPage(page, filters);

    }

    public Page<DetailStockChatView> findPageList(Page<DetailStockChatView> page, List<PropertyFilter> filters) {
        String groupIdFilter = "";
        String styleFilter = "";
        Boolean isWarehIdNull = true;
        for (int i = 0; i < filters.size(); i++) {
            PropertyFilter propertyFilter = filters.get(i);
            String propertyName = propertyFilter.getPropertyNames()[0];
            PropertyFilter.MatchType matchType = propertyFilter.getMatchType();
            String name = matchType.name();
            if (propertyName.equals("groupId")) {
                groupIdFilter = propertyFilter.getMatchValue().toString();
            } else if (propertyName.equals("styleId")||propertyName.equals("styleName")) {
                styleFilter = propertyFilter.getMatchValue().toString();
            } else if (propertyName.equals("warehId") && propertyFilter.getMatchValue().toString() != null) {
                isWarehIdNull = false;
            }
        }
        if (isWarehIdNull) {
            String sortName = page.getOrderBy();
            String order = page.getOrder();
            String hql = "select new com.casesoft.dmc.model.search.DetailStockChatView" +
                    "(styleId,styleName,sum(qty) as qty,price,precast,puprice,wsprice,class1) " +
                    "from DetailStockChatView ";
            if (groupIdFilter == "") {
                hql += "where (groupId like '%" + groupIdFilter + "%' or groupId is null) ";
            } else {
                hql += "where groupId = '" + groupIdFilter + "' ";
            }
            hql += "and (styleId like '%" + styleFilter + "%' or styleName like '%" + styleFilter + "%') " +
                    "group by class1,precast,price,puprice,styleId,styleName,wsprice " +
                    "order by " + sortName + " " + order;
            page.setAutoCount(false);
            return this.detailStockChatViewDao.findPage(page, hql, new Object[]{});
        } else {
            // 原始方法
            return this.detailStockChatViewDao.findPage(page, filters);
        }
    }

    @Override
    public void save(DetailStockChatView entity) {

    }

    @Override
    public DetailStockChatView load(String id) {
        return null;
    }

    @Override
    public DetailStockChatView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<DetailStockChatView> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<DetailStockChatView> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(DetailStockChatView entity) {

    }

    @Override
    public void delete(DetailStockChatView entity) {

    }

    @Override
    public void delete(String id) {

    }

    public Long findstockNum(String warehId, String styleId) {
        String hql = "select sum(qty) from DetailStockChatView where 1=1";
        if (CommonUtil.isNotBlank(warehId)) {
            hql += " and warehId like '" + warehId + "'";
        }
        if (CommonUtil.isNotBlank(styleId)) {
            hql += " and (styleid = '" + styleId + "')";
        }
        Long unique = this.detailStockViewDao.findUnique(hql);
        return unique;
    }

    public Long findwarehNum(String warehId, String styleId, String groupId,String class3) {
        String hql = "select sum(qty) from DetailStockChatView where 1=1";
        if (CommonUtil.isNotBlank(warehId)) {
            hql += " and warehId like '" + warehId + "'";
        }
        if (CommonUtil.isNotBlank(styleId)) {
            hql += " and (styleid like '%" + styleId + "%' or styleName like '%" + styleId + "%')";
        }
        if (CommonUtil.isNotBlank(groupId)) {
            hql += " and groupId = '" + groupId + "'";
        }
        if (CommonUtil.isNotBlank(class3)) {
            hql += " and class3 = '" + class3 + "'";
        }
        Long unique = this.detailStockViewDao.findUnique(hql);
        return unique;
    }

    public Double findwarehMun(String warehId, String styleId, String groupId,String class3) {
        String hql = "select sum(qty*precast) from DetailStockChatView where 1=1";
        if (CommonUtil.isNotBlank(warehId)) {
            hql += " and warehId like '" + warehId + "'";
        }
        if (CommonUtil.isNotBlank(styleId)) {
            hql += " and (styleid like '%" + styleId + "%' or styleName like '%" + styleId + "%')";
        }
        if (CommonUtil.isNotBlank(groupId)) {
            hql += " and groupId = '" + groupId + "'";
        }
        if (CommonUtil.isNotBlank(class3)) {
            hql += " and class3 = '" + class3 + "'";
        }
        Double unique = this.detailStockViewDao.findUnique(hql);
        return unique;
    }

    public List<DetailStockCodeView> findDetial(String warehId, String styleId) {
        String hql = "from DetailStockCodeView where warehId=? and styleId=?";
        List<DetailStockCodeView> list = this.detailStockChatViewDao.find(hql, new Object[]{warehId, styleId});
        return list;
    }
}
