/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 05.01.13 13:49
 */

package eu.artofcoding.beetlejuice.cdm;

import eu.artofcoding.beetlejuice.api.persistence.GenericEntity;

/**
 * Base class for database entites.
 */
public class Base implements GenericEntity {

    private Long id;

    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
