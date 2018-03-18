package com.casesoft.dmc.controller.syn.third.basic;

/**
 * Created by pc on 2016-12-18.
 */
public interface ISynUnitAction {

    public String synchronizeWarehouse();//仓库

    public String synchronizeShop();//店铺

    public String synchronizeAgent() ;//代理商，分销商

    public String synchronizeVender();//供应商

    public String synchronizeUnit();

    public String synchronizeFactory();

    public String synchronizeRole();

    public String synchronizeRoleRes();

    public String synchronizeCustomer();

    public String synchronizeResource();

    public String synchronizeUser();
}
