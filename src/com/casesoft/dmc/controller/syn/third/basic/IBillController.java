package com.casesoft.dmc.controller.syn.third.basic;

import com.casesoft.dmc.core.vo.MessageBox;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by john on 2017/1/15.
 */
public interface IBillController {
    /**
     * 查询第三方单据
     *
     * @param thirdBillRequestEntity
     * @param result
     * @return
     */

    public MessageBox findBill(ThirdBillRequestEntity thirdBillRequestEntity, BindingResult result);
    /**
     * 查询第三方单据明细
     * @param billId
     * @return
     */
    public MessageBox findBillDtls(String billId,Integer type);
    /**
     * 查询第三方单据主页
     * @param billId
     * @return
     */
    public ModelAndView viewDtlIndex(String billId,Integer type);

}
