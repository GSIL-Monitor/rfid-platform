package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.*;
import com.casesoft.dmc.extend.third.request.BaseDao;
import com.casesoft.dmc.model.hall.*;
import com.casesoft.dmc.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.casesoft.dmc.core.tag.util.StringUtil.joinString;

/**
 * Created by session on 2017/3/10 0010.
 * mainly server for ApiHub
 */

@Service
@Transactional
public class HallTaskService extends BaseDao<HallTask,String> {

	@Autowired
	private HallTaskDao hallTaskDao;

	@Autowired
	private HallTaskDetailDao hallTaskDetailDao;

	@Autowired
	private HallInventoryDao hallInventoryDao;

	@Autowired
	private SampleDao sampleDao;

	@Autowired
	private HallFloorDao hallFloorDao;

	@Override
	public Page<HallTask> findPage(Page<HallTask> page, List<PropertyFilter> filters) {
		return this.hallTaskDao.findPage(page,filters);
	}

	@Override
	public void save(HallTask entity) {
		this.hallTaskDao.delete(entity);
	}

	@Override
	public HallTask load(String id) {
		return this.hallTaskDao.load(id);
	}

	public HallTask get(String propertyName, Object value) {
		return null;
	}


	public List<HallTaskDetail> findDetail(List<PropertyFilter> filters) {
		return (List<HallTaskDetail>)this.hallTaskDetailDao.find(filters);
	}

	public List<HallTaskDetail> findBorrowList(String taskids){
		String hql = "from HallTaskDetail d where d.taskId in (" + taskids + ")";
		return this.hallTaskDao.find(hql, new Object[]{});
	}


	@Override
	public List<HallTask> getAll() {
		return null;
	}

	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(HallTask entity) {

	}

	public void deleteTaskByDeviceId(String deviceId){
		String hql="delete from HallTask h where h.deviceId=?";
		this.hallTaskDao.batchExecute(hql,new Object[]{deviceId});
	}

	public String findTaskMaxCode(String prefix) {
		String hql = "select max(CAST(SUBSTRING(t.id," + (prefix.length()
				+ 1) + "),integer))"
				+ " from HallTask as t where t.id like ?";
		Integer code = this.hallTaskDao.findUnique(hql, prefix + '%');
		return code == null ? (prefix + "001") : prefix
				+ CommonUtil.convertIntToString(code + 1, 3);
	}

	@Override
	public void delete(HallTask entity) {

	}
//-------------------------------------->
	public void save(List<HallTask> list) {
		this.hallTaskDao.doBatchInsert(list);
	}

	public void saveSamples(List<Sample> sampleList) {
		for(Sample s : sampleList) {
			this.hallTaskDao.saveOrUpdateX(s);
		}
	}
	public void saveDetails(List<HallTaskDetail> taskDetails){
			this.hallTaskDao.doBatchInsert(taskDetails);
	}

//-------------------------------------->
//Query in Sample
	public List<Sample> findSampleByCodes(String codes, String ownerId) {
		String hql = "from Sample s where s.code in ("
				+ codes
				+ ") and "
				+ "  s.ownerId=?";
		return this.sampleDao.find(hql, new Object[]{ownerId});
	}

	public void save(HallTask task, List<Sample> sampleList,
					 List<Product> productList) {
		// TODO Auto-generated method stub
		this.sampleDao.saveOrUpdateX(task);
		if(CommonUtil.isNotBlank(task.getDetailList())) {
			this.sampleDao.doBatchInsert(task.getDetailList());
		}
		this.sampleDao.doBatchInsert(sampleList);
		if(CommonUtil.isNotBlank(productList)){
			this.sampleDao.doBatchInsert(productList);
		}

	}

	public void save(HallTask entity,List<Sample> sampleList) {
		this.hallTaskDao.save(entity);
		//this.amsTask2Dao.doBatchInsert(sampleList);
		for (HallTaskDetail detail:entity.getDetailList()){
			this.hallTaskDetailDao.saveOrUpdate(detail);
		}
		for(Sample s : sampleList) {
			this.hallTaskDao.saveOrUpdateX(s);
		}
	}

	public Sample findSample(String code, String ownerId) {
		String hql = "from Sample s where s.code=? and s.ownerId=?";
		List<Sample> list = this.sampleDao.find(hql,new Object[]{code,ownerId});
		return CommonUtil.isBlank(list)?null:list.get(0);
	}

