/*
 * beetlejuice
 * beetlejuice-web-jsfcomponents
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.03.13 15:43
 */

package eu.artofcoding.beetlejuice.web.jsf;

import java.io.Serializable;

public class RadioButtonData implements Serializable {

    private String label;

    private String value;

    private String description;

    private boolean disabled;

    public RadioButtonData(String label, String value, String description, boolean disabled) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.disabled = disabled;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisabled() {
        return disabled;
    }

}
