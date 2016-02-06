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

import eu.artofcoding.beetlejuice.cdm.Base;

/**
 * http://de.wikipedia.org/wiki/Steuernummer
 * Zitat:
 * <ul>
 * <li>Die Länge der Steuernummer variiert beim Standardschema der Länder zwischen 10 und 11 Ziffern
 * und beträgt für das Bundesschema einheitlich 13 Ziffern.</li>
 * <li>Dabei stellen (F)FF die letzten zwei bzw. drei Ziffern der vierstelligen Bundesfinanzamtsnummer dar.</li>
 * <li>Außerdem steht BBB(B) für die Bezirksnummer innerhalb des Bereiches des jeweiligen Finanzamtes.</li>
 * <li>Sie ist in Nordrhein-Westfalen 4-stellig, ansonsten 3-stellig.</li>
 * <li>(U)UUU ist die persönliche Unterscheidungsnummer. Sie ist in Nordrhein-Westfalen 3-stellig, ansonsten 4-stellig.</li>
 * <li>P ist eine einstellige Prüfziffer.</li>
 * </ul>
 */
public class Steuernummer extends Base {

    private String bundesland;

    private String bundesfinanzamtsnummer;

    private String bezirksnummer;

    private String unterscheidungsnummer;

    private String pruefziffer;

    private String muster;

    public static Steuernummer create(String f, String b, String u, String p) {
        return null;
    }

    public static Steuernummer create(Bundesland bundesland, String b, String u, String p) {
        return null;
    }

    /*
    Baden-Württemberg	FFBBB/UUUUP	z. B. 93815/08152	28FF0BBBUUUUP	z. B. 2893081508152
    Bayern	FFF/BBB/UUUUP	z. B. 181/815/08155	9FFF0BBBUUUUP	z. B. 9181081508155
    Berlin	FF/BBB/UUUUP	z. B. 21/815/08150	11FF0BBBUUUUP	z. B. 1121081508150
    Brandenburg	FFF/BBB/UUUUP	z. B. 048/815/08155	3FFF0BBBUUUUP	z. B. 3048081508155
    Bremen	FF BBB UUUUP	z. B. 75 815 08152	24FF0BBBUUUUP	z. B. 2475081508152
    Hamburg	FF/BBB/UUUUP	z. B. 02/815/08156	22FF0BBBUUUUP	z. B. 2202081508156
    Hessen	0FF BBB UUUUP	z. B. 013 815 08153	26FF0BBBUUUUP	z. B. 2613081508153
    Mecklenburg-Vorpommern	FFF/BBB/UUUUP	z. B. 079/815/08151	4FFF0BBBUUUUP	z. B. 4079081508151
    Niedersachsen	FF/BBB/UUUUP	z. B. 24/815/08151	23FF0BBBUUUUP	z. B. 2324081508151
    Nordrhein-Westfalen	FFF/BBBB/UUUP	z. B. 133/8150/8159	5FFF0BBBBUUUP	z. B. 5133081508159
    Rheinland-Pfalz	FF/BBB/UUUU/P	z. B. 22/815/0815/4	27FF0BBBUUUUP	z. B. 2722081508154
    Saarland	FFF/BBB/UUUUP	z. B. 010/815/08182	1FFF0BBBUUUUP	z. B. 1010081508182
    Sachsen	FFF/BBB/UUUUP	z. B. 201/123/12340	3FFF0BBBUUUUP	z. B. 3201012312340
    Sachsen-Anhalt	FFF/BBB/UUUUP	z. B. 101/815/08154	3FFF0BBBUUUUP	z. B. 3101081508154
    Schleswig-Holstein	FF BBB UUUUP	z. B. 29 815 08158	21FF0BBBUUUUP	z. B. 2129081508158
    Thüringen FFF/BBB/UUUUP	z. B. 151/815/08156	4FFF0BBBUUUUP	z. B. 4151081508156
    */

}
