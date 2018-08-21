package com.casesoft.dmc.extend.msg.controller;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.extend.msg.websocket.WsMsgWebSocketHandler;
import com.casesoft.dmc.extend.msg.websocket.entity.OnlineUserVo;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessage;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author WinLi
 */
@Controller
@RequestMapping("/data/msgPush")
public class MsgPushController extends BaseController {
    
    @Autowired
    private WsMessageService wsMessageService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/data/msgPush";
    }
    
    @RequestMapping("/page")
    @ResponseBody
    public Page<WsMessage> findPage(Page<WsMessage> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = wsMessageService.findPage(page, filters);
        return page;
    }

    @RequestMapping("/toConsole")
    public ModelAndView toConsole() {
        ModelAndView modelAndView = new ModelAndView("/views/data/msgPush_console");
        return modelAndView;
    }
    @RequestMapping("/listOnlineDevice")
    @ResponseBody
    public Collection<OnlineUserVo> listOnlineDevice() {
        Collection<OnlineUserVo> deviceSessionList = WsMsgWebSocketHandler.onlineUserMap.values();
        return deviceSessionList;
    }
    
}
