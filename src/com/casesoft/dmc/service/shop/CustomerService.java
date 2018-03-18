package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.shop.CustomerDao;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.shop.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService extends AbstractBaseService<Customer, String> {

  @Autowired
  private CustomerDao customerDao;
  @Autowired
  private MonthAccountStatementDao monthAccountStatementDao;

  @Override
  @Transactional(readOnly = true)
  public Page<Customer> findPage(Page<Customer> page, List<PropertyFilter> filters) {
    return this.customerDao.findPage(page, filters);
  }
    public void saveList(List<Customer> customerList) {
        this.customerDao.doBatchInsert(customerList);
    }
  @Override
  public void save(Customer entity) {
    this.customerDao.saveOrUpdate(entity);
  }

  public void saveCustomer(Customer entity) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MONTH, -1);
    Date date = cal.getTime();

    String dateNow = CommonUtil.getDateString(new Date(), "yyyy-MM-dd");
    String month = CommonUtil.getDateString(date, "yyyy-MM");
    MonthAccountStatement monthAccountStatement = new MonthAccountStatement();
    monthAccountStatement.setId(entity.getCode() + "-" + month);
    monthAccountStatement.setBillDate(CommonUtil.converStrToDate(dateNow, "yyyy-MM-dd"));
    monthAccountStatement.setMonth(month);
    monthAccountStatement.setBillType("付款");
    monthAccountStatement.setUnitId(entity.getCode());
    monthAccountStatement.setUnitType("供应商");
    monthAccountStatement.setOwnerId(entity.getOwnerId());
    monthAccountStatement.setTotVal(0D);
    this.customerDao.saveOrUpdate(entity);
    this.monthAccountStatementDao.saveOrUpdate(monthAccountStatement);
  }

  @Override
  @Transactional(readOnly = true)
  public Customer load(String id) {
    return this.customerDao.load(id);
  }
  @Transactional(readOnly = true)
  public Customer getCustomerById(String id) {
    return this.customerDao.findUnique("from Customer c where c.id=?",new Object[]{id});
  }

  @Override
  @Transactional(readOnly = true)
  public Customer get(String propertyName, Object value) {
    return this.customerDao.findUniqueBy(propertyName, value);
  }

  @Override
  public List<Customer> find(List<PropertyFilter> filters) {
    return this.customerDao.find(filters);
  }
  
  public String getMaxNo(String prefix) throws Exception {

	  String hql = "select max(CAST(SUBSTRING(c.code," + (prefix.length() + 1) + "),integer))"
		        + " from Customer as c where c.code like ?";
		    Integer code = this.customerDao.findUnique(hql, prefix + "%");
		    return code == null ? (prefix + "0001") : prefix + CommonUtil.convertIntToString(code + 1, 4);
  }

  public boolean checkIsRegisted(Customer customer){
	  String hql="from Customer c where c.ownerId=? and c.phone=?";
	  List<Customer> list=this.customerDao.find(hql,new Object[]{customer.getOwnerId(),customer.getPhone()});
    return CommonUtil.isBlank(list);
  }

  public Customer getById(String id){
    String hql ="from Customer c where c.id =?";
    return this.customerDao.findUnique(hql,new Object[]{id});
  }

  @Override
  public List<Customer> getAll() {
    return this.customerDao.getAll();
  }

  @Override
  public <X> List<X> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(Customer entity) {
    this.customerDao.update(entity);
  }

  @Override
  public void delete(Customer entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(String id) {
    // TODO Auto-generated method stub

  }

  public CustomerDao getCustomerDao() {
    return customerDao;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }


}
