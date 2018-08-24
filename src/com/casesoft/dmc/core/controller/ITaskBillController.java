package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.vo.MessageBox;

import java.util.List;

/**
 * 任务单据的状态维护： 保存后的状态为已扫描，已扫描的可以废除
 *     执行(发货或收货或盘点)后，不能废除
 * @author WingLi
 *
 * @param <T>
 */
public interface ITaskBillController<T> {
    public abstract List<T> list() throws Exception;
    public abstract MessageBox save(T entity, String strDtlList) throws Exception;
    public abstract MessageBox edit(String taskId) throws Exception;
    public abstract MessageBox execute(String taskId) throws Exception;//执行 (入库或出库或盘点调整)
    public abstract MessageBox delete(String taskId) throws Exception;//为单元测试使用
    public abstract MessageBox abolish(String taskId) throws Exception;
    public abstract MessageBox recover(String taskId) throws Exception;
    public abstract void exportDtlExcel() throws Exception;
    public abstract void exportUniqueCodeExcel() throws Exception;
    public abstract MessageBox importUniqueCodeExcel() throws Exception;
}
