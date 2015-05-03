/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 15.02.13 12:25
 */

package eu.artofcoding.beetlejuice.web.security;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import java.net.URL;

public class WebSecurityFactory {

    @Produces
    @WebSecurity
    public Sanitizer create(final InjectionPoint injectionPoint) {
        final Annotated annotated = injectionPoint.getAnnotated();
        final WebSecurity webSecurity = annotated.getAnnotation(WebSecurity.class);
        final String antisamyXml = webSecurity.antisamyXml();
        final URL antisamyXmlUrl = WebSecurity.class.getResource(String.format("antisamy/%s", antisamyXml));
        return new AntisamySanitizerImpl(antisamyXmlUrl);
    }

}
