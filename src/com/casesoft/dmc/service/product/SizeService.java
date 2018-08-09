package com.casesoft.dmc.service.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.extend.echarts.code.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.SizeDao;
import com.casesoft.dmc.dao.product.SizeSortDao;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.SizeSort;

@Service
@Transactional
public class SizeService implements IBaseService<Size, Serializable> {

    @Autowired
    private SizeDao sizeDao;

    @Autowired
    private SizeSortDao sizesortDao;

    public List<SizeSort> findSizeSort (){
        return this.sizesortDao.find("from SizeSort where isUse='Y' order by seqNo asc");
    }

    @Transactional(readOnly = true)
    public Page<SizeSort> find(Page<SizeSort> page, List<PropertyFilter> filters) {
        if (CommonUtil.isBlank(filters)) {
            return this.sizesortDao.findPage(page, "from SizeSort s");
        }
        return this.sizesortDao.findPage(page, filters);
    }


    @Transactional(readOnly = true)
    public List<Size> getSizeBySortId(String sortId) {
        String hql = "from  Size s where s.sortId=? order by seqNo";

        return this.sizesortDao.find(hql, new Object[]{sortId});
    }

    @Transactional(readOnly = true)
    public Size findSizeById(String id) {
        String hql = "from Size s where s.id=?";
        return this.sizeDao.findUnique(hql, new Object[]{id});
    }

    public SizeSort findSortBySortNo(String sortNo) {
        String hql = "from SizeSort s where s.sortNo=?";
        return this.sizesortDao.findUnique(hql, new Object[]{sortNo});
    }

    public Size findSizeBySizeId(String sizeId) {
        String hql = "from Size s where s.sizeId=?";
        return this.sizesortDao.findUnique(hql, new Object[]{sizeId});
    }

    public Integer findMaxSeqNoInSizeSortBySortNo() {
        String hql = "select max(s.seqNo) from SizeSort s";
        Integer maxSeqNo = this.sizesortDao.findUnique(hql);
        return maxSeqNo == null? 0 :maxSeqNo;
    }

    public Integer findMaxSeqNoInSizeBySortId(String sortId) {
        String hql = "select max(s.seqNo) from Size s where s.sortId=?";
        Integer maxSeqNo = this.sizesortDao.findUnique(hql, new Object[]{sortId});
        return maxSeqNo;
    }

    @Transactional(readOnly = true)
    public List<SizeSort> findSort(List<PropertyFilter> filters) {
        return this.sizesortDao.find(filters);
    }

    @Transactional(readOnly = true)
    public SizeSort findSizeSortById(String id) {
        String hql = "from SizeSort s where s.id=?";
        return this.sizesortDao.findUnique(hql, new Object[]{id});
    }

    public List<SizeSort> getAllSort(){
        return this.sizesortDao.getAll();
    }
    @Override
    public void save(Size size) {
        this.sizeDao.saveOrUpdate(size);
    }

    public void saveSort(SizeSort sizeSort) {
        this.sizesortDao.saveOrUpdate(sizeSort);
    }

    @Override
    public Size load(Serializable id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Size get(String propertyName, Object value) {
        // TODO Auto-generated method stub
        return this.sizeDao.findUniqueBy(propertyName,value);
    }

    public SizeSort getsizeSort(String id) {
        return this.sizesortDao.load(id);
    }

    @Override
    public List<Size> find(List<PropertyFilter> filters) {
        return this.sizeDao.find(filters);
    }

    public List<Size> find(List<PropertyFilter> filters, Map<String, String> sortMap) {
        return this.sizeDao.find(filters, sortMap);
    }

    @Override
    public List<Size> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Size entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Size entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Serializable id) {
        // TODO Auto-generated method stub

    }


    @Override
    public Page<Size> findPage(Page<Size> page, List<PropertyFilter> filters) {
        return this.sizeDao.findPage(page,filters);
    }

}
