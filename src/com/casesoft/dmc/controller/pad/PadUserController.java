package com.casesoft.dmc.controller.pad;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.service.ServiceException;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

/**
 * 普通用户 user.type=0
 */

@Controller
@RequestMapping("/pad/padUser")
public class PadUserController extends BaseController implements IBaseInfoController<User>{
    @Autowired
    private UserService userService;

    @RequestMapping("/loginWS")
    @ResponseBody
    public MessageBox loginWS(String code, String password) throws Exception {
        this.logAllRequestParams();
        this.getRequest().setCharacterEncoding("utf-8");
        this.getResponse().setContentType("text/html;charset=utf-8");
        User user;
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(code, password);
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            user = this.userService.getUser(code);
            if(currentUser.isAuthenticated()){
                //response.sendRedirect(request.getContextPath()+"/views/pad/padUser.html");
                System.out.println("客户端用户[" + code + "]登录认证通过");
            }else{
                token.clear();
            }
            return new MessageBox(true,"登录成功",user);
        } catch (Exception e){
            this.logger.error(e.getMessage());
            //response.sendRedirect(request.getContextPath()+"/padLogin.html");
            return new MessageBox(false,"请核对用户名密码");
        }
    }

    @Override
    public Page<User> findPage(Page<User> page) throws Exception {
        return null;
    }

    @Override
    public List<User> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(User entity) throws Exception {
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

    @Override
    public String index() {
        return null;
    }
}
