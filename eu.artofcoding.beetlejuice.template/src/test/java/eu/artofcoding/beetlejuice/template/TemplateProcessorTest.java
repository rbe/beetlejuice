/*
 * eu.artofcoding.beetlejuice
 * eu.artofcoding.beetlejuice.template
 * Copyright (C) 2011-2015 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 03.05.15 15:36
 */

package eu.artofcoding.beetlejuice.template;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateProcessorTest {

    public static void main(String[] args) throws Exception {
        final Map<String, Object> root = new HashMap<>();
        root.put("user", "Big Joe");
        root.put("registrationUrl", "http://www.example.com/registration/complete/abc123");
        final URL[] templateDirectory = new URL[]{TemplateProcessor.class.getResource(".")};
        final TemplateProcessor templateProcessor = new TemplateProcessor();
        templateProcessor.addTemplateLoader(templateDirectory);
        System.out.println("o=" + templateProcessor.renderTemplateToString("test_de.ftl", Locale.ENGLISH, root));
        final OutputStreamWriter out = new OutputStreamWriter(System.out, Charset.forName("UTF-8"));
        templateProcessor.renderTemplate("test_de.ftl", Locale.ENGLISH, root, out);
        out.flush();
    }

}
