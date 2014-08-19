/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 23.11.12 17:28
 */

package eu.artofcoding.beetlejuice.entity;

import java.io.Serializable;

public class Attachment implements Serializable {

    private MimeType mimeType;

    private String filename;

    private Object content;

    public Attachment(MimeType mimeType, String filename, Object content) {
        this.mimeType = mimeType;
        this.filename = filename;
        this.content = content;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public Object getContent() {
        return content;
    }

}
