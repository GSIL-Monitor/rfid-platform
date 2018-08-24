package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ILogisticsBillController<T> {

    Page<T> findPage(Page<T> page) throws Exception;
    List<T> list() throws Exception;
    /**
     * @Param String bill单据 json字符串
     * @Param String strDtlList 单据明细 jsonArryList字符串 保存单据为录入状态
     * */
    MessageBox save(String bill, String strDtlList, String userId) throws Exception;
    ModelAndView add() throws Exception;
    ModelAndView edit(String billNo) throws Exception;
    MessageBox check(String billNo) throws Exception;
    MessageBox end(String billNo) throws Exception;
    MessageBox cancel(String billNo) throws Exception;
    /**
     * @Param String strDtlList 单据明细 jsonArryList字符串
     *  @Param String recordList 唯一码明细 jsonArryList字符串
     * */
    MessageBox convert(String strDtlList, String recordList) throws Exception;//
}
