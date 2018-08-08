package com.casesoft.dmc.service.cfg;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.cfg.MultiLevelRelationDao;
import com.casesoft.dmc.model.cfg.MultiLevelRelation;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yushen on 2018/5/17.
 */
@Service
@Transactional
public class MultiLevelRelationService extends AbstractBaseService<MultiLevelRelation, String> {

    @Autowired
    private MultiLevelRelationDao multiLevelRelationDao;

    @Override
    public Page<MultiLevelRelation> findPage(Page<MultiLevelRelation> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(MultiLevelRelation entity) {
        this.multiLevelRelationDao.save(entity);
    }

    public void save(Unit company, String MultiLevelType) {
        try {
            MultiLevelRelation multiLevelRelation = this.get("relatedId", company.getId());
            if (CommonUtil.isBlank(multiLevelRelation)) {
                multiLevelRelation = new MultiLevelRelation();
                multiLevelRelation.setId(company.getId());
                multiLevelRelation.setRelatedId(company.getId());
                multiLevelRelation.setMultiLevelType(MultiLevelType);
                multiLevelRelation.setRoot(false);
                multiLevelRelation.setArchive(false);
                multiLevelRelation.setOpenedState(true);
                Integer maxSeqNo = this.getMaxSeqNo(company.getOwnerId());
                multiLevelRelation.setTreeSeqNo(maxSeqNo);
            }
            multiLevelRelation.setName(company.getName());
            MultiLevelRelation parent;
            if(multiLevelRelation.getRoot()){
                parent = null;
            }else {
                parent = this.get("id", company.getOwnerId());
            }
            if (CommonUtil.isBlank(parent)) {
                multiLevelRelation.setParentId(null);
                multiLevelRelation.setTreePath(company.getId() + ">");
                multiLevelRelation.setDepth(0);
                this.multiLevelRelationDao.saveOrUpdate(multiLevelRelation);
            }else {
                multiLevelRelation.setParentId(company.getOwnerId());
                multiLevelRelation.setTreePath(parent.getTreePath() + company.getId() + ">");
                multiLevelRelation.setDepth(parent.getDepth() + 1);
                this.multiLevelRelationDao.saveOrUpdate(multiLevelRelation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MultiLevelRelation load(String id) {
        return null;
    }

    @Override
    public MultiLevelRelation get(String propertyName, Object value) {

        return this.multiLevelRelationDao.findUniqueBy(propertyName, value);
    }

    @Override
    public List<MultiLevelRelation> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MultiLevelRelation> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MultiLevelRelation entity) {

    }

    @Override
    public void delete(MultiLevelRelation entity) {

    }

    @Override
    public void delete(String id) {

    }

    private Integer getMaxSeqNo(String parentId) {
        Integer code = this.multiLevelRelationDao.findUnique("select max(treeSeqNo) from MultiLevelRelation where parentId = ?", parentId);
        if (code == null) {
            code = 0;
        } else {
            code = code + 1;
        }
        return code;
    }

    public List<MultiLevelRelation> listByType(String multiLevelType) {
        return this.multiLevelRelationDao.find("from MultiLevelRelation where multiLevelType = ? order by treeSeqNo", multiLevelType);
    }

    public List<MultiLevelRelation> listByParentId(String parentId) {
        return this.multiLevelRelationDao.find("from MultiLevelRelation where parentId = ? ", parentId);
    }

    public void move(String id, String parentId, String position, String sourceParentId, String sourcePosition) {
        if ((parentId + "").equals((sourceParentId + ""))) { //同一个节点内变动
            if (new Integer(position) < new Integer(sourcePosition)) {
                //节点内向上移动，大于当前position，小于原始sourcePosition的节点，treeSeqNo + 1
                this.multiLevelRelationDao.batchExecute("update MultiLevelRelation set treeSeqNo = treeSeqNo + 1 where treeSeqNo >= ? and treeSeqNo <= ? and parentId = ?",
                        new Integer(position), new Integer(sourcePosition), parentId);
            } else {
                //节点内向上移动，大于原始sourcePosition，小于当前position的节点，treeSeqNo - 1
                this.multiLevelRelationDao.batchExecute("update MultiLevelRelation set treeSeqNo = treeSeqNo - 1 where treeSeqNo >= ? and treeSeqNo <= ? and parentId = ?",
                        new Integer(sourcePosition), new Integer(position), parentId);
            }
        } else {//跨节点变动
            //原始节点内大于原始sourcePosition的节点， treeSeqNo - 1
            this.multiLevelRelationDao.batchExecute("update MultiLevelRelation set treeSeqNo = treeSeqNo - 1 where treeSeqNo >= ? and parentId = ?",
                    new Integer(sourcePosition), sourceParentId);
            //新节点内大于position的节点， treeSeqNo + 1
            this.multiLevelRelationDao.batchExecute("update MultiLevelRelation set treeSeqNo = treeSeqNo + 1 where treeSeqNo >= ? and parentId = ?",
                    new Integer(position), parentId);
        }
        //更新当前节点
        MultiLevelRelation multiLevelRelation = this.get("relatedId", id);
        multiLevelRelation.setParentId(parentId);
        multiLevelRelation.setTreeSeqNo(new Integer(position));
        this.multiLevelRelationDao.saveOrUpdate(multiLevelRelation);

        this.updateTreePath(multiLevelRelation);
    }

    //更新当前节点以及其所有子节点的treePath
    private void updateTreePath(MultiLevelRelation relation) {
        MultiLevelRelation parent = this.get("relatedId", relation.getParentId());
        String parentTreePath = parent.getTreePath();
        relation.setTreePath(parentTreePath + relation.getId() + ">");
        relation.setDepth(parent.getDepth() + 1);
        this.multiLevelRelationDao.saveOrUpdate(relation);
        List<MultiLevelRelation> childList = this.listByParentId(relation.getId());
        for (MultiLevelRelation child : childList) {
            updateTreePath(child);
        }
    }
}
