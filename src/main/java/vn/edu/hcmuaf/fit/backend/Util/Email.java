package vn.edu.hcmuaf.fit.backend.Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Email {
    static final String form = "tilemarket2022@gmail.com";
    static final String password = "auogzmiqdjuyxbji";

    public static void sendMail(String to, String content, String subject) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2"); //TLS 587 SSL 465
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        //create Authentication
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(form, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        //Create Message
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-tyoe", "text/HTML; charset=UTF-8");
            Address address = new InternetAddress(form, "Tile Market");
            msg.setFrom(address);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setContent(content, "text/HTML; charset=UTF-8");

            //send mail
            Transport.send(msg);
            System.out.println("Gửi Email thành công");
        } catch (MessagingException e) {
            System.out.println("Gửi Email không thành công");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Gửi Email không thành công");
            throw new RuntimeException(e);
        }
    }

}



