/*
 * beetlejuice
 * beetlejuice-contact
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 12.04.13 15:27
 */

package eu.artofcoding.beetlejuice.contact;

import net.fortuna.ical4j.util.CompatibilityHints;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.VCardBuilder;

import java.io.File;
import java.io.FileReader;

public class VCardFactory {

    public static VCard getVCard() {
        return new VCard();
    }

    public static VCard getVCard(File file) throws ContactException {
        try {
            CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
            VCard vcard = new VCardBuilder(new FileReader(file)).build();
            CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, false);
            return vcard;
        } catch (Exception e) {
            throw new ContactException(e);
        }
    }

}
