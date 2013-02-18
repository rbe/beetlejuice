/*
 * beetlejuice
 * beetlejuice-web-jsfacesContextomponents
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 22.11.12 10:18
 */
package eu.artofcoding.beetlejuice.web.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class FacesHelper {

    private Logger logger;

    private static final FacesHelper INSTANCE = new FacesHelper();

    private static FacesContext facesContext;

    public static FacesHelper getInstance(FacesContext facesContext) {
        FacesHelper.facesContext = facesContext;
        return INSTANCE;
    }

    private FacesHelper() {
        logger = Logger.getLogger(FacesHelper.class.getName());
    }

    /**
     * Get value for context parameter from web application deployment descriptor.
     * @param parameter Name of parameter.
     * @return The value of parameter.
     */
    public String getContextParam(String parameter) {
        return facesContext.getExternalContext().getInitParameter(parameter);
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) facesContext.getExternalContext().getRequest();
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) facesContext.getExternalContext().getResponse();
    }

    public HttpSession getHttpSession() {
        return (HttpSession) facesContext.getExternalContext().getSession(true);
    }

    public void readParameter() {
        Map<String, String> keymap = facesContext.getExternalContext().getRequestParameterMap();
        for (String key : keymap.keySet()) {
            System.out.println("Parameter: " + key + " = " + keymap.get(key));
        }
    }

    public Object writeToSessionMap(String key, Object value) {
        return facesContext.getExternalContext().getSessionMap().put(key, value);
    }

    public Object readSessionMap(String key) {
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        return map.get(key);
    }

    /**
     * Add a JSF message for a certain component.
     * @param component ID of component to add message to.
     * @param message   Short message.
     * @param detail    Detailed message.
     */
    public void addFacesMessage(String component, String message, String detail) {
        FacesMessage facesMessage = new FacesMessage(message, detail);
        facesContext.addMessage(component, facesMessage);
    }

    /**
     * Add a JSF message.
     * @param message Short message.
     */
    public void addFacesMessage(String message) {
        facesContext.addMessage(null, new FacesMessage(message));
    }

    public void writeMessage() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nur zur Information", "");
        facesContext.addMessage(null, message);
    }

    /**
     * Redirect.
     * @param url
     * @throws IOException
     */
    public void servletRedirect(String url) throws IOException {
        HttpServletResponse res = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        res.sendRedirect(url);
    }

    /**
     * Redirect.
     * @param url
     * @throws IOException
     */
    public void jsfRedirect(String url) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        /*
        NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
        myNav.handleNavigation(facesContext, null, url);
        */
        facesContext.getExternalContext().redirect(url);
    }

    /**
     * http://stackoverflow.com/questions/5498391/how-to-download-a-file-stored-in-a-database-with-jsf-2-0
     */
    public void download(String contentType, String filename, byte[] data) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        // Check content type
        if (null == contentType) {
            contentType = "application/x-octet-stream";
        }
        externalContext.setResponseHeader("Content-Type", contentType);
        externalContext.setResponseHeader("Content-Length", String.valueOf(data.length));
        if (null == filename) {
            String[] split = contentType.split("/");
            String s;
            if (split.length >= 2) {
                s = String.format("file.%s", split[1]);
            } else {
                s = "file";
            }
            filename = String.format("This_is_a_%s", s);
        }
        externalContext.setResponseHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        try {
            externalContext.getResponseOutputStream().write(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        facesContext.responseComplete();
    }

}