	public List<Sample> findSample(List<PropertyFilter> filters){
		return this.sampleDao.find(filters);
	}

	public List<Sample> findBorrowSample(String codeList) {
		int split = 990;
		if (codeList.split(",").length <= split) {
			String hql = "from Sample s where s.code in ("
					+ codeList
					+ ") and s.status=?";
			return this.sampleDao.find(hql,new Object[]{2});
		} else {
			List<Sample> sampleList = new ArrayList<Sample>();
			String[] strArr = codeList.toString().split(",");
			int l = strArr.length/split;
			int i1 = 0;

			for(int i=0;i<=l;i++) {
				int i2 = i1+split;
				if(i2>strArr.length) {
					String[] tempArr = Arrays.copyOfRange(strArr, i1, strArr.length);
					String tempCodeList = joinString(tempArr,",");
					sampleList.addAll(findBorrowSample(tempCodeList));
					break;
				}
				String[] tempArr = Arrays.copyOfRange(strArr, i1, i2);
				String tempCodeList = joinString(tempArr,",");
				sampleList.addAll(findBorrowSample(tempCodeList));
				i1=i2;
			}
			return sampleList;
		}
	}

	public List<Sample> findOutboundCodeListByCode(String code, int type,String outboundSampleRoomId) {
		String hql = "from Sample s where s.status=? and s.businessTaskId in " +
				" (select s1.businessTaskId from Sample s1 where s1.code=? and s1.status=? and s1.businessOwnerId=?)";
		return this.sampleDao.find(hql,new Object[]{type,code,type,outboundSampleRoomId});
	}

	public  void updateSampleFloor(String floor,String codes){
		String hql="update Sample s  set s.floor=? where s.code in("+codes+")";
		this.sampleDao.batchExecute(hql, new Object[]{floor});
	}

	public List<Sample> findInRoom(String codeList, String sampleRoomId) {
		int split = 990;
		if (codeList.split(",").length <= split) {
			String hql = "from Sample s where s.code in (" + codeList
					+ ") and s.ownerId=?";
			// + " s.status=1 and s.ownerId=?";
			return this.sampleDao.find(hql, new Object[] { sampleRoomId });
		} else {
			List<Sample> sampleList = new ArrayList<Sample>();
			String[] strArr = codeList.toString().split(",");
			int l = strArr.length / split;
			int i1 = 0;

			for (int i = 0; i <= l; i++) {
				int i2 = i1 + split;
				if (i2 > strArr.length) {
					String[] tempArr = Arrays.copyOfRange(strArr, i1,
							strArr.length);
					String tempCodeList = joinString(tempArr, ",");
					sampleList.addAll(findInRoom(tempCodeList, sampleRoomId));
					break;
				}
				String[] tempArr = Arrays.copyOfRange(strArr, i1, i2);
				String tempCodeList = joinString(tempArr, ",");
				sampleList.addAll(findInRoom(tempCodeList,sampleRoomId));
				i1 = i2;
			}
			return sampleList;
		}
	}
	//---------------------------------->
	//Query in HallInventory

	public String findBillMaxCode(String prefix) {
		String hql = "select max(CAST(SUBSTRING(sb.taskId,"
				+ (prefix.length() + 1) + "),integer))"
				+ " from HallInventory as sb where sb.taskId like ?";
		Integer code = this.hallInventoryDao.findUnique(hql, prefix + '%');
		return code == null ? (prefix + "001") : prefix
				+ CommonUtil.convertIntToString(code + 1, 3);
	}
	public void save(HallFloor s, List<Sample> list, HallInventory inv, List<HallInventoryDetail> details) {
		this.hallFloorDao.saveOrUpdate(s);		//库位	样衣		盘点任务 盘点明细
		this.hallInventoryDao.saveOrUpdate(inv);
		this.sampleDao.doBatchInsert(list);
		this.hallInventoryDao.doBatchInsert(details);
	}

	//---------------------------------->
	//Query in HallFloor

	public HallFloor findFloorByOnerwId(String ownerId){
		//areaId在原库中为hostId
		String hql = "from HallFloor hf where hf.areaId='A' and hf.ownerId=?";
		List<HallFloor> s = this.hallFloorDao.find(hql, new Object[]{ownerId});
		return s.size()>0?s.get(0):null;
	}

