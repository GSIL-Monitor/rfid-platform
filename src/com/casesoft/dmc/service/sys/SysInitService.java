package com.casesoft.dmc.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by WingLi on 2016-11-29.
 */
@Service
@Transactional
public class SysInitService {

    public boolean isFirst() {
        return true;
    }

    public boolean initData() {
        //菜单

        //角色

        //角色权限

        //组织信息

        //公司
        return true;
    }
}
