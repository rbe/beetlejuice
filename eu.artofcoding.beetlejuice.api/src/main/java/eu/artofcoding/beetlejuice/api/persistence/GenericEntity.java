/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 7/10/12 4:15 PM
 */

package eu.artofcoding.beetlejuice.api.persistence;

import java.io.Serializable;

public interface GenericEntity extends Serializable {

    public Long getId();

    public Long getVersion();

}
