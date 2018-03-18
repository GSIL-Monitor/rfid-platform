package com.casesoft.dmc.core.service.email;

import java.io.File;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 纯文本邮件服务类.
 * 
 * @author calvin
 */

public class SimpleMailService {
  private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

  private JavaMailSender mailSender;

  private String textTemplate;

  /**
   * 发送纯文本的用户修改通知邮件.
   */
  public void sendNotificationMail(String userName) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("feigeniu@163.com");
    msg.setTo("feigeniu@163.com");
    msg.setSubject("用户修改通知");

    // 将用户名与当期日期格式化到邮件内容的字符串模板
    String content = String.format(textTemplate, userName, new Date());
    msg.setText(content);

    try {
      mailSender.send(msg);
      if (logger.isInfoEnabled()) {
        logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
      }
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
    }
  }

  public void sendMsg(String formUser, String toUser, String subject, String content) {
	  SimpleMailMessage msg = new SimpleMailMessage();
	    msg.setFrom(formUser);
	    String []users=toUser.split(",");
	    msg.setTo(users);
	    msg.setSubject(subject);

	    msg.setText(content);

	    try {
	      mailSender.send(msg);
	      if (logger.isInfoEnabled()) {
	        logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
	      }
	    } catch (Exception e) {
	      logger.error("发送邮件失败", e);
	    }
  }
    public void sendMsg(String formUser, String[] toUsers, String subject, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(formUser);
        msg.setTo(toUsers);
        msg.setSubject(subject);

        msg.setText(content);

        try {
            mailSender.send(msg);
            if (logger.isInfoEnabled()) {
                logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
            }
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }
    }
  public void sendAttachment(String formUser, String toUser, String subject, String content,
      String attanchmentPath) throws Exception {

    JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
    MimeMessage mailMessage = senderImpl.createMimeMessage();
    // 设置UTF-8编码，否则邮件会乱码
    try {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
      messageHelper.setFrom(formUser);// .setFromUser(formUser);
      messageHelper.setTo(toUser);
      messageHelper.setSubject(subject);
      messageHelper.setText(content, true);// 邮件内容，注意加参数true
      // 附件内容
      // messageHelper.addInline("a", new File("E:/xiezi.jpg"));//插入图片
      // messageHelper.addInline("b", new File("E:/logo.png")); //插入图片
      File file = new File(attanchmentPath);
      // 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
      messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
      mailSender.send(mailMessage);

    } catch (MessagingException e) {
      e.printStackTrace();
      throw new Exception("发送邮件失败!");
    }

  }

    public void sendAttachments(String formUser, String toUser, String subject, String content,
                               String attanchmentPaths) throws Exception {

        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // 设置UTF-8编码，否则邮件会乱码
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            messageHelper.setFrom(formUser);// .setFromUser(formUser);
            String []users=toUser.split(",");
            messageHelper.setTo(users);
           // messageHelper.setTo(toUser);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);// 邮件内容，注意加参数true
            // 附件内容
            // messageHelper.addInline("a", new File("E:/xiezi.jpg"));//插入图片
            // messageHelper.addInline("b", new File("E:/logo.png")); //插入图片
            String attanchmentPath[]=attanchmentPaths.split(";");
            for (int i=0;i<attanchmentPath.length;i++){
                File file = new File(attanchmentPath[i]);
                // 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
                messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
            }

            mailSender.send(mailMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new Exception("发送邮件失败!");
        }

    }
    public void sendAttachments(String formUser, String[] toUser, String subject, String content,String rootPath,
                                String attanchmentPaths) throws Exception {

        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // 设置UTF-8编码，否则邮件会乱码
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            messageHelper.setFrom(formUser);// .setFromUser(formUser);
            messageHelper.setTo(toUser);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);// 邮件内容，注意加参数true
            // 附件内容
            // messageHelper.addInline("a", new File("E:/xiezi.jpg"));//插入图片
            // messageHelper.addInline("b", new File("E:/logo.png")); //插入图片
            String attanchmentPath[]=attanchmentPaths.split(";");
            for (int i=0;i<attanchmentPath.length;i++){
                File file = new File(rootPath+"/email/"+attanchmentPath[i]);
                // 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
                messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
            }

            mailSender.send(mailMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new Exception("发送邮件失败!");
        }

    }
  public void sendPrintNotification(String unitName, String attanchmentPath, long totEpcQty,
      long totSkuQty) throws Exception {
    sendAttachment("support@casesoft.com.cn", "support@casesoft.com.cn", "标签初始化--" + unitName,
        "附件为标签打印生产资料文件：总计 标签数：" + totEpcQty + ", SKU数量:" + totSkuQty, attanchmentPath);
  }

  /**
   * Spring的MailSender.
   */
  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 邮件内容的字符串模板.
   */
  public void setTextTemplate(String textTemplate) {
    this.textTemplate = textTemplate;
  }
}
