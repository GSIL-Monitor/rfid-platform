package com.casesoft.dmc.service.search;

import com.casesoft.dmc.dao.search.BoxDetailSearchDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by GuoJunwen on 2017-04-11.
 */
@Service
@Transactional
public class BoxDetailSearchService  {

    @Autowired
    private BoxDetailSearchDao boxDetailSearchDao;


    public DataResult find(RequestPageData<?> request) {

        return boxDetailSearchDao.find(request);
    }


}
