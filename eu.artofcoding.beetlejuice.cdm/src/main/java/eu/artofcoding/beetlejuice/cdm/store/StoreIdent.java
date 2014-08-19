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

import eu.artofcoding.beetlejuice.cdm.Base;

public class StoreIdent extends Base {

    private String name;

    private String ident;
    
    public StoreIdent() {
    }

    public StoreIdent(String ident, String name) {
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
