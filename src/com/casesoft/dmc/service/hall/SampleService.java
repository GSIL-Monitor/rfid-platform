package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.HallTaskDao;
import com.casesoft.dmc.dao.hall.HallTaskDetailDao;
import com.casesoft.dmc.dao.hall.SampleDao;
import com.casesoft.dmc.model.hall.HallTask;
import com.casesoft.dmc.model.hall.HallTaskDetail;
import com.casesoft.dmc.model.hall.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

@Service
@Transactional
public class SampleService extends AbstractBaseService<Sample, String> {

	@Autowired
	private SampleDao sampleDao;
	@Autowired
	private HallTaskDao hallTaskDao;
	@Autowired
	private HallTaskDetailDao hallTaskDetailDao;
	@Autowired
	private HallRoomService hallRoomService;

	@Override
	public Page<Sample> findPage(Page<Sample> page, List<PropertyFilter> filters) {
		return this.sampleDao.findPage(page, filters);
	}

	@Override
	public void save(Sample entity) {
		this.sampleDao.saveOrUpdate(entity);
	}

	@Override
	public Sample load(String id) {
		return null;
	}

	public Sample getById(String id){
		String hql="from Sample s where s.id=?";
		return this.sampleDao.findUnique(hql,new Object[]{id});
	}

	@Override
	public Sample get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Sample> find(List<PropertyFilter> filters) {
		return this.sampleDao.find(filters);
	}

	public Sample findByCode(String code) {
		return this.sampleDao.findUnique("from Sample s where s.code=?", new Object[]{code});
	}

	public List<Sample> findByOwnreId(String ownerId) {
		return this.sampleDao.find("from Sample s where s.ownerId=?", new Object[]{ownerId});
	}

	@Override
	public List<Sample> getAll() {
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(Sample entity) {

	}

	public void updateStatus(String codeList, String deviceId, int type, String customerId) {
		String sampleRoomId = CacheManager.getDeviceByCode(deviceId).getStorageId();
//		String sampleRoomId =this.hallRoomService.findRoomByDeviceId(deviceId);
		String hql = "";
		switch (type) {
			case 1:// 入库
				hql = "update Sample s set s.status=1,s.inDate=?,s.inDeviceId=?,s.inOwnerId=?"
						+ " where s.code in (" + codeList + ") and s.status=0";
				System.out.println("deviceId=" + deviceId + "; sampleRoomId=" + sampleRoomId);
				this.sampleDao.batchExecute(hql, new Object[]{new Date(), deviceId, sampleRoomId});
				break;
			case 7:// 调拨入库
				hql = "update Sample s set s.status=1,s.inDate=?,s.inDeviceId=?,s.ownerId=?"
						+ " where s.code in (" + codeList + ") and s.status=3";
				System.out.println("deviceId=" + deviceId + "; sampleRoomId=" + sampleRoomId);
				this.sampleDao.batchExecute(hql, new Object[]{new Date(), deviceId, sampleRoomId});
				break;
			case 8:// 强制入库
				hql = "update Sample s set s.status=1,s.inDate=?,s.inDeviceId=?,s.ownerId=?"
						+ " where s.code in (" + codeList + ")";
				System.out.println("deviceId=" + deviceId + "; sampleRoomId=" + sampleRoomId);
				this.sampleDao.batchExecute(hql, new Object[]{new Date(), deviceId, sampleRoomId});
				break;
			case 2:// 借出
				hql = "update Sample s set s.status=2,s.businessDate=?,s.businessDeviceId=?,s.businessOwnerId=? , s.isPreBorrow=1"
						+ " where s.code in (" + codeList + ") and s.status=1 ";
				this.sampleDao.batchExecute(hql, new Object[]{new Date(), deviceId, customerId});
				break;
			case 3:// 调拨出库 ,将ownerId设置为调入方的样衣间ID
				hql = "update Sample s set s.status=3,s.businessDate=?,s.businessDeviceId='" + deviceId +
						"',s.businessOwnerId='" + sampleRoomId + "',s.ownerId='" + customerId + "'"
						+ " where s.code in (" + codeList + ") and s.status=1";
				this.sampleDao.batchExecute(hql, new Object[]{new Date()});

				break;
			case 6:// 投产下单
				hql = "update Sample s set s.status=4,s.businessDate=?,s.businessDeviceId=?,s.businessOwnerId=?"
						+ " where s.code in (" + codeList + ") and s.status=1";
				this.sampleDao.batchExecute(hql, new Object[]{new Date(), deviceId, customerId});
				break;
			case 4:// 返回
				hql = "update Sample s set s.status=1,s.ownerId=?,s.businessDate=?,s.businessDeviceId=?,s.businessOwnerId=?"
						+ " where s.code in (" + codeList + ") ";
				this.sampleDao.batchExecute(hql, new Object[]{sampleRoomId, new Date(), deviceId, customerId});
				break;
			case 5:// 丢失
				hql = "update Sample s set s.status=5,s.ownerId，s.businessDate=?,s.businessDeviceId=?,s.businessOwnerId=?"
						+ " where s.code in (" + codeList + ") ";
				this.sampleDao.batchExecute(hql, new Object[]{sampleRoomId, new Date(), deviceId, customerId});
				break;
		}

	}

	public List<Sample> findDtlListByCode(String code, int type) {
		String hql = "from Sample s where s.status=? and s.businessTaskId in " +
				" (select s1.businessTaskId from Sample s1 where s1.code=? and s1.status=?)";
		return this.sampleDao.find(hql, new Object[]{type, code, type});
	}

	public List<Sample> findDtlListByCode(String code, int type, String sampleRoomId) {
		String hql = "from Sample s where s.status=? and s.businessTaskId in" +
				" (select s1.businessTaskId from Sample s1 where s1.code=? and s1.status=? and s1.inOwnerId=?)";
		return this.sampleDao.find(hql, new Object[]{type, code, type, sampleRoomId});
	}


	public void save(HallTask task, List<Sample> sampleList) {

		List<HallTaskDetail> hallTaskDetailList = task.getDetailList();

		this.hallTaskDetailDao.doBatchInsert(hallTaskDetailList);


		this.sampleDao.doBatchInsert(sampleList);
		this.hallTaskDao.saveOrUpdateX(task);
	}

	public void toInbound(HallTask task, String codeList) {
		this.sampleDao.saveOrUpdateX(task);
		this.sampleDao.doBatchInsert(task.getDetailList());
		this.updateStatus(codeList, task.getDeviceId(), 7, null);
	}

	public void toForceInbound(HallTask task, String codeList) {
		this.sampleDao.saveOrUpdateX(task);
		this.sampleDao.doBatchInsert(task.getDetailList());
		this.updateStatus(codeList, task.getDeviceId(), 8, null);
	}

	public void update(Sample entity, HallTask task) {
		this.sampleDao.update(entity);
		if(CommonUtil.isNotBlank(task)){
			this.sampleDao.saveOrUpdateX(task);
			if(CommonUtil.isNotBlank(task.getDetailList())){
				this.sampleDao.doBatchInsert(task.getDetailList());
			}
		}

	}

	@Override
	public void delete(Sample entity) {

	}


	@Override
	public void delete(String id) {

	}
}
