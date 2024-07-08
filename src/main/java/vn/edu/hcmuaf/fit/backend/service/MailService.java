package vn.edu.hcmuaf.fit.backend.service;

import vn.edu.hcmuaf.fit.backend.Util.MailStructure;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailService {
    static String fromName = "Group two Corporation";
    static String fromEmail = "fitexamnlu@gmail.com";
    static String password = "eyeypjafwcjpmmvg";

    public static void sendMail(String toEmail, MailStructure mailStructure) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Create Authentication
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // Create Message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(mailStructure.getSubject(), "UTF-8");
            message.setContent(mailStructure.getContent(), "text/html; charset=utf-8");

            // Send mail
            Transport.send(message);
            System.out.println("Gửi Email thành công");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Gửi Email không thành công");
        }
    }

}
