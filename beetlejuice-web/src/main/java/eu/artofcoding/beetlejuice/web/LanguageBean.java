/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 28.08.12 14:26
 */

package eu.artofcoding.beetlejuice.web;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LanguageBean implements Serializable {

    /**
     * Actual locale to use.
     */
    private String localeCode;

    /**
     * Supported countries/locales.
     */
    private static Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<>();
        countries.put("English", Locale.ENGLISH); // Label, value
        countries.put("German", Locale.GERMAN);
    }

    /**
     * Return a list of countries/languages supported by this application.
     * @return
     */
    public Map<String, Object> getCountries() {
        return countries;
    }

    /**
     * Return actual locale code.
     * Asking for locale code for the first time we use locale from actual HTTP request.
     * @return String Locale code.
     */
    public String getLocaleCode() {
        if (null == localeCode) {
            localeCode = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
            System.out.println(this + ".getLocaleCode(): set locale (from request) to " + localeCode);
        }
        return localeCode;
    }

    /**
     * Set locale code.
     * @param localeCode Set locale code by string, e.g. "de" or "de_DE".
     */
    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    /**
     * Value change event listener.
     * @param e ValueChangeEvent
     */
    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        String newLocaleValue = e.getNewValue().toString();
        System.out.println(this + ".countryLocaleCodeChanged: " + newLocaleValue);
        // Loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {
            final boolean foundLocale = entry.getValue().toString().equals(newLocaleValue);
            if (foundLocale) {
                // Change locale
                final Locale locale = (Locale) entry.getValue();
                FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
            }
        }
    }

    /**
     * Get a localized message from a bundle.
     * @param baseName Name of resource bundle.
     * @param locale   Locale to lookup.
     * @param key      Key for message.
     * @return String The message.
     */
    public String getMessage(String baseName, Locale locale, String key) {
        ResourceBundle res = ResourceBundle.getBundle(baseName, locale);
        return res.getString(key);
    }

}
