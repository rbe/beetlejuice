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

package eu.artofcoding.beetlejuice.cdm.germany;

public enum Bundesland {

    BADEN_WUERTTEMBERG("Baden-Württemberg"),
    BAYERN("Bayern"),
    BERLIN("Berlin"),
    BRANDENBURG("Brandenburg"),
    BREMEN("Bremen"),
    HAMBURG("Hamburg"),
    HESSEN("Hessen"),
    MECKLENBURG_VORPOMMERN("Mecklenburg-Vorpommern"),
    NIEDERSACHSEN("Niedersachsen"),
    NORDRHEIN_WESTFALEN("Nordrhein-Westfalen"),
    RHEINLAND_PFALZ("Rheinland-Pfalz"),
    SAARLAND("Saarland"),
    SACHSEN("Sachsen"),
    SACHSEN_ANHALT("Sachsen-Anhalt"),
    SCHLESWIG_HOLSTEIN("Schleswig-Holstein"),
    THUERINGEN("Thüringen");

    String denomination;

    Bundesland(String denomination) {
        this.denomination = denomination;
    }

    public String getDenomination() {
        return denomination;
    }

}
