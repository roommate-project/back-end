package roommateproject.roommatebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
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
            Random random = new Random();
            int randomNumber = random.nextInt(2000000000) % 1000000;
            String sendRandomNumber = Integer.toString(randomNumber);
            while(sendRandomNumber.length() < 6)sendRandomNumber += "0";
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(sendFrom));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            mimeMessage.setSubject("[roommate] 이메일 확인");
            mimeMessage.setContent(sendRandomNumber,"text/html;charset=utf-8");
            Transport.send(mimeMessage);
            return Integer.toString(randomNumber);
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
}
