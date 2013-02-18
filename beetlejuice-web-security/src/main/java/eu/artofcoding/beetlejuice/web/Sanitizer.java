/*
 * beetlejuice
 * beetlejuice-web
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 15.02.13 11:43
 */

package eu.artofcoding.beetlejuice.web;

import java.util.Locale;

public interface Sanitizer {

    String cleanInput(Locale locale, String input) throws BeetlejuiceWebException;

}
