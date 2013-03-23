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

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.MethodExpressionActionListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class FacesHelper {

    /**
     * Get value for context parameter from web application deployment descriptor.
     * @param parameter Name of parameter.
     * @return The value of parameter.
     */
    public static String getContextParam(String parameter) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getInitParameter(parameter);
    }

    /**
     * Get managed bean by name.
     * @param beanName Name of bean.
     * @return Managed bean instance, can be null.
     */
    public static Object getManagedBean(final String beanName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object bean;
        try {
            ELContext elContext = facesContext.getELContext();
            bean = elContext.getELResolver().getValue(elContext, null, beanName);
        } catch (RuntimeException e) {
            throw new FacesException(e.getMessage(), e);
        }
        return bean;
    }

    /**
     * Evaluate the given value expression #{...} and sets the result to the given value.
     * @param value
     * @param expression
     */
    public static void setValue2ValueExpression(final Object value, final String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression targetExpression = expressionFactory.createValueExpression(elContext, expression, Object.class);
        targetExpression.setValue(elContext, value);
    }

    /**
     * Map a variable to the given value expression #{...}.
     * Uses {@link javax.el.VariableMapper} to assign the expression to the specified variable,
     * so that any reference to that variable will be replaced by the expression in EL evaluations.
     * @param variable
     * @param expression
     */
    public static void mapVariable2ValueExpression(final String variable, final String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression targetExpression = expressionFactory.createValueExpression(elContext, expression, Object.class);
        elContext.getVariableMapper().setVariable(variable, targetExpression);
    }

    /**
     * Get actual HTTP request.
     * @return HttpServletRequest.
     */
    public static HttpServletRequest getHttpServletRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (HttpServletRequest) facesContext.getExternalContext().getRequest();
    }

    public static MethodExpression createMethodExpression(String valueExpression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        MethodExpression methodExpression = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExpressionFactory factory = fc.getApplication().getExpressionFactory();
            methodExpression = factory.createMethodExpression(fc.getELContext(), valueExpression, expectedReturnType, expectedParamTypes);
        } catch (Exception e) {
            String format = String.format("Method expression '%s' could not be created.", valueExpression);
            throw new FacesException(format);
        }
        return methodExpression;
    }

    public static MethodExpressionActionListener createMethodActionListener(String valueExpression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        MethodExpressionActionListener actionListener = null;
        try {
            actionListener = new MethodExpressionActionListener(createMethodExpression(valueExpression, expectedReturnType, expectedParamTypes));
        } catch (Exception e) {
            String format = String.format("Method expression for ActionListener '%s' could not be created.", valueExpression);
            throw new FacesException(format);
        }
        return actionListener;
    }

    /**
     * Get actual HTTP response.
     * @return HttpServletResponse.
     */
    public static HttpServletResponse getHttpServletResponse() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (HttpServletResponse) facesContext.getExternalContext().getResponse();
    }

    /**
     * Get session.
     * @return HttpSession.
     */
    public static HttpSession getHttpSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (HttpSession) facesContext.getExternalContext().getSession(true);
    }

    public static void readParameter() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> keymap = facesContext.getExternalContext().getRequestParameterMap();
        for (String key : keymap.keySet()) {
            System.out.println("Parameter: " + key + " = " + keymap.get(key));
        }
    }

    public static Object writeToSessionMap(String key, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getSessionMap().put(key, value);
    }

    public static Object readSessionMap(String key) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        return map.get(key);
    }

    public static Flash getFlash() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getFlash();
    }

    /**
     * Add a JSF message for a certain component.
     * @param component ID of component to add message to.
     * @param message   Short message.
     * @param detail    Detailed message.
     */
    public static void addFacesMessage(String component, String message, String detail) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(message, detail);
        facesContext.addMessage(component, facesMessage);
    }

    /**
     * Add a JSF message.
     * @param message Short message.
     */
    public static void addFacesMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(message));
    }

    /**
     *
     */
    public static void writeMessage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nur zur Information", "");
        facesContext.addMessage(null, message);
    }

    /**
     * Redirect.
     * @param url
     * @throws IOException
     */
    public static void servletRedirect(String url) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse res = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        res.sendRedirect(url);
    }

    /**
     * Redirect.
     * @param url
     * @throws IOException
     */
    public static void jsfRedirect(String url) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        /*
        NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
        myNav.handleNavigation(facesContext, null, url);
        */
        facesContext.getExternalContext().redirect(url);
    }

    /**
     * See http://stackoverflow.com/questions/5498391/how-to-download-a-file-stored-in-a-database-with-jsf-2-0.
     * @param contentType
     * @param filename
     * @param data
     */
    public static void download(String contentType, String filename, byte[] data) {
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
