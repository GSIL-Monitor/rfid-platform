package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.shop.VipCardDao;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.VipCard;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员卡管理
 * Created by luis on 2018/6/20.
 */
@Service
@Transactional
public class VipCardService extends AbstractBaseService<VipCard, String> {

    @Autowired
    private VipCardDao vipCardDao;
    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CustomerService customerService;

    @Override
    public Page<VipCard> findPage(Page<VipCard> page, List<PropertyFilter> filters) {
        return this.vipCardDao.findPage(page, filters);
    }

    @Override
    public void save(VipCard entity) {
        this.vipCardDao.saveOrUpdate(entity);
    }

    @Override
    public VipCard load(String id) {
        return this.vipCardDao.load(id);
    }

    @Override
    public VipCard get(String propertyName, Object value) {
        return this.vipCardDao.findUniqueBy(propertyName,value);
    }

    @Override
    public List<VipCard> find(List<PropertyFilter> filters) {
        return this.vipCardDao.find(filters);
    }

    @Override
    public List<VipCard> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(VipCard entity) {

    }
    //查询升级总金额
    public Double upgradeConsumeNo(String id){
        return this.vipCardDao.findUnique("select upgradeConsumeNo from VipCard where id = ?",id);
    }

    @Override
    public void delete(VipCard entity) {
        this.vipCardDao.delete(entity);
    }

    @Override
    public void delete(String id) {
        this.vipCardDao.delete(id);
    }

    public void membershipUpgrade() {
        String VipCardIdMax = this.vipCardDao.findUnique("select max(id) from VipCard");
        //unit表
        List<Unit> units = this.unitService.getAll();
        for (Unit unit : units){
            if (unit.getIdCard()!=null&&!unit.getIdCard().equals("")&&Integer.parseInt(unit.getIdCard())<=Integer.parseInt(VipCardIdMax)){
                Double actPriceSum = this.saleOrderBillService.findSumActPrice(unit.getId());
                if (actPriceSum!=0){
                    VipCard vipCard =get("id",unit.getIdCard());
                    Double upgradeConsumeNo = vipCard.getUpgradeConsumeNo();
                    if (actPriceSum>=upgradeConsumeNo){
                        unit.setIdCard(Integer.parseInt(unit.getIdCard())+1+"");
                        if (unit.getDiscount()>=vipCard.getDiscount()) {
                            unit.setDiscount(vipCard.getDiscount());
                        }
                        this.unitService.saveOrUpdate(unit);
                    }
                }
            }
        }
        //customer表
        List<Customer> customerList = this.customerService.getAll();
        for (Customer customer : customerList){
            if (customer.getIdCard()!=null&&!customer.getIdCard().equals("")&&Integer.parseInt(customer.getIdCard())<=Integer.parseInt(VipCardIdMax)){
                Double actPriceSum = this.saleOrderBillService.findSumActPrice(customer.getId());
                if (actPriceSum != 0){
                    VipCard vipCard =get("id",customer.getIdCard());
                    Double upgradeConsumeNo = vipCard.getUpgradeConsumeNo();
                    if (actPriceSum>=upgradeConsumeNo){
                        customer.setIdCard(Integer.parseInt(customer.getIdCard())+1+"");
                        if (customer.getDiscount()>=vipCard.getDiscount()){
                            customer.setDiscount(vipCard.getDiscount());
                        }
                        this.customerService.save(customer);
                    }
                }
            }
        }
    }
}
