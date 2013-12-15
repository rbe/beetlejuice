/*
 * beetlejuice
 * beetlejuice-xml
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 18.02.13 19:49
 */

package eu.artofcoding.beetlejuice.xml;

public class JaxbHelperException extends Exception {

    public JaxbHelperException() {
        super();
    }

    public JaxbHelperException(String message) {
        super(message);
    }

    public JaxbHelperException(String message, Throwable cause) {
        super(message, cause);
    }

    public JaxbHelperException(Throwable cause) {
        super(cause);
    }

    protected JaxbHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
