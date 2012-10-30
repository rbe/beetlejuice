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
import java.util.Calendar;

public class EmailEntity implements GenericEntity {

    private Long id;

    private Long version;

    private Calendar lastModified;

    private String fromAddress;

    private String toAddress;

    private String subject;

    private String body;

    private String contentType;

    private Timestamp sentDate;

    private boolean sentSuccessfully;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailEntity that = (EmailEntity) o;

        if (id != that.id) return false;
        if (sentSuccessfully != that.sentSuccessfully) return false;
        if (version != that.version) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
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
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (sentDate != null ? sentDate.hashCode() : 0);
        result = 31 * result + (sentSuccessfully ? 1 : 0);
        return result.intValue();
    }

}
