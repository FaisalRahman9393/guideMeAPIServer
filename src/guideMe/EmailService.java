package guideMe;
// File Name SendHTMLEmail.java
//
//Username:  guidemelancaster@gmail.com
//Pass:RosesAreRed

/**
 * This is an email service that will be used by the server
 *
 * It will send out an email with forgotten passwords
 */

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    public EmailService(String sendTo, String messageToSend) {

        //String sendTO = "faze93@live.com";
        //String messageToSend = "hello how are yaaaa";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("guidemelancaster@gmail.com","RosesAreRed");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("guidemelancaster@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendTo));
            message.setSubject("Testing Subject");
            message.setText(messageToSend);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}