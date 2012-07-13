/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 7/10/12 4:19 PM
 */

package eu.artofcoding.beetlejuice.entity;

import eu.artofcoding.beetlejuice.api.GenericEntity;

import java.util.Calendar;

public class TemplateEntity implements GenericEntity {

    private Long id;

    private Long version;

    private Calendar lastModified;

    private String templateName;

    private String templateType;

    private String content;

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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateEntity that = (TemplateEntity) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (templateName != null ? !templateName.equals(that.templateName) : that.templateName != null) return false;
        if (templateType != null ? !templateType.equals(that.templateType) : that.templateType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (templateName != null ? templateName.hashCode() : 0);
        result = 31 * result + (templateType != null ? templateType.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result.intValue();
    }

}
