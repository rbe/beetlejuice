/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 24.11.12 12:21
 */

package eu.artofcoding.beetlejuice.email;

import eu.artofcoding.beetlejuice.entity.Email;
import eu.artofcoding.beetlejuice.entity.MimeType;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Set;

public interface Postman extends Serializable {

    /**
     * Get JavaMail Session.
     * @return {@link Session}.
     */
    Session getSession();

    /**
     * Set JavaMail Session.
     * @param session {@link Session}.
     */
    void setSession(Session session);

    /**
     * Add a part to the email.
     * @param content  Content, e.g. {@link String} or {@link java.io.File}.
     * @param mimeType {@link MimeType}.
     * @param filename Filename, can be null.
     * @throws MessagingException
     */
    void addPart(Object content, MimeType mimeType, String filename) throws MessagingException;

    /**
     * Set body part of email.
     * @param content  Content, e.g. {@link String} or {@link java.io.File}.
     * @param mimeType {@link MimeType}.
     * @throws MessagingException
     */
    void setBody(Object content, MimeType mimeType) throws MessagingException;

    /**
     * Create a message.
     * @param email {@link Email} entity.
     * @return {@link MimeMessage}
     * @throws MessagingException
     */
    MimeMessage createMimeMessage(Email email) throws MessagingException;

    /**
     * Create a message.
     * @param fromAddress Sender address.
     * @param toAddress   Recipient address(es).
     * @param subject     Subject.
     * @param content     Body content, e.g. String or byte[].
     * @param mimeType    Type of body content, see {@link MimeType}.
     * @return {@link MimeMessage}
     * @throws MessagingException
     */
    MimeMessage createMimeMessage(String fromAddress, String[] toAddress, String subject, Object content, MimeType mimeType) throws MessagingException;

    /**
     * Create a message.
     * @param fromAddress Sender address.
     * @param toAddress   Recipient address(es), comma separated.
     * @param subject     Subject.
     * @param content     Body content, e.g. String or byte[].
     * @param mimeType    Type of body content, see {@link MimeType}.
     * @return {@link MimeMessage}
     * @throws MessagingException
     */
    MimeMessage createMimeMessage(String fromAddress, String toAddress, String subject, Object content, MimeType mimeType) throws MessagingException;

    /**
     * Send an email: data is encapsulated in Email, email is sent through {@link javax.mail.Session}.
     * @param email Email, Email as an entity bean.
     * @throws MessagingException
     */
    void sendMail(Email email) throws MessagingException;

    /**
     * Send an email by populating an Email and sending it to a JMS destination.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The body.
     * @param contentType The content type, e.g. "text/plain" or "text/html".
     */
    void sendMail(String fromAddress, Set<String> recipient, String subject, String body, MimeType contentType) throws MessagingException;

    /**
     * Convenience method for sending a plain text message.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The plain text body.
     */
    void sendPlainTextMail(String fromAddress, Set<String> recipient, String subject, String body) throws MessagingException;

    /**
     * Convenience method for sending a HTML message.
     * @param fromAddress Sender address.
     * @param recipient   Recipient address(es).
     * @param subject     The subject.
     * @param body        The plain text body.
     */
    void sendHtmlMail(String fromAddress, Set<String> recipient, String subject, String body) throws MessagingException;

}
