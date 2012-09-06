/*
 * beetlejuice
 * beetlejuice-web-primefaces
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.09.12 20:42
 */

package eu.artofcoding.beetlejuice.web.primefaces;

import java.util.Collection;

/**
 *
 */
public interface Category {

    String getName();

    Collection<Category> getChildren();

}
