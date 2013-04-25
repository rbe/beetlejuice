/*
 * beetlejuice
 * beetlejuice-contact
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 12.04.13 15:27
 */

package eu.artofcoding.beetlejuice.contact;

public class ContactException extends Exception {

    public ContactException(Throwable cause) {
        super(cause);
    }

    public ContactException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactException(String message) {
        super(message);
    }

    public ContactException() {
    }

}
