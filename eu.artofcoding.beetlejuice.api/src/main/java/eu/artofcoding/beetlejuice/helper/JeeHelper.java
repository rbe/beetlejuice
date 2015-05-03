/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 7/5/12 8:42 AM
 */

package eu.artofcoding.beetlejuice.helper;

import javax.servlet.ServletContext;

public final class JeeHelper {

    private JeeHelper() {
        throw new AssertionError();
    }

    /**
     * Log a message and associated exception to the servlet context application log.
     * @param message Message to be logged
     * @param throwable Exception to be logged
     */
    public static void log(ServletContext servletContext, String message, Throwable throwable) {
        if (servletContext != null) {
            if (null != throwable) {
                servletContext.log(message, throwable);
            } else {
                servletContext.log(message);
            }
        } else {
            throwable.printStackTrace(System.out);
        }
    }

    /**
     * Convenience method for log(ServletContext, String, null), without Throwable.
     * @param servletContext
     * @param message
     */
    public static void log(ServletContext servletContext, String message) {
        log(servletContext, message, null);
    }

}
