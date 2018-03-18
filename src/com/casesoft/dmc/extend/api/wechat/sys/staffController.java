package com.casesoft.dmc.extend.api.wechat.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.StaffInfo;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yushen on 2017/11/30.
 */
@Controller
@RequestMapping(value = "/api/weChat/sys/staff", method = {RequestMethod.POST, RequestMethod.GET})
public class staffController {
    @Autowired
    private UserService userService;

    /**
     *
     * @param phone
     * @param openId
     * @return msg说明: Y.保存成功; N.不是本店员工或尚未在user表中录入手机号码; F.保存失败
     */
    @RequestMapping(value = "/uploadStaffInfo.do")
    @ResponseBody
    public MessageBox uploadStaffInfo(String phone, String openId) {
        try {
            User user = this.userService.findUserByPhone(phone);
            if (CommonUtil.isNotBlank(user)) {
                String defaultWH = CacheManager.getUnitById(user.getOwnerId()).getDefaultWarehId();
                user.setDefaultWH(defaultWH);
                StaffInfo staffInfo = new StaffInfo();
                staffInfo.setOpenId(openId);
                staffInfo.setPhone(phone);
                this.userService.saveStaffInfo(staffInfo);
                return new MessageBox(true, "Y", user);
            } else {
                return new MessageBox(false, "N");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "F");
        }
    }


    /**
     * @param openId
     * @return msg说明: Y.本店员工可直接登录; S.第一次登录，未查询到openId; F.查询失败
     */
    @RequestMapping(value = "/firstLoginCheck.do")
    @ResponseBody
    public MessageBox firstLoginCheck(String openId) {
        try {
            StaffInfo staff = this.userService.findStaffByOpenId(openId);
            if (CommonUtil.isNotBlank(staff)) {
                User user = this.userService.findUserByPhone(staff.getPhone());
                String defaultWH = CacheManager.getUnitById(user.getOwnerId()).getDefaultWarehId();
                user.setDefaultWH(defaultWH);
                return new MessageBox(true, "Y", user);
            } else {
                return new MessageBox(false, "S");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, "F");
        }

    }
}
