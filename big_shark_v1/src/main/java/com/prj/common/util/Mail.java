package com.prj.common.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 
 * 
 * @author HuPeng
 * @version $Id: Mail.java, v 0.1 2015年8月21日 下午2:56:09 HuPeng Exp $
 */
public class Mail {

    String to       = "";                // 收件人
    String from     = "innospace@qq.com"; // 发件人
    String host     = "smtp.qq.com";     // smtp主机
    String username = "innospace@qq.com"; // 用户名
    String password = "cube3cube3";      // 密码
    String subject  = "";                // 邮件主题
    String content  = "";                // 邮件正文

    public Mail() {
    }

    public Mail(String to, String from, String host, String username, String password,
                String subject, String content) {
        this.to = to;
        this.from = from;
        this.host = host;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.content = content;
    }

    public Mail(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 把主题转换为中文
     * 
     * @param strText
     * @return
     */
    public String transferChinese(String strText) {

        try {
            strText = MimeUtility.encodeText(new String(strText.getBytes(), "GB2312"), "GB2312",
                "B");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strText;
    }

    /**
     * 发送邮件
     * 
     * @return 成功返回true，失败返回false
     */
    public boolean sendMail() {
        // 构造mail session
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 构造MimeMessage并设定基本的值，创建消息对象
            MimeMessage msg = new MimeMessage(session);
            // 设置消息内容
            msg.setFrom(new InternetAddress(from));
            // 把邮件地址映射到Internet地址上
            InternetAddress[] address = { new InternetAddress(to) };
            /**
             * setRecipient（Message.RecipientType type, Address
             * address），用于设置邮件的接收者。<br>
             * 有两个参数，第一个参数是接收者的类型，第二个参数是接收者。<br>
             * 接收者类型可以是Message.RecipientType .TO，Message
             * .RecipientType.CC和Message.RecipientType.BCC，TO表示主要接收人，CC表示抄送人
             * ，BCC表示秘密抄送人。接收者与发送者一样，通常使用InternetAddress的对象。
             */
            msg.setRecipients(Message.RecipientType.TO, address);
            // 设置邮件的标题
            //subject = transferChinese(subject);
            msg.setSubject(subject);
            // 构造Multipart
            Multipart mp = new MimeMultipart();

            // 向Multipart添加正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            // 设置邮件内容(纯文本格式)
            // mbpContent.setText(content);
            // 设置邮件内容(HTML格式)
            mbpContent.setContent(content, "text/html;charset=utf-8");
            // 向MimeMessage添加（Multipart代表正文）
            mp.addBodyPart(mbpContent);
            // 向Multipart添加MimeMessage
            msg.setContent(mp);
            // 设置邮件发送的时间。
            msg.setSentDate(new Date());
            // 发送邮件
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Getter method for property <tt>to</tt>.
     * 
     * @return property value of to
     */
    public String getTo() {
        return to;
    }

    /**
     * Setter method for property <tt>to</tt>.
     * 
     * @param to value to be assigned to property to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Getter method for property <tt>from</tt>.
     * 
     * @return property value of from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setter method for property <tt>from</tt>.
     * 
     * @param from value to be assigned to property from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Getter method for property <tt>host</tt>.
     * 
     * @return property value of host
     */
    public String getHost() {
        return host;
    }

    /**
     * Setter method for property <tt>host</tt>.
     * 
     * @param host value to be assigned to property host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Getter method for property <tt>username</tt>.
     * 
     * @return property value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for property <tt>username</tt>.
     * 
     * @param username value to be assigned to property username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for property <tt>password</tt>.
     * 
     * @return property value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for property <tt>password</tt>.
     * 
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>subject</tt>.
     * 
     * @return property value of subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Setter method for property <tt>subject</tt>.
     * 
     * @param subject value to be assigned to property subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Getter method for property <tt>content</tt>.
     * 
     * @return property value of content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter method for property <tt>content</tt>.
     * 
     * @param content value to be assigned to property content
     */
    public void setContent(String content) {
        this.content = content;
    }

}