/*
 * beetlejuice
 * beetlejuice-template
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 06.11.12 17:05
 */
package eu.artofcoding.beetlejuice.template;

import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This template processor acts as a facade to FreeMarker template engine.
 * <pre>
 * // Create the root hash
 * Map<String, Object> root = new HashMap<String, Object>();
 * root.put("user", "Big Joe");
 * root.put("registrationUrl", "http://www.example.com/registration/complete/abc123");
 * // Locate directory with template
 * URL[] templateDirectory = new URL[]{TemplateProcessor.class.getResource(".")};
 * // Create instance of TemplateProcessor
 * TemplateProcessor templateProcessor = new TemplateProcessor();
 * templateProcessor.addTemplateLoader(templateDirectory);
 * // Render template
 * OutputStreamWriter out = new OutputStreamWriter(System.out, Charset.forName("UTF-8"));
 * templateProcessor.renderTemplate("test_de.ftl", root, out);
 * out.flush();
 * </pre>
 */
@Named("template")
public class TemplateProcessor implements Serializable {

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
        //configuration.setObjectWrapper(new DefaultObjectWrapper());
    }

    public Configuration getConfiguration() {
        return configuration;
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
     * @param templateLoader {@link TemplateLoader}
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
     * @param templateName Name of template.
     * @param root         Data for FreeMarker, e.g. Map<String, Object> or SimpleHash.
     * @param out          {@link Writer}
     * @throws TemplateException
     */
    public void renderTemplate(String templateName, Locale locale, Object root, Writer out) throws TemplateException {
        makeTemplateLoader();
        Template temp = null;
        try {
            temp = configuration.getTemplate(templateName, locale, "UTF-8");
            temp.process(root, out);
        } catch (IOException e) {
            throw new TemplateException("Cannot render template", e, null);
        }
    }

    /**
     * Render a template, see {@link #renderTemplate(String, java.util.Locale, java.lang.Object, java.io.Writer)} and return a String.
     * @param templateName Name of template.
     * @param root         Data for FreeMarker, e.g. Map<String, Object> or SimpleHash.
     * @return String
     * @throws TemplateException
     */
    public String renderTemplateToString(String templateName, Locale locale, Object root) throws TemplateException {
        Writer o = new StringWriter();
        renderTemplate(templateName, locale, root, o);
        try {
            o.flush();
            return o.toString();
        } catch (IOException e) {
            throw new TemplateException("Cannot render template", e, null);
        }
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
