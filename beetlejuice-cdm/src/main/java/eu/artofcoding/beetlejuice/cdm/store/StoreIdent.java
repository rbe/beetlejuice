/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 13.02.13 12:15
 */

package eu.artofcoding.beetlejuice.cdm.store;

import java.io.Serializable;

public enum StoreIdent implements Serializable {

    NONE("00", "keine Firma"),
    AKTIVSHOP("01", "aktivshop"),
    AKTIVMED("02", "aktivmed"),
    DIABETIKER_BEDARF("03", "diabetiker-bedarf");

    private String name;

    private String ident;

    StoreIdent(String ident, String name) {
        this.ident = ident;
        this.name = name;
    }

    public String getIdent() {
        return ident;
    }

    public String getName() {
        return name;
    }

}
