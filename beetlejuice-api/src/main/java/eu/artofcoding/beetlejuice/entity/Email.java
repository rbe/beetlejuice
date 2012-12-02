/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 29.10.12 13:28
 */

package eu.artofcoding.beetlejuice.entity;

import eu.artofcoding.beetlejuice.api.persistence.GenericEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Email implements GenericEntity {

    private Long id;

    private Long version;

    private Calendar lastModified;

    private String fromAddress;

    private String toAddress;

    private String subject;

    private String body;

    private MimeType mimeType;

    private List<Attachment> attachments = new ArrayList<>();

    private Timestamp sentDate;

    private boolean sentSuccessfully;

    //<editor-fold desc="Getter and Setter">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public void updateLastModified() {
        setLastModified(Calendar.getInstance());
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    public boolean isSentSuccessfully() {
        return sentSuccessfully;
    }

    public void setSentSuccessfully(boolean sentSuccessfully) {
        this.sentSuccessfully = sentSuccessfully;
    }

    //</editor-fold>

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email that = (Email) o;

        if (id != that.id) return false;
        if (sentSuccessfully != that.sentSuccessfully) return false;
        if (version != that.version) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
        if (fromAddress != null ? !fromAddress.equals(that.fromAddress) : that.fromAddress != null) return false;
        if (sentDate != null ? !sentDate.equals(that.sentDate) : that.sentDate != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (toAddress != null ? !toAddress.equals(that.toAddress) : that.toAddress != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + version;
        result = 31 * result + (fromAddress != null ? fromAddress.hashCode() : 0);
        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (sentDate != null ? sentDate.hashCode() : 0);
        result = 31 * result + (sentSuccessfully ? 1 : 0);
        return result.intValue();
    }

    @Override
    public String toString() {
        return "Email{" +
                "fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }

}
