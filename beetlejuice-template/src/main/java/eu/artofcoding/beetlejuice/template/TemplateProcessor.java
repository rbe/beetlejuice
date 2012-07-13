/*
 * app1
 * app1ea-cdi
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 6/13/12 6:41 PM
 */
package eu.artofcoding.beetlejuice.template;

import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This template processor acts as a facade to FreeMarker template engine.
 */
public class TemplateProcessor {

    /**
     * FreeMarker configuration.
     */
    private Configuration configuration;

    /**
     * Template loader.
     */
    private List<TemplateLoader> templateLoaders;

    /**
     * Constructor.
     */
    public TemplateProcessor() {
        templateLoaders = new ArrayList<>();
        // Create FreeMarker configuration
        configuration = new Configuration();
        // Specify how templates will see the data-model. This is an advanced topic...
        configuration.setObjectWrapper(new DefaultObjectWrapper());
    }

    /**
     * Add directories to use for templates.
     * @param templateURL An array of URLs.
     * @throws URISyntaxException
     * @throws IOException
     */
    public void addTemplateLoader(URL[] templateURL) throws URISyntaxException, IOException {
        if (null != templateURL) {
            List<FileTemplateLoader> fileTemplateLoaders = new ArrayList<>(templateURL.length);
            for (URL url : templateURL) {
                System.out.println("adding " + url.toURI().toASCIIString());
                fileTemplateLoaders.add(new FileTemplateLoader(new File(url.toURI())));
            }
            templateLoaders.addAll(fileTemplateLoaders);
        }
    }

    /**
     * Add classes (optional with relative paths) to use for templates.
     * @param classes Map with key=Class, value=String containing a path, see {@link ClassTemplateLoader}.
     */
    public void addTemplateLoader(Map<Class, String> classes) {
        if (null != classes) {
            List<ClassTemplateLoader> classTemplateLoaders = new ArrayList<ClassTemplateLoader>(classes.size());
            for (Class c : classes.keySet()) {
                classTemplateLoaders.add(new ClassTemplateLoader(c, classes.get(c)));
            }
            templateLoaders.addAll(classTemplateLoaders);
        }
    }

    /**
     * Add a servlet context.
     * @param servletContext The servlet context.
     * @param path           Base path, can be null.
     */
    public void addTemplateLoader(ServletContext servletContext, String path) {
        if (null != path) {
            templateLoaders.add(new WebappTemplateLoader(servletContext, path));
        } else {
            templateLoaders.add(new WebappTemplateLoader(servletContext));
        }
    }

    /**
     * Just add a TemplateLoader.
     * @param templateLoader
     */
    public void addTemplateLoader(TemplateLoader templateLoader) {
        templateLoaders.add(templateLoader);
    }

    /**
     * Create a list with all previously added template loaders and add them to configuration.
     */
    private void makeTemplateLoader() {
        MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
        configuration.setTemplateLoader(multiTemplateLoader);
    }

    /**
     * Render template (UTF-8), output will be accessible through provided Writer instance.
     * @param templateName
     * @param root
     * @param out
     * @throws IOException
     * @throws TemplateException
     */
    public void renderTemplate(String templateName, Locale locale, Map<String, Object> root, Writer out) throws IOException, TemplateException {
        makeTemplateLoader();
        Template temp = configuration.getTemplate(templateName, locale, "UTF-8");
        temp.process(root, out);
    }

    /**
     * Render a template, see {@link #renderTemplate(String, java.util.Locale, java.util.Map, java.io.Writer)} and return a String.
     * @param templateName
     * @param root
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String renderTemplateToString(String templateName, Locale locale, Map<String, Object> root) throws IOException, TemplateException {
        Writer o = new StringWriter();
        renderTemplate(templateName, locale, root, o);
        o.flush();
        return o.toString();
    }

    /*
    public static void main(String[] args) throws Exception {
        // Create the root hash
        final Map<String, Object> root = new HashMap<String, Object>();
        root.put("user", "Big Joe");
        root.put("registrationUrl", "http://www.example.com/registration/complete/abc123");
        //
        URL[] templateDirectory = new URL[]{TemplateProcessor.class.getResource(".")};
        TemplateProcessor templateProcessor = new TemplateProcessor();
        templateProcessor.addTemplateLoader(templateDirectory);
        System.out.println("o=" + templateProcessor.renderTemplateToString("test_de.ftl", root));
        final OutputStreamWriter out = new OutputStreamWriter(System.out, Charset.forName("UTF-8"));
        templateProcessor.renderTemplate("test_de.ftl", root, out);
        out.flush();
    }
    */

}
