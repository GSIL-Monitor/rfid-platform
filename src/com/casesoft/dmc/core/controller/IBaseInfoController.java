package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 基础信息的状态维护： 审核中的不能删除、可以废除，录入中的可以删除
 * @author WingLi
 *
 * @param <T>
 */
public interface IBaseInfoController<T> {

    public abstract Page<T> findPage(Page<T> page) throws Exception;
    public abstract List<T> list() throws Exception;
    public abstract MessageBox save(T entity) throws Exception;
    public abstract MessageBox edit(String taskId) throws Exception;
    public abstract MessageBox delete(String taskId) throws Exception;
    public abstract void exportExcel() throws Exception;
    public abstract MessageBox importExcel(MultipartFile file) throws Exception;
}
