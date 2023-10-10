package com.spring.boot.blog.app.service.impl;

import com.spring.boot.blog.app.service.EmailService;
import com.spring.boot.blog.app.utils.NotificationUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final int MAIL_PRIORITY = 1;
    private final JavaMailSender javaMailSender;
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCONDING = "text/html";
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            msg.setFrom(fromEmail);
            msg.setTo(to);
            msg.setText(NotificationUtils.getEmailMessage(name, host, token));
            emailSender.send(msg);
        }catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(MAIL_PRIORITY);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));

            //Add attachments
            FileSystemResource file1 = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/logo-snap-7.PNG"));
            FileSystemResource file2 = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/Internship presentation at DSi.pdf"));
            helper.addAttachment(file1.getFilename(), file1);
            helper.addAttachment(file2.getFilename(), file2);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(MAIL_PRIORITY);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));
            //Add attachments
            FileSystemResource file1 = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/logo-snap-7.PNG"));
            FileSystemResource file2 = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/Internship presentation at DSi.pdf"));
            helper.addInline(contentId(file1.getFilename()), file1);
            helper.addInline(contentId(file2.getFilename()), file2);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            /*context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host, token));*/
            context.setVariables(Map.of("name", name, "url", NotificationUtils.getVerificationUrl(host, token)));
            String text = templateEngine.process("ActivateEmailTemplate", context);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(MAIL_PRIORITY);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            //Add attachments (Optional)

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {

    }
    private String contentId(String filename) {
        return "<" + filename + ">";
    }
    private String getEmailMessage(String name, String host, String token) {
        return "This is working!!";
    }
}
