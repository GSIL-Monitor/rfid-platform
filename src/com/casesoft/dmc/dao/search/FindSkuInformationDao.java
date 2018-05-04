package com.casesoft.dmc.dao.search;

import com.casesoft.dmc.core.dao.BaseHibernateDao;
import com.casesoft.dmc.model.search.DetailStockCodeView;
import com.casesoft.dmc.model.search.FindSkuInformation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/5/4.
 */
@Repository
public class FindSkuInformationDao extends BaseHibernateDao<FindSkuInformation, String> {
}
