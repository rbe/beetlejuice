/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 24.11.12 12:28
 */

package eu.artofcoding.beetlejuice.email.cdi;

import eu.artofcoding.beetlejuice.email.MailAuth;
import eu.artofcoding.beetlejuice.email.Postman;
import eu.artofcoding.beetlejuice.email.PostmanImpl;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.mail.Session;
import java.util.Properties;

import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.S_FALSE;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.S_TRUE;
import static eu.artofcoding.beetlejuice.email.cdi.TransportType.SIMPLE;
import static eu.artofcoding.beetlejuice.email.cdi.TransportType.SSL_TLS;

public class PostmanFactory {

    private MailAuth createMailAuth(QPostman qPostman, Properties p) {
        MailAuth mailAuth = null;
        if (null != qPostman.username()) {
            p.setProperty("mail.smtp.auth", S_TRUE);
            mailAuth = new MailAuth(qPostman.username(), qPostman.password());
        } else {
            p.setProperty("mail.smtp.auth", S_FALSE);
        }
        return mailAuth;
    }

    private Session createSession(Properties p, MailAuth mailAuth) {
        // Create session
        Session session;
        if (null != mailAuth) {
            session = Session.getInstance(p, mailAuth.getAuthenticator());
        } else {
            session = Session.getInstance(p, null);
        }
        session.setDebug(false);
        return session;
    }

    /**
     * Factory method to create a simple QPostman instance.
     * @return {@link eu.artofcoding.beetlejuice.email.Postman} instance.
     */
    @Produces
    @QPostman(transportType = SIMPLE)
    public Postman createSimplePostman(InjectionPoint injectionPoint) {
        // CDI
        Annotated annotated = injectionPoint.getAnnotated();
        QPostman qPostman = annotated.getAnnotation(QPostman.class);
        if (!qPostman.sessionProvided()) {
            // Create properties
            Properties p = new Properties();
            // Host and port
            p.setProperty("mail.smtp.host", qPostman.server());
            p.setProperty("mail.smtp.port", String.valueOf(qPostman.port()));
            // SMTP protocol
            p.setProperty("mail.transport.protocol", "smtp");
            p.setProperty("mail.smtp.tls", S_FALSE);
            p.setProperty("mail.smtp.starttls.enable", S_FALSE);
            p.setProperty("mail.smtp.socketFactory.class", "");
            p.setProperty("mail.smtp.socketFactory.port", String.valueOf(qPostman.port()));
            // SMTP AUTH
            MailAuth mailAuth = createMailAuth(qPostman, p);
            // Create session
            Session session = createSession(p, mailAuth);
            // Create Postman instance
            return new PostmanImpl(session, mailAuth);
        } else {
            // Create Postman instance
            return new PostmanImpl(null, null);
        }
    }

    /**
     * Factory method to create a secure QPostman instance.
     * @return {@link eu.artofcoding.beetlejuice.email.Postman} instance.
     */
    @Produces
    @QPostman(transportType = SSL_TLS)
    public Postman createSecurePostman(InjectionPoint injectionPoint) {
        // CDI
        Annotated annotated = injectionPoint.getAnnotated();
        QPostman qPostman = annotated.getAnnotation(QPostman.class);
        if (!qPostman.sessionProvided()) {
            // Create properties
            Properties p = new Properties();
            // Host and port
            p.setProperty("mail.smtp.host", qPostman.server());
            p.setProperty("mail.smtp.port", String.valueOf(qPostman.port()));
            // SMTP protocol
            p.setProperty("mail.transport.protocol", "smtps");
            p.setProperty("mail.smtp.tls", S_TRUE);
            p.setProperty("mail.smtp.starttls.enable", S_TRUE);
            p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.setProperty("mail.smtp.socketFactory.port", String.valueOf(qPostman.port()));
            // SMTP AUTH
            MailAuth mailAuth = createMailAuth(qPostman, p);
            // Create session
            Session session = createSession(p, mailAuth);
            // Create QPostman instance
            return new PostmanImpl(session, mailAuth);
        } else {
            // Create QPostman instance
            return new PostmanImpl(null, null);
        }
    }

}
