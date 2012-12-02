/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 24.11.12 13:18
 */

package eu.artofcoding.beetlejuice.email.cdi;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, TYPE})
public @interface QPostman {

    boolean sessionProvided() default false;
    
    TransportType transportType() default TransportType.SIMPLE;

    String server() default "localhost";

    int port() default 25;

    String username() default "";

    String password() default "";

}
