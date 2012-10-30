/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 29.10.12 10:45
 */

package eu.artofcoding.beetlejuice.email;

import eu.artofcoding.beetlejuice.api.BeetlejuiceConstant;
import eu.artofcoding.beetlejuice.entity.EmailEntity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Set;

public class Postman {

    private Session session;

    private MailAuth mailAuth;

    /**
     * Constructor.
     * @param session  A JavaMail session.
     */
    public Postman(Session session) {
        this.session = session;
    }

    /**
     * Constructor.
     * @param session  A JavaMail session.
     * @param mailAuth MailAuth containing credentials.
     */
    public Postman(Session session, MailAuth mailAuth) {
        this.session = session;
        this.mailAuth = mailAuth;
    }

    /**
     * Factory method to create a Postman instance.
     * @param smtpProtocol
     * @param server
     * @param port
     * @param mailAuth
     * @return
     */
    public static Postman createPostman(SmtpProtocol smtpProtocol, String server, int port, MailAuth mailAuth) {
        // Create properties
        Properties p = new Properties();
        // Host and port
        p.setProperty("mail.smtp.host", server);
        p.setProperty("mail.smtp.port", String.valueOf(port));
        // SMTP protocol
        p.setProperty("mail.transport.protocol", smtpProtocol.toString());
        if (SmtpProtocol.SMTPS == smtpProtocol) {
            p.setProperty("mail.smtp.tls", BeetlejuiceConstant.TRUE);
            p.setProperty("mail.smtp.starttls.enable", BeetlejuiceConstant.TRUE);
            p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));
        } else {
            p.setProperty("mail.smtp.tls", BeetlejuiceConstant.FALSE);
            p.setProperty("mail.smtp.starttls.enable", BeetlejuiceConstant.FALSE);
            p.setProperty("mail.smtp.socketFactory.class", "");
            p.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));
        }
        // Create session
        Session session;
        // SMTP AUTH
        if (null != mailAuth) {
            p.setProperty("mail.smtp.auth", BeetlejuiceConstant.TRUE);
            session = Session.getDefaultInstance(p, mailAuth.getAuthenticator());
        } else {
            p.setProperty("mail.smtp.auth", BeetlejuiceConstant.FALSE);
            session = Session.getDefaultInstance(p, null);
        }
        session.setDebug(false);
        //
        Postman postman = new Postman(session, mailAuth);
        return postman;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Send an email: data is encapsulated in EmailEntity, email is sent through Session.
     * @param email EmailEntity, Email as an entity bean.
     * @throws MessagingException
     */
    public void send(EmailEntity email) throws MessagingException {
        // Create message
        MimeMessage m = new MimeMessage(session);
        // From
        Address from = new InternetAddress(email.getFromAddress());
        m.setFrom(from);
        // To
        for (String toAddress : email.getToAddress().split(BeetlejuiceConstant.COMMA)) {
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        }
        // Subject
        m.setSubject(email.getSubject());
        m.setSentDate(new java.util.Date());
        // Set content and MIME type
        m.setContent(email.getBody(), email.getContentType());
        //
        m.saveChanges();
        // Send mail
        Transport.send(m);
        // Set successfully-sent-flag
        email.setSentSuccessfully(true);
        email.setSentDate(new Timestamp(m.getSentDate().getTime()));
    }

    /**
     * Send an email by populating an EmailEntity and sending it to a JMS destination.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The body.
     * @param contentType The content type, e.g. "text/plain" or "text/html".
     */
    public void sendMail(String fromAddress, Set<String> recipient, String subject, String body, String contentType) throws MessagingException {
        // Create Email entity instance and populate it with data
        EmailEntity email = new EmailEntity();
        email.setFromAddress(fromAddress);
        StringBuilder toAddressBuilder = new StringBuilder();
        for (String to : recipient) {
            toAddressBuilder.append(to).append(BeetlejuiceConstant.COMMA);
        }
        email.setToAddress(toAddressBuilder.toString());
        email.setSubject(subject);
        email.setBody(body);
        email.setContentType(contentType);
        // Set object in message and send it
        send(email);
    }

    /**
     * Convenience method for sending a plain text message.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The plain text body.
     */
    public void sendPlainTextMail(String fromAddress, Set<String> recipient, String subject, String body) throws MessagingException {
        sendMail(fromAddress, recipient, subject, body, "text/plain");
    }

    /**
     * Convenience method for sending a HTML message.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The plain text body.
     */
    public void sendHtmlMail(String fromAddress, Set<String> recipient, String subject, String body) throws MessagingException {
        sendMail(fromAddress, recipient, subject, body, "text/html; charset=UTF-8");
    }

}
