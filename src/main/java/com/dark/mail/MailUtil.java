package com.dark.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by darkxue on 18/11/16.
 */
public class MailUtil {
    // private static Properties props;
    private static Session session;

    private static MimeMessage msg;

    private static MimeMultipart msgMultipart;

    // configuration params
    private static String from;

    private static String acount;

    private static String passwd;

    private static String host;

    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static void main(String[] args) {
        List<String> recipients = new ArrayList<String>();
        recipients.add("xt0000201101@126.com");
        MailUtil.send(recipients, "a subject", "This is test content.");
    }

    /**
     * 初始化，发送邮件准备过程
     *
     * @throws Exception
     */
    public static void init() throws Exception {
        Properties props = new Properties();
        InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("mails.properties");
        props.load(in);

        from = props.getProperty("from");
        acount = props.getProperty("acount");
        passwd = props.getProperty("passwd");
        host = props.getProperty("host");

        session = Session.getInstance(props);
        session.setDebug(false);
        msg = new MimeMessage(session);
        msgMultipart = new MimeMultipart("mixed");
        msg.setContent(msgMultipart);
    }

    /**
     * 添加邮件内容.
     *
     * @param html
     * @throws MessagingException
     */
    public static void addContent(String html) throws MessagingException {
        MimeBodyPart content = new MimeBodyPart();
        MimeMultipart bodyMultipart = new MimeMultipart("alternative");
        content.setContent(bodyMultipart);
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/plain;charset=utf-8");
        bodyMultipart.addBodyPart(htmlPart);
        msgMultipart.addBodyPart(content);
    }

    /**
     * 添加附件
     *
     * @param bytes
     * @param name
     * @param contentType
     * @throws MessagingException
     */
    public static void addAttachment(byte[] bytes, String name, String contentType)
            throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        part.setDisposition(MimeBodyPart.ATTACHMENT);
        ByteArrayDataSource orgSrc = new ByteArrayDataSource(bytes, contentType);
        DataHandler handler = new DataHandler(orgSrc);
        part.setDataHandler(handler);
        part.setFileName(name);
        part.setContentID("<" + name + ">");
        msgMultipart.addBodyPart(part);
    }

    /**
     * 群发，带附件
     *
     * @param receivers
     * @param subject
     * @param contentHtml
     * @param attach
     * @param name
     * @param contentType
     */
    public static void send(List<String> receivers, String subject, String contentHtml,
                            byte[] attach, String name, String contentType) {
        try {
            addAttachment(attach, name, contentType);
            send(receivers, subject, contentHtml);
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 群发邮件
     *
     * @param receivers
     * @param subject
     * @param contentHtml
     */
    public static void send(List<String> receivers, String subject, String contentHtml) {

        InternetAddress[] receiversAddress = new InternetAddress[receivers.size()];
        int i = 0;
        for (String to : receivers) {
            try {
                receiversAddress[i++] = new InternetAddress(to);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        Transport transport = null;
        try {
            init();
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(subject);
            addContent(contentHtml);
            transport = session.getTransport("smtp");
            transport.connect(host, acount, passwd);
            transport.sendMessage(msg, receiversAddress);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (transport != null) transport.close();
            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 给指定地址发邮件
     *
     * @param toAddr
     * @param subject
     * @param content
     */
    public static void send(String toAddr, String subject, String content) {
        try {
            InternetAddress to = new InternetAddress(toAddr);
            Transport transport = null;
            try {
                init();
                msg.setFrom(new InternetAddress(from));
                msg.setSubject(subject);
                addContent(content);
                msg.setRecipient(Message.RecipientType.TO, to);
                transport = session.getTransport("smtp");
                transport.connect(host, acount, passwd);
                transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    if (transport != null) transport.close();
                } catch (MessagingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (AddressException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
