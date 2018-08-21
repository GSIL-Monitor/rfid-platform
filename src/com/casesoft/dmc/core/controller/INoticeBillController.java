package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.vo.MessageBox;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface INoticeBillController<T> {

    public abstract List<T> list() throws Exception;
    public abstract MessageBox save(T entity, String strDtlList) throws Exception;
    public abstract MessageBox add() throws Exception;
    public abstract MessageBox edit(String billNo, String agentId) throws Exception;
    public abstract MessageBox check(String billNo, String agentId) throws Exception;
    public abstract MessageBox notice(String billNo, String agentId) throws Exception;
    public abstract MessageBox end(String billNo, String agentId) throws Exception;
    public abstract MessageBox delete(String billNo, String agentId) throws Exception;
    public abstract MessageBox abolish(String billNo, String agentId) throws Exception;
    public abstract MessageBox recover(String billNo, String agentId) throws Exception;
    public abstract void exportExcel() throws Exception;
    public abstract MessageBox importExcel(MultipartFile file) throws Exception;
}
