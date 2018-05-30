package com.casesoft.dmc.service.sys.impl;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.UnitDao;
import com.casesoft.dmc.model.cfg.MultiLevelRelation;
import com.casesoft.dmc.model.cfg.VO.State;
import com.casesoft.dmc.model.cfg.VO.TreeVO;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.VO.UnitTreeVO;
import com.casesoft.dmc.service.cfg.MultiLevelRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yushen on 2018/5/18.
 */
@Service
@Transactional
public class OrganizationService implements IBaseService<Unit, String> {
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private MultiLevelRelationService multiLevelRelationService;

    @Override
    public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
        this.unitService.findPage(page, filters);

        for (Unit unit : page.getRows()) {
            Unit owner = CacheManager.getUnitByCode(unit.getOwnerId());
            if (CommonUtil.isNotBlank(owner)) {
                unit.setUnitName(owner.getName());
            }
        }
        return page;
    }

    @Override
    public void save(Unit entity) {
        this.unitService.saveOrUpdate(entity);

        //保存公司层级关系
        this.multiLevelRelationService.save(entity, Constant.MultiLevelType.Company);
    }

    @Override
    public Unit load(String id) {
        return null;
    }

    @Override
    public Unit get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Unit> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Unit> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Unit entity) {

    }

    @Override
    public void delete(Unit entity) {

    }

    @Override
    public void delete(String id) {

    }

    //add by yushen 获取组织信息的树形结构list
    public List<TreeVO> listOrganizationTree() {
        List<MultiLevelRelation> relationList = this.multiLevelRelationService.listByType(Constant.MultiLevelType.Company);
        return treeRelationList(relationList, null);
    }

    private List<TreeVO> treeRelationList(List<MultiLevelRelation> relationList, String parentId) {
        List<TreeVO> result = new ArrayList<>();
        for (MultiLevelRelation relation : relationList) {
            String pid = relation.getParentId();
            if ((parentId == null && pid == null) || (parentId != null && parentId.equals(pid))) {
                TreeVO treeVO = new TreeVO();
                treeVO.setId(relation.getId());
                treeVO.setText(relation.getName());
                if (relation.getOpenedState()) {
                    treeVO.setState(new State(true));
                }
                List<TreeVO> childrenTreeVO = treeRelationList(relationList, relation.getId());
                treeVO.setChildren(childrenTreeVO);
                result.add(treeVO);
            }
        }
        return result;
    }

    public void move(String id, String parentId, String position, String sourceParentId, String sourcePosition) {
        Unit organization = this.unitService.getunitbyId(id);
        organization.setOwnerId(parentId);
        this.unitService.saveOrUpdate(organization);

        this.multiLevelRelationService.move(id, parentId, position, sourceParentId, sourcePosition);

    }
}
