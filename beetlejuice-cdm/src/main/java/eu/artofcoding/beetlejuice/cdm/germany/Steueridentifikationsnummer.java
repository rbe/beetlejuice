/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 07.12.12 08:25
 */

package eu.artofcoding.beetlejuice.cdm.germany;

/**
 * http://de.wikipedia.org/wiki/Steuer-Identifikationsnummer
 * 
 */
public class Steueridentifikationsnummer {
    
    /*
Ziffernfolge : array[1..10] of 0..9;
Summe := 0; Produkt := 10;
 
for Stelle := 1 to 10 do
 begin 
  Summe := (Ziffernfolge[Stelle] + Produkt) mod 10;
  if ( Summe = 0 )
    then Summe := 10;
  Produkt := (Summe * 2) mod 11;
 end;
Prüfziffer := 11 - Produkt;
if ( Prüfziffer = 10 )
 then Prüfziffer := 0;
     */
    
}
