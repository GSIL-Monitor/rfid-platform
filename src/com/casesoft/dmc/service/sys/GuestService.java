package com.casesoft.dmc.service.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.MonthAccountStatementDao;
import com.casesoft.dmc.dao.logistics.SaleOrderBillDao;
import com.casesoft.dmc.dao.sys.GuestDao;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.PointsChange;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.shop.PointsChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@Autowired
	private PointsChangeService pointsChangeService;

	private Logger logger = LoggerFactory.getLogger(SaleOrderBill.class);

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

	public void updateUnit(Unit guest,Customer preCustomer) {
		this.guestDao.saveOrUpdateX(preCustomer);
		this.guestDao.saveOrUpdate(guest);
	}

	public void updateCustomer(Customer guest,Unit preUnit) {
		this.guestDao.saveOrUpdate(preUnit);
		this.guestDao.saveOrUpdateX(guest);
	}

	//add by yushen 重置之前客户的欠款金额和积分
	public void resetPreGust(String billNo, String preUnitId, Double preDiffPrice){
		if (CommonUtil.isNotBlank(preUnitId)) {
			// TODO: 18-6-21 for yushen 验证缓存中取出数据的准确性
			Unit preUnit = this.guestDao.findUnique("from Unit where id = ? and status=1", preUnitId);
			Customer preCustomer = this.guestDao.findUnique("from Customer where id = ? and status=1", preUnitId);
			if (CommonUtil.isNotBlank(preUnit)) {
				preUnit.setOwingValue(preUnit.getOwingValue() - preDiffPrice);
				logger.info("Unit原来客户"+preUnit.getName()+"Unit原来客户编号"+preUnit.getId()+"原单差额"+preDiffPrice);
				if(CommonUtil.isNotBlank(preUnit.getVipId())){
					//开通会员则重置之前的积分
					if(CommonUtil.isNotBlank(preUnit.getVippoints())){
						//如果当前已有积分则根据单号查积分变动记录，减去对应的积分
						Long points = pointsChangeService.getPointsById(billNo);
						if(CommonUtil.isNotBlank(points)){
							logger.info("Unit原来客户"+preUnit.getName()+"Unit原来客户编号"+preUnit.getId()+"原单积分"+points);
							preUnit.setVippoints(preUnit.getVippoints() - points);
						}
					}else {
						//如果当前积分字段为空则初始化积分为0
						preUnit.setVippoints(0D);
					}
				}
				this.guestDao.saveOrUpdateX(preUnit);
			} else {
				preCustomer.setOwingValue(preCustomer.getOwingValue() - preDiffPrice);
				logger.info("Unit原来客户"+preCustomer.getName()+"Unit原来客户编号"+preCustomer.getId()+"原单差额"+preDiffPrice);
				if(CommonUtil.isNotBlank(preCustomer.getVipId())){
					//开通会员则重置之前的积分
					if(CommonUtil.isNotBlank(preCustomer.getVippoints())){
						//如果当前已有积分则根据单号查积分变动记录，减去对应的积分
						Long points = pointsChangeService.getPointsById(billNo);
						if(CommonUtil.isNotBlank(points)){
							logger.info("Customer原来客户"+preCustomer.getName()+"Customer原来客户编号"+preCustomer.getId()+"原单积分"+points);
							preCustomer.setVippoints(preCustomer.getVippoints() - points);
						}
					}else {
						//如果当前积分字段为空则初始化积分为0
						preCustomer.setVippoints(0D);
					}
				}
				this.guestDao.saveOrUpdateX(preCustomer);
			}
		}
	}

	//add by yushen 更新客户的欠款金额和积分
	public void updateCurrentGuest(String billNo, Double diffPrice, String unitId, Long points){
		Unit unit = this.guestDao.findUnique("from Unit where id = ? and status=1", unitId);
		Customer customer = this.guestDao.findUnique("from Customer where id = ? and status=1", unitId);

		if (CommonUtil.isNotBlank(unit)) {
			if(CommonUtil.isBlank(unit.getOwingValue())){
				unit.setOwingValue(0.0);
			}
			logger.info("销售单"+billNo+"本单差额"+diffPrice+"unit客户"+unit.getName()+"unit客户编号"+unit.getId()+"欠款金额"+(unit.getOwingValue()+diffPrice)+"原欠款金额"+(unit.getOwingValue()));
			unit.setOwingValue(unit.getOwingValue() + diffPrice);
			if(CommonUtil.isNotBlank(unit.getVipId())){
				if(CommonUtil.isBlank(unit.getVippoints())){
					unit.setVippoints(0D);
				}
			}
			logger.info("销售单"+billNo+"本单新增积分"+points+"unit客户"+"原欠积分"+(unit.getOwingValue()));
			unit.setVippoints(unit.getVippoints() + points);
			this.guestDao.saveOrUpdateX(unit);
		} else {
			if(CommonUtil.isBlank(customer.getOwingValue())){
				customer.setOwingValue(0.0);
			}
			logger.info("销售单"+billNo+"本单差额"+diffPrice+"customer客户"+customer.getName()+"customer客户编号"+customer.getId()+"欠款金额"+(customer.getOwingValue()+diffPrice)+"原欠款金额"+(customer.getOwingValue()));
			customer.setOwingValue(customer.getOwingValue() + diffPrice);
			if(CommonUtil.isNotBlank(customer.getVipId())){
				if(CommonUtil.isBlank(customer.getVippoints())){
					customer.setVippoints(0D);
				}
			}
			logger.info("销售单"+billNo+"本单新增积分"+points+"customer客户"+"原欠积分"+(customer.getOwingValue()));
			customer.setVippoints(customer.getVippoints() + points);
			this.guestDao.saveOrUpdateX(customer);
		}
	}
}
