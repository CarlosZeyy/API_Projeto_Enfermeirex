package dev.carlosmoises.projeto.enferm.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String myEmailSender;

    public void sendPasswordResetEmail(String to, String subject, String body, String username) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(myEmailSender);

        Context context = new Context();

        context.setVariable("username", username);
        context.setVariable("resetLink", body);

        String finalHTML = templateEngine.process("email-recuperacao", context);

        helper.setText(finalHTML, true);

        mailSender.send(mimeMessage);
    }
}
