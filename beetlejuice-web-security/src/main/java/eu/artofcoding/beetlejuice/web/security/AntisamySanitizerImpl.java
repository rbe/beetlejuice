/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 15.02.13 09:14
 */

package eu.artofcoding.beetlejuice.web.security;

import eu.artofcoding.beetlejuice.web.BeetlejuiceWebException;
import org.owasp.validator.html.*;

import java.net.URL;
import java.util.Locale;

public class AntisamySanitizerImpl implements Sanitizer {

    /**
     * URL of OWASP AntiSamy policy file.
     */
    private URL policyFile;

    /**
     * Policy file for OWASP AntiSamy.
     */
    private Policy policy;

    /**
     * Instance of OWASP AntiSamy.
     */
    private AntiSamy antiSamy;

    /**
     * Constructor.
     * @param policyFile OWASP AntiSamy policy file.
     */
    public AntisamySanitizerImpl(URL policyFile) {
        if (null == policyFile) {
            throw new IllegalStateException("Need policy XML file");
        }
        this.policyFile = policyFile;
        antiSamy = new AntiSamy();
    }

    private Policy getPolicy() throws PolicyException {
        if (null == policy) {
            policy = Policy.getInstance(policyFile);
        }
        return policy;
    }

    /**
     * Clean possibly tainted HTML.
     * @param locale Locale of text.
     * @param input  Possibly tainted HTML.
     * @return String Clean HTML.
     * @throws BeetlejuiceWebException
     */
    public String cleanInput(Locale locale, String input) throws BeetlejuiceWebException {
        // Save actual default locale
        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(locale);
        try {
            CleanResults cleanResults = antiSamy.scan(input, getPolicy(), AntiSamy.SAX);
            return cleanResults.getCleanHTML();
        } catch (ScanException | PolicyException e) {
            throw new BeetlejuiceWebException(e);
        } finally {
            // Restore old default locale
            Locale.setDefault(oldLocale);
        }
    }

}
