package com.casesoft.dmc.controller.pad;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.pad.WeiXinUser;
import com.casesoft.dmc.service.pad.WeiXinUserService;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号
 * Created by ltc on 2018/6/7.
 */
@Controller
@RequestMapping("/pad/WxPublicNumber")
public class WxPublicNumber extends BaseController implements IBaseInfoController<WxPublicNumber> {
    @Autowired
    private WeiXinUserService weiXinUserService;
    /**
     *测试基本配置
     * @param req 微信后台回传信息
     * @param resp 返回信息告知微信后台已接收
     * @return 消息
     */
    @RequestMapping("/indexWS")
    public String login(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        Map<String ,String> map  = xmlToMap(req);
        if (map.get("Event").equals("subscribe")){
            //获取openid
            String fromUserName = map.get("FromUserName");
            //获取关注的微信公众号信息
            JSONObject weiXinJson = WeiXinUserInfoUtils.getUserInfo(fromUserName);
            System.out.println(weiXinJson);
            Object errcode = weiXinJson.get("errcode");
            if(errcode == null || "0".equals(errcode)) {
                WeiXinUser weiXinUser = new WeiXinUser();
                weiXinUser.setSubscribe(weiXinJson.getInt("subscribe"));
                weiXinUser.setOpenId(weiXinJson.getString("openid"));
                weiXinUser.setNickname(weiXinJson.getString("nickname"));
                weiXinUser.setSex(weiXinJson.getInt("sex"));
                weiXinUser.setLanguage(weiXinJson.getString("language"));
                weiXinUser.setCity(weiXinJson.getString("city"));
                weiXinUser.setProvince(weiXinJson.getString("province"));
                weiXinUser.setCountry(weiXinJson.getString("country"));
                weiXinUser.setHeadImgUrl(weiXinJson.getString("headimgurl"));
                weiXinUser.setSubscribeTime(weiXinJson.getString("subscribe_time"));
//                weiXinUser.setUnionId(weiXinJson.getString("unionid"));
                System.out.println(weiXinUser.toString());
                this.weiXinUserService.save(weiXinUser);
            }else {
                System.out.println("当前获取weiXinUser失败，错误码为："+errcode + "错误原因为："+weiXinJson.getString("errmsg"));
            }
        }
        /*String echostr = req.getParameter("echostr");
        PrintWriter pw = resp.getWriter();
        pw.write(echostr);  //这里 echostr 的值必须返回，否则微信认为请求失败
        pw.flush();
        pw.close();*/
        return "";
    }
    /**
     * XML格式转为map格式
     * @param request  微信后台回传信息
     * @return 解析后的map
     */
    private static Map<String , String> xmlToMap(HttpServletRequest request){
        Map<String ,String> map = new HashMap<String , String>();
        try {
            InputStream inputStream =null;
            inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(inputStream);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element el:elements) {
                map.put(el.getName() , el.getText());
            }
            inputStream.close();
            return map ;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    @Override
    public String index() {
        return null;
    }

    @Override
    public Page<WxPublicNumber> findPage(Page<WxPublicNumber> page) throws Exception {
        return null;
    }

    @Override
    public List<WxPublicNumber> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(WxPublicNumber entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
