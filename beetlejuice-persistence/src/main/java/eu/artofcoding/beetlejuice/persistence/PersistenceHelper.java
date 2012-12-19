/*
 * beetlejuice
 * beetlejuice-persistence
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.12.12 21:13
 */

package eu.artofcoding.beetlejuice.persistence;

public class PersistenceHelper {

    public static String escape(String str) {
        if (null != str && str.length() > 0) {
            StringBuilder builder = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (c == '\'' || c == '"' || c == '|') {
                    // filter, ignore
                } else {
                    builder.append(c);
                }
            }
            return builder.toString();
        } else {
            return str;
        }
    }

}
