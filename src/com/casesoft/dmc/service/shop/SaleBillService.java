package com.casesoft.dmc.service.shop;

import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.dao.shop.SaleBillDao;
import com.casesoft.dmc.model.shop.*;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Wing Li on 2014/6/21.
 */
@Service
@Transactional
public class SaleBillService extends AbstractBaseService<SaleBill, String> {

	@Autowired
	private SaleBillDao saleBillDao;

	@Autowired
	private IBillWSService billWSService;


	public List<SaleBillDtl> findSaleBillDtlListByUniqueCode(String uniqueCode) {
		return this.saleBillDao.find("from SaleBillDtl dtl where dtl.uniqueCode=?",new Object[]{uniqueCode});
	}

	@Override
	public Page<SaleBill> findPage(Page<SaleBill> page,
			List<PropertyFilter> filters) {
		return this.saleBillDao.findPage(page, filters);
	}

	public void save(SaleBill saleBill, Customer customer, boolean autoUpload, String codes) {
		boolean success = true;
		if (autoUpload) {
			MessageBox msg = billWSService.uploadPosToERP(saleBill);
            success = msg.getSuccess();
		}
		if (success) {
			save(saleBill, customer);
            if(TagFactory.getCurrentTag().isUniqueCodeStock()) {
                this.updateStock(saleBill.getType(), codes, saleBill.getShopId());
            }
		}
	}
	public void saveBill(SaleBill saleBill, Customer customer,
			boolean autoUpload) {
		boolean success = true;
		if (autoUpload) {
			 MessageBox msg = billWSService.uploadPosToERP(saleBill);
             success = msg.getSuccess();
		}
		if (success) {
			save(saleBill, customer);			
		}
	}
	public void batchBills(List<SaleBill> bills){
		if(CommonUtil.isNotBlank(bills)){
			this.saleBillDao.doBatchInsert(bills);
			for(SaleBill bill:bills){
				this.saleBillDao.doBatchInsert(bill.getDtlList());
				if (CommonUtil.isNotBlank(bill.getRecordList())) {
					this.saleBillDao.doBatchInsert(bill.getRecordList());
					List<String> codes=TaskUtil.getRecordCodes(bill.getRecordList());
					String codeStr=CommonUtil.getSqlStrByList(codes, EpcStock.class, "code");
					int token =Constant.Token.Shop_Sales;
					int inStock=1;
					switch(bill.getType()){
					case Constant.ScmConstant.SaleBillType.Outbound:
						token=Constant.Token.Shop_Sales;
						inStock=0;
						break;
					case Constant.ScmConstant.SaleBillType.Inbound:
						token=Constant.Token.Shop_Sales_refund;
						inStock=1;
						break;
					}
					String hql="update EpcStock epcstock set epcstock.inStock=?,epcstock.token=?"
							+ " where epcstock.warehouseId=? and "
							+ codeStr;
					this.saleBillDao.batchExecute(hql, inStock,token,bill.getShopId());
				}
			}
		}
	}
	public void updateStock(int saleBillType,String codes,String warehouseId){
		int token =Constant.Token.Shop_Sales;
		int inStock=1;
		switch(saleBillType){
		case Constant.ScmConstant.SaleBillType.Outbound:
			token=Constant.Token.Shop_Sales;
			inStock=0;
			break;
		case Constant.ScmConstant.SaleBillType.Inbound:
			token=Constant.Token.Shop_Sales_refund;
			inStock=1;
			break;
		}
		String hql="update EpcStock epcStock set epcStock.inStock=?,epcStock.token=?"
				+ " where epcStock.warehouseId=? and epcStock.code in ("
				+ codes+ ")";
		this.saleBillDao.batchExecute(hql, inStock,token,warehouseId);
		
	}
	// 检查是否能销售
	/*
	* true :已销售
	* */
	public boolean checkIsSaled(String codes) {
		String hql = "select count(*) as qty,uniqueCode from SaleBillDtl dtl "
				+ " where dtl.uniqueCode in(" + codes
				+ ") group by uniqueCode ";
		List<Object[]> list = this.saleBillDao.find(hql, new Object[] {});
		if (CommonUtil.isNotBlank(list)) {
			for (Object[] obj : list) {
				if (Long.parseLong((obj[0]).toString()) % 2 != 0) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	/* public List<SaleBillDtl> findRefunds(String) */
	public void save(SaleBill saleBill, Customer customer) {

		if (CommonUtil.isNotBlank(customer)) {
			this.saleBillDao.saveOrUpdateX(customer);
		}
		save(saleBill);
	}

	public void save(SaleBill saleBill, Customer customer, String codes) {

		if (CommonUtil.isNotBlank(customer)) {
			this.saleBillDao.saveOrUpdateX(customer);
		}
		save(saleBill);
        if(TagFactory.getCurrentTag().isUniqueCodeStock()) {
            this.updateStock(saleBill.getType(), codes, saleBill.getShopId());
        }
	}

	@Override
	public void save(SaleBill entity) {
		this.saleBillDao.save(entity);
		this.saleBillDao.doBatchInsert(entity.getDtlList());
		if (CommonUtil.isNotBlank(entity.getRecordList())) {
			this.saleBillDao.doBatchInsert(entity.getRecordList());
		}

		if (CommonUtil.isNotBlank(entity.getBusiness())) {
			this.saleBillDao.saveOrUpdateX(entity.getBusiness());
			this.saleBillDao.doBatchInsert(entity.getBusiness().getDtlList());
		}
	}

	@Override
	public SaleBill load(String id) {
        return this.saleBillDao.get(id);
	}

	@Override
	public SaleBill get(String propertyName, Object value) {
		return this.saleBillDao.findUniqueBy(propertyName,value);
	}

	@Override
	public List<SaleBill> find(List<PropertyFilter> filters) {
		return null;
	}

	@Override
	public List<SaleBill> getAll() {
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	public <X> List<X> findDtlList(String billId) {
		String hql = "from SaleBillDtl dtl where dtl.billId=?";
		return this.saleBillDao.find(hql, new Object[] { billId });
	}
	public List<Object> findSaledDtlList(String billId) {
		String hql = "select dtl.uniqueCode from SaleBillDtl dtl where dtl.billId=? and dtl.refundBillId is null";
		return this.saleBillDao.find(hql, billId);
	}
	public <X> List<X> findSaledDtlList(String billId, String[] uniqueCodeList) {
		String hql = "from SaleBillDtl dtl where dtl.billId=? and dtl.uniqueCode in("+CommonUtil.addQuotes(uniqueCodeList)+")";
		return this.saleBillDao.find(hql, new Object[] { billId });
	}
	
	// 检查是否退货的唯一码
	public String checkUniqueCodeIsRefund(String codes) {
		String hql = "select count(*) as qty,uniqueCode from SaleBillDtl dtl "
				+ " where dtl.uniqueCode in(" + codes
				+ ") group by uniqueCode ";
		List<Object[]> list = this.saleBillDao.find(hql, new Object[] {});
		StringBuffer refundCodes = new StringBuffer();
		if (CommonUtil.isNotBlank(list)) {
			for (Object[] obj : list) {
				if (Long.parseLong((obj[0]).toString()) % 2 == 1) {
					refundCodes.append(",").append(obj[1]);
				}
			}
			if(CommonUtil.isBlank(refundCodes.toString())) {
				return null;
			} else {
				return refundCodes.substring(1);
			}
		} else {//没零售，不能退货
			return null;
		}
			 
	}

	@Override
	public void update(SaleBill entity) {
		this.saleBillDao.update(entity);
	}

	@Override
	public void delete(SaleBill entity) {

	}

	@Override
	public void delete(String id) {
        this.saleBillDao.batchExecute("delete SaleBill s where s.billNo=?",id);
        this.saleBillDao.batchExecute("delete SaleBillDtl s where s.billId=?",id);
        this.saleBillDao.batchExecute("delete Business b where b.id=?", id);
        this.saleBillDao.batchExecute("delete BusinessDtl b where b.taskId=?", id);
        this.saleBillDao.batchExecute("delete Record b where b.taskId=?", id);
	}

	/**
	 * 获取销售单货退货单最大编号
	 *
	 * @return
	 */
	public String finaSallBillMaxNo(String prefix) {

		String hql = "select max(CAST(SUBSTRING(t.billNo,"
				+ (prefix.length() + 1) + "),integer))"
				+ " from SaleBill as t where t.billNo like ?";
		Integer code = this.saleBillDao.findUnique(hql, new Object[] { prefix
				+ "%" });
		return code == null ? (prefix + "001") : prefix
				+ CommonUtil.convertIntToString(code + 1, 3);
	}

	public SaleBillDtl findDtl(String uniqueCode) {
		String hql = "from SaleBillDtl dtl"
				+ " where dtl.uniqueCode=? and dtl.refundBillId is null"
				+ " and dtl.billDate in (select max(d.billDate) from SaleBillDtl d"
				+ " where d.uniqueCode=? and d.refundBillId is null)";
		SaleBillDtl dtl = this.saleBillDao.findUnique(hql, new Object[] {
				uniqueCode, uniqueCode });
		return dtl;
	}
	/**
	 * 尺码
	 */
	public List<SaleBillSizeAnalysis> findSaleBillDtlSizeTop(String id, int topSize) {
		String hql = "select new com.casesoft.dmc.model.shop.SaleBillSizeAnalysis(d.sizeId,si.sizeName,count(d.sizeId) as sizeCount) from"
				+ " SaleBillDtl d,SaleBill s ,Size si  where s.client2Id=? and d.qty!=0 and d.actPrice > 0 and d.billNo=s.billNo  and d.sizeId = si.sizeId group by d.sizeId,si.sizeName order by sizeCount desc)";
		return this.saleBillDao.createQuery(hql, new Object[]{id}).setMaxResults(topSize).list();
	}

	/**
	 * 颜色
	 */
	public List<SaleBillColorAnalysis> findSaleBillDtlColorTop(String id, int topColor) {
		String hql = "select new com.casesoft.dmc.model.shop.SaleBillColorAnalysis(d.colorId,c.colorName,count(d.colorId) as colorCount) from"
				+ " SaleBillDtl d,SaleBill s ,Color c  where s.client2Id=? and d.actPrice > 0  and d.billNo=s.billNo  and d.colorId = c.colorId  group by d.colorId,c.colorName order by colorCount desc)";
		return this.saleBillDao.createQuery(hql, new Object[]{id}).setMaxResults(topColor).list();
	}

	/**
	 * 小类(C4)
	 */
	public List<StyleSubClassAnalysis> findSaleBillDtlStyleClass4Top(String id, int topSize) {
		String hql = "select new com.casesoft.dmc.model.shop.StyleSubClassAnalysis(" +
				"sty.class4,count(sty.class4))" +
				" from SaleBillDtl d,SaleBill s,"
				+ "com.casesoft.dmc.model.product.Style sty where s.client2Id=? and  d.billNo=s.billNo "
				+ " and d.styleId = sty.id and d.actPrice > 0 and sty.class4 is not null group by sty.class4 order by count(sty.class4) desc";
		return this.saleBillDao.createQuery(hql, new Object[]{id}).setMaxResults(topSize).list();
	}

	/**
	 * 材质(C8）
	 */
	public List<StyleMaterialAnalysis> findSaleBillDtlStyleClass8Top(String id, int topSize) {
		String hql = "select new com.casesoft.dmc.model.shop.StyleMaterialAnalysis(" +
				"sty.class8,count(sty.class8) as class8Count) " +
				"from SaleBillDtl d,SaleBill s,"
				+ "com.casesoft.dmc.model.product.Style sty where s.client2Id=? and  d.billNo=s.billNo "
				+ " and d.styleId = sty.id and d.actPrice > 0 and sty.class8 is not null  group by sty.class8 order by class8Count desc";
		return this.saleBillDao.createQuery(hql, new Object[]{id}).setMaxResults(topSize).list();
	}

	/**
	 * 查询退货率
	 */
	public Double findRefundRate(String id) {
		Long buyQty = this.saleBillDao.findUnique("select sum(abs(dtl.qty)) from SaleBillDtl dtl where dtl.actPrice > 0 and dtl.billNo in" +
				" (select a.billNo from SaleBill a where a.client2Id=? )", new Object[]{id});
		Long backQty = this.saleBillDao.findUnique("select sum(abs(dtl.qty)) from SaleBillDtl dtl where  dtl.actPrice < 0 " +
				"and  dtl.billNo in" +
				" (select a.billNo from SaleBill a where a.client2Id=?) ", new Object[]{id});
		DecimalFormat df = new DecimalFormat("######0.00");
		double back = 1d;
		if (CommonUtil.isNotBlank(buyQty) && buyQty != 0) {
			if (CommonUtil.isNotBlank(backQty) && backQty != 0) {
				back = Double.parseDouble(df.format(((double) backQty / (double) buyQty)));
			} else {
				back = 0;
			}
		}
		return back;
	}

	/**
	 * 查询连带率
	 */
	public Double findAssociatedRate(String id) {
		Long buyQty = this.saleBillDao.findUnique("select sum(abs(qty)) from SaleBillDtl dtl where dtl.actPrice > 0 and dtl.billNo in" +
				" (select a.billNo from SaleBill a where a.client2Id=? )", new Object[]{id});
		Long billQty = this.saleBillDao.findUnique("select count(*) from SaleBillDtl dtl where  dtl.actPrice > 0 " +
				"and  dtl.billNo in" +
				" (select a.billNo from SaleBill a where a.client2Id=?) ", new Object[]{id});
		DecimalFormat df = new DecimalFormat("######0.00");
		double back = 1d;
		if (CommonUtil.isNotBlank(billQty) && billQty != 0) {
			if (CommonUtil.isNotBlank(buyQty) &&buyQty != 0) {
				back = Double.parseDouble(df.format(((double) buyQty / (double) billQty)));
			}else {
				back = 0;
			}
		}
		return back;
	}
	public  List<Object[]> findSeason(String id) {
		List<Object[]> seasons= this.saleBillDao.find("select sty.class10,sum(s.totOrderQty),sum(s.totActValue) from SaleBillDtl d,SaleBill s,"
				+ "com.casesoft.dmc.model.product.Style sty where s.client2Id=? and  d.billNo=s.billNo "
				+ " and d.styleId = sty.id and d.actPrice > 0 and sty.class10 is not null  group by sty.class10 ", new Object[]{id});
		return seasons;
	}
	@Override
	public <X> List<X> findAll(PropertyFilter filter) {
		String hql = filter.getHql();
		return this.saleBillDao.find(hql, filter.getValues());
	}

	public SaleBill findLastSaBillByCustomer(String id) {
		String hql = "from SaleBill s where s.id=(select max(id) from SaleBill where client2Id=? and type=0) and s.client2Id=?";
		return this.saleBillDao.findUnique(hql,  new Object[] {id,id});
	}

	public List<SaleBillColorAnalysis> SaleBillDtlColorAnalysis(String id) {
		String hql ="select new com.casesoft.dmc.extend.scm.sale.SaleBillColorAnalysis(d.colorId,c.colorName,count(d.colorId) as colorCount) from"
				   +" SaleBillDtl d,SaleBill s ,Color c  where d.client2Id=? and d.qty!=0 and d.billId=s.billNo and s.type=0 and d.colorId = c.colorId group by d.colorId,c.colorName order by colorCount desc)";
		return this.saleBillDao.find(hql, new Object[] {id});
	}

	public List<SaleBillStyleAnalysis> SaleBillDtlStyleAnalysis(String id) {
		String hql = "select new com.casesoft.dmc.extend.scm.sale.SaleBillStyleAnalysis(d.styleId,sty.styleName,sty.class4,"
				   + " count(sty.class4) as class4Count,sty.class8,count(sty.class8) as class8Count,sty.class10,count(sty.class10) "
				   + " as class10Count,d.actPrice*count(d.styleId),count(d.styleId)) from SaleBillDtl d,SaleBill s,"
				   + "com.casesoft.dmc.model.product.Style sty where d.client2Id=? and d.qty!=0 and d.billId=s.billNo and s.type=0"
				   + " and d.styleId = sty.id group by d.styleId,sty.styleName,sty.class4,sty.class8,sty.class10,d.actPrice order by class4Count desc,class8Count desc";
		return this.saleBillDao.find(hql, new Object[] {id});
	}

	public List<SaleBillSizeAnalysis> SaleBillDtlSizeAnalysis(String id) {
		String hql ="select new com.casesoft.dmc.extend.scm.sale.SaleBillSizeAnalysis(d.sizeId,si.sizeName,count(d.sizeId) as sizeCount) from"
				   +" SaleBillDtl d,SaleBill s ,Size si  where d.client2Id=? and d.qty!=0 and d.billId=s.billNo and s.type=0 and d.sizeId = si.sizeId group by d.sizeId,si.sizeName order by sizeCount desc)";
		return this.saleBillDao.find(hql, new Object[] {id});
	}

	public List<Object[]> sumSaleAndRefundQty(String id) {
		String hql = "select type,sum(totOrderQty) from SaleBill where client2Id=? group by type order by type asc";
		return this.saleBillDao.find(hql,  new Object[] {id});
	}

	public List<SaleBillDtl> findDtlListByCustomer(String id) {
		return this.saleBillDao.find("from SaleBillDtl where client2Id=? and qty > 0",new Object[] {id});
	}

	public Long sumBuyBillQty(String id) {
		
		return this.saleBillDao.findUnique("select count(*) from SaleBill where client2Id=?", new Object[] {id});
	}

	

}
