package roommateproject.roommatebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

@Service @Slf4j
public class SendMailService {
    @Value("${spring.mail.username}")
    private String sendFrom;
    @Value("${spring.mail.password}")
    private String password;

    public String sendEmail(String sendTo) {
        String regx = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regx);
        if(!pattern.matcher(sendTo).matches()){
            return "fail";
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendFrom, password);
            }
        });
        session.setDebug(true);
        try {
            String uuid = UUID.randomUUID().toString();
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(sendFrom));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            mimeMessage.setSubject("[roommate] 이메일 확인");
            mimeMessage.setContent(uuid,"text/html;charset=utf-8");
            Transport.send(mimeMessage);
            return uuid;
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
}
