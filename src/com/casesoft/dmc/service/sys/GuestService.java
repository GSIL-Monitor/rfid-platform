package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.sys.GuestDao;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-06-20.
 */
@Service
@Transactional
public class GuestService implements IBaseService<Unit,String>{

	@Autowired
	private GuestDao guestDao;

	@Autowired
	private MonthAccountStatementDao monthAccountStatementDao;

	@Override
	public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
		return this.guestDao.findPage(page,filters);
	}

	public Unit getGuestById(String id){
		return  this.guestDao.findUnique("from Unit g where g.id=?",new Object[]{id});
	}

	public String findMaxNo(String GuestPrefix){
		String flag=GuestPrefix;
		String hql="select max(CAST(SUBSTRING(guest.code,"+(flag.length()+1)+"),integer)) from Unit guest where guest.code like '"+flag+"%'";
		Integer id=this.guestDao.findUnique(hql,new Object[]{});
		return id==null?flag+"001":flag+ CommonUtil.convertIntToString(id+1,3);
	}

	@Override
	public void save(Unit entity) {
		this.guestDao.saveOrUpdate(entity);

	}

	public void saveUnit(Unit entity) throws ParseException {
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
		this.guestDao.saveOrUpdate(entity);
		this.monthAccountStatementDao.saveOrUpdate(monthAccountStatement);
	}

	@Override
	public Unit load(String id) {
		return this.guestDao.load(id);
	}

	@Override
	public Unit get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Unit> find(List<PropertyFilter> filters) {
		return this.guestDao.find(filters);
	}

	public Unit findById(String id){
		String hql ="from Unit g where g.id=?";
		return this.guestDao.findUnique(hql,new Object[]{id});
	}

	@Override
	public List<Unit> getAll() {
		return this.guestDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(Unit entity) {
		this.guestDao.update(entity);
	}

	@Override
	public void delete(Unit entity) {

	}

	@Override
	public void delete(String id) {

	}

	public void updateUnit(Unit guest) {
		this.guestDao.batchExecute("delete Customer where id = ?",guest.getId());
		this.guestDao.saveOrUpdate(guest);
	}

	public void updateCustomer(Customer guest) {
		this.guestDao.batchExecute("delete Unit where id = ?",guest.getId());
		this.guestDao.saveOrUpdateX(guest);
	}
}
