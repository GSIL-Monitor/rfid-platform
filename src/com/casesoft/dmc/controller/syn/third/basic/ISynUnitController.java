package com.casesoft.dmc.controller.syn.third.basic;

import com.casesoft.dmc.core.vo.MessageBox;

/**
 * Created by pc on 2016-12-18.
 */
public interface ISynUnitController {

    public MessageBox synchronizeWarehouse();//仓库

    public MessageBox synchronizeShop();//店铺

    public MessageBox synchronizeAgent() ;//代理商，分销商

    public MessageBox synchronizeVender();//供应商

    public MessageBox synchronizeUnit();

    public MessageBox synchronizeFactory();

    public MessageBox synchronizeRole();

    public MessageBox synchronizeRoleRes();

    public MessageBox synchronizeCustomer();

    public MessageBox synchronizeResource();

    public MessageBox synchronizeUser();
}
