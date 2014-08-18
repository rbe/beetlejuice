/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 15.02.13 11:32
 */

package eu.artofcoding.beetlejuice.web;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class SecurityHelperTest {

    private static final String TEST_RESOURCES = "src/test/resources";
    private static final String WEB_ANTISAMY = "eu/artofcoding/beetlejuice/web/antisamy";

    @Inject
    @WebSecurity
    private Sanitizer antisamySanitizer;

    @Deployment
    public static Archive<?> createDeployment() {
        JavaArchive archive =
                ShrinkWrap.create(JavaArchive.class, "beetlejuice-web.jar").
                        addPackage(WebSecurity.class.getPackage()).
                        addAsResource(new FileAsset(new File(String.format("%s/%s/antisamy.xml", TEST_RESOURCES, WEB_ANTISAMY))), String.format("%s/antisamy.xml", WEB_ANTISAMY)).
                        addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        //System.out.println(archive.toString(true));
        return archive;
    }

    @Test
    public void testUnlistedTag() throws BeetlejuiceWebException {
        String taintedHTML = "<div><anewtag id=\"newtag\" anewattrib=\"attrib\">Text goes\n<b>here</b>.</anewtag></div>";
        String sanitizedHTML = antisamySanitizer.cleanInput(Locale.ENGLISH, taintedHTML);
        String expected = "<div>Text goes <b>here</b>.</div>";
        assertEquals(sanitizedHTML, expected);
    }

    @Test
    public void testRemoveTagButKeepChildren() throws BeetlejuiceWebException {
        String taintedHTML = "<div><anewtag id=\"newtag\" anewattrib=\"attrib\">Text goes\n<b>here</b>.</anewtag></div>";
        String sanitizedHTML = antisamySanitizer.cleanInput(Locale.ENGLISH, taintedHTML);
        String expected = "<div>Text goes <b>here</b>.</div>";
        assertEquals(sanitizedHTML, expected);
    }

}
