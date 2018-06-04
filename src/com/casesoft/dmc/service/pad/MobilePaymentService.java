package com.casesoft.dmc.service.pad;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.pad.MobilePaymentDao;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.model.pad.MobilePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liutinaci 2018/5/30.
 */
@Service
@Transactional
public class MobilePaymentService extends BaseService<MobilePayment, String> {
    @Autowired
    private MobilePaymentDao mobilePaymentDao;
    @Override
    public Page<MobilePayment> findPage(Page<MobilePayment> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(MobilePayment entity) {
        this.mobilePaymentDao.saveOrUpdate(entity);
    }

    @Override
    public MobilePayment load(String id) {
        return null;
    }

    @Override
    public MobilePayment get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<MobilePayment> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<MobilePayment> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(MobilePayment entity) {

    }

    @Override
    public void delete(MobilePayment entity) {

    }

    @Override
    public void delete(String id) {

    }
}
