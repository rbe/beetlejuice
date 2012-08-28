/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 28.08.12 14:11
 */

package eu.artofcoding.beetlejuice.web;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("EncodingFilter.doFilter 1: request="+servletRequest.getCharacterEncoding()+" response="+servletResponse.getCharacterEncoding());
        servletRequest.setCharacterEncoding(ENCODING);
        servletResponse.setCharacterEncoding(ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
        //System.out.println("EncodingFilter.doFilter 2: request="+servletRequest.getCharacterEncoding()+" response="+servletResponse.getCharacterEncoding());
    }

    @Override
    public void destroy() {
    }

}