	//---------------------------------->
	@Override
	public void delete(String id) {

	}

	public List<Sample> findIsInStock(String codeList, String ownerId,
									  boolean isIn) {
		int split = 990;
		if(codeList.split(",").length<=split) {
			System.out.println("执行2");
			String hql = "from Sample s where s.code in ("
					+ codeList
					+ ")"
					+ " and  s.status<>3 and s.status<>4 and s.status<>0 and s.status<>5 and s.status<>8 and ownerId=?";// 有已入库的，
			if(isIn) {
				return this.hallTaskDao.find(hql, new Object[] {ownerId});
			}
			else {// 是出库
				hql = "from Sample s where s.code in ("+ codeList + ")" +" and s.ownerId=?";
				return this.hallTaskDao.find(hql, new Object[]{ownerId});
			}
		} else {
			List<Sample> sampleList = new ArrayList<Sample>();
			System.out.println("执行1");
			String[] strArr = codeList.toString().split(",");
			int l = strArr.length/split;
			int i1 = 0;

			for(int i=0;i<=l;i++) {
				int i2 = i1+split;
				if(i2>strArr.length) {
					String[] tempArr = Arrays.copyOfRange(strArr, i1, strArr.length);
					String tempCodeList = joinString(tempArr,",");
					sampleList.addAll(findIsInStock(tempCodeList,ownerId,isIn));
					break;
				}
				String[] tempArr = Arrays.copyOfRange(strArr, i1, i2);
				String tempCodeList = joinString(tempArr,",");
				sampleList.addAll(findIsInStock(tempCodeList,ownerId,isIn));
				i1=i2;
			}
			return sampleList;
		}

	}

	public List<Sample> findIsCanBorrow(String codeList, String ownerId,
										String preBorrowUser) {
		int split = 990;
		if (codeList.split(",").length <= split) {
			String hql = "from Sample s where s.code in (" + codeList
					+ ") and s.ownerId=?";
			// + " s.status=1 and s.ownerId=?";
			return this.sampleDao.find(hql, new Object[] { ownerId });
		} else {
			List<Sample> sampleList = new ArrayList<Sample>();
			String[] strArr = codeList.toString().split(",");
			int l = strArr.length / split;
			int i1 = 0;

			for (int i = 0; i <= l; i++) {
				int i2 = i1 + split;
				if (i2 > strArr.length) {
					String[] tempArr = Arrays.copyOfRange(strArr, i1,
							strArr.length);
					String tempCodeList = joinString(tempArr, ",");
					sampleList.addAll(findIsCanBorrow(tempCodeList, ownerId,
							preBorrowUser));
					break;
				}
				String[] tempArr = Arrays.copyOfRange(strArr, i1, i2);
				String tempCodeList = joinString(tempArr, ",");
				sampleList.addAll(findIsCanBorrow(tempCodeList, ownerId,
						preBorrowUser));
				i1 = i2;
			}
			return sampleList;
		}
	}


//delete functions
	//inbound
	public void deleteWithInbound(String code,String deviceId){
		String hqlSample="delete from Sample s where s.code=?";
		String hqlHallTask="delete from HallTask ht where ht.deviceId=?";
		String hqltaskDetail="delete from HallTaskDetail htd where htd.code=?";

		this.hallTaskDao.batchExecute(hqlSample,new Object[]{code});
		this.hallTaskDetailDao.batchExecute(hqltaskDetail,new Object[]{code});
		this.hallTaskDao.batchExecute(hqlHallTask,new Object[]{deviceId});
	}

	public void deleteWithInv(String code,String deviceId){
		String hqlInv="delete from HallInventory HI where HI.deviceId=?";
		String hqlInvDtl="delete from HallInventoryDetail HID where HID.code=?";
		this.hallTaskDao.batchExecute(hqlInv,new Object[]{deviceId});
		this.hallTaskDao.batchExecute(hqlInvDtl,new Object[]{code});
		deleteWithInbound(code,deviceId);
	}

	public List<HallTaskDetail> findDetailByCode(String code, String businessTaskId) {
		String hql=" select d from HallTaskDetail d ,Sample s where s.code=d.code and d.code = ? and d.taskId =?";
		return this.hallTaskDao.find(hql,new Object[]{code,businessTaskId});
	}
}
