package com.casesoft.dmc.extend.msg.websocket.entity;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pc on 2016/3/28.
 */
@Service
@Transactional
public class WsMessageService {

    @Autowired
    private WsMessageDao wsMessageDao;

    public Page<WsMessage> findPage(Page<WsMessage> page,
            List<PropertyFilter> filters) {
        return this.wsMessageDao.findPage(page, filters);
    }

    public void save(WsMessage entity) {
        if(CommonUtil.isBlank(entity.getId())) {
            entity.setId(new GuidCreator().toString());
        }
        wsMessageDao.save(entity);
    }

    /**
     * @param userCode 获取未发送的消息
	 *
     */
    public long findCountAcceptUnSuccessMsg(String userCode) {
        String hql = "select count(*) from WsMessage m where m.toCode=? and m.success=?";

        return this.wsMessageDao.findUnique(hql, new Object[]{userCode, false});
    }

    public List<WsMessage> findAcceptUnSuccessMsg(String userCode) {
        String hql = "from WsMessage m where m.toCode=? and m.success=?";
        return this.wsMessageDao.find(hql, new Object[]{userCode, false});
    }

    public void save(List<WsMessage> wsMessages) {
        this.wsMessageDao.doBatchInsert(wsMessages);
    }
}
