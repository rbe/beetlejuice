/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 25.03.13 08:35
 */

package eu.artofcoding.beetlejuice.api;

public class BeetlejuiceException extends Exception {

    public BeetlejuiceException() {
    }

    public BeetlejuiceException(String message) {
        super(message);
    }

    public BeetlejuiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeetlejuiceException(Throwable cause) {
        super(cause);
    }

    public BeetlejuiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
