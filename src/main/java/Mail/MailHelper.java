package Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
        <dependency>
            <groupId>javax.mail</groupId>
            <artifactId>mail</artifactId>
            <version>1.4.7</version>
        </dependency>
* */
public class MailHelper {

    String eamil = "";
    String pwd = "";

    public MailHelper(String email, String pwd) {
        this.eamil = email;
        this.pwd = pwd;
    }

    public void sendEmail(List<String> toList, String subject, String body, boolean isHTML) throws MessagingException {

        String host = "smtp.qq.com";//这是QQ邮箱的smtp服务器地址
        String port = "465"; //端口号
        /*
         *Properties是一个属性对象，用来创建Session对象
         */
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");//"true"
        props.setProperty("mail.smtp.connectiontimeout", "5000");

        /*
         *Session类定义了一个基本的邮件对话。
         */
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //登录用户名密码
                return new PasswordAuthentication(eamil, pwd);
            }
        });
        session.setDebug(true);
        /*
         *Transport类用来发送邮件。
         *传入参数smtp，transport将自动按照smtp协议发送邮件。
         */
        Transport transport = session.getTransport("smtp");//"smtps"
        transport.connect(host, eamil, pwd);
        /*
         *Message对象用来储存实际发送的电子邮件信息
         */
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        //消息发送者接收者设置(发件地址，昵称)，收件人看到的昵称是这里设定的
        message.setFrom(new InternetAddress(eamil));

        ArrayList<InternetAddress> list = new ArrayList<InternetAddress>();
        for (String item : toList) {
            list.add(new InternetAddress(item));
        }
        InternetAddress[] arr = new InternetAddress[list.size()];
        message.addRecipients(Message.RecipientType.TO, list.toArray(arr));
        message.saveChanges();

        if (isHTML) {
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(body, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            message.setContent(mainPart);
        } else {
            //设置邮件内容及编码格式
            //后一个参数可以不指定编码，如"text/plain"，但是将不能显示中文字符
            message.setContent(body, "text/plain;charset=UTF-8");
        }

        //发送
        //transport.send(message);
        Transport.send(message);
        transport.close();
    }
}
