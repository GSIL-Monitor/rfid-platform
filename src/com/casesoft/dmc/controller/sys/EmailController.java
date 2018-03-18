package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.email.SimpleMailService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Email;
import com.casesoft.dmc.service.sys.impl.EmailService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.cxf.wsdl.http.UrlEncoded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("email")
public class EmailController extends BaseController implements IBaseInfoController<Email> {

    @Autowired
    private EmailService emailService;

    private SimpleMailService simpleMailService;
    private String ADDRESSER = "support@casesoft.com.cn";

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/sys/email";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Email> findPage(Page<Email> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.emailService.findPage(page, filters);
        for (Email email : page.getRows()) {
            email.setType(CacheManager.getSetting("51").getValue());
        }
        return page;
    }

    @RequestMapping(value = "/detail")
    @ResponseBody
    public ModelAndView viewDtl(String id) throws Exception {
        this.logAllRequestParams();
        Email email = this.emailService.findById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("email", email);
        model.setViewName("/views/sys/email_detail");
        return model;
    }

    /**
     * @param id 邮件id
     * @return 是否发送成功
     */
    @RequestMapping(value = "/sendMail")
    @ResponseBody
    public MessageBox sendMail(String id) throws Exception {
        Email email = this.emailService.findById(id);
        email.setStatus(true);
        try {
            SimpleMailService simpleMailService = (SimpleMailService) SpringContextUtil.getBean("simpleMailService");
            String content = StringEscapeUtils.unescapeHtml3(email.getContent());
            String[] users = email.getRecipients().split(";");
            if (email.getAdjunctUrl() == null) {
                simpleMailService.sendMsg(ADDRESSER, users, email.getTitle(), email.getContent());
            } else {
                String rootPath = this.getSession().getServletContext().getRealPath("/");
                simpleMailService.sendAttachments(ADDRESSER, users, email.getTitle(), content, rootPath, email.getAdjunctUrl());

            }
        } catch (Exception e) {
            email.setStatus(false);
            return returnFailInfo("error", e.getMessage());
        }
        email.setSendTime(new Date());
        this.emailService.save(email);
        return returnSuccessInfo("ok");
    }

    /**
     * @param fileName 附件名
     * @return 下载的文件
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download(String fileName) throws IOException {
        this.logAllRequestParams();
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String url = rootPath + "/email/" + fileName;
        File file = new File(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "utf-8"));//中文编码问题
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }

    /**
     * @param file 上传的附件
     * @param id   邮件id
     */
    @RequestMapping(value = "/saveAdjunct")
    @ResponseBody
    public MessageBox saveAdjunct(MultipartFile file, String id) throws Exception {
        this.logAllRequestParams();
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        String fileName = file.getOriginalFilename();
        String fileParant = rootPath + "/email/";
        File folder = new File(fileParant);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File f = new File(folder, fileName);
        file.transferTo(f);
        Email email = this.emailService.findById(id);
        email.setAdjunctUrl(fileName);
        this.emailService.save(email);
        return returnSuccessInfo(fileName);
    }

    @Override
    public List<Email> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/save")
    @Override
    public MessageBox save(Email email) throws Exception {
        Email e = this.emailService.findById(email.getId());
        try {
            if (CommonUtil.isBlank(e)) {
                e = new Email();
            }
            e.setStatus(email.getStatus());
            e.setCopy(email.getCopy());
            e.setTitle(email.getTitle());
            e.setRecipients(email.getRecipients());
            e.setContent(email.getContent());
            e.setSendTime(email.getSendTime());
            e.setAdjunctUrl(email.getAdjunctUrl());
            this.emailService.save(email);
        } catch (Exception ex) {
            return returnFailInfo("error");
        }
        return returnSuccessInfo("ok");
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
