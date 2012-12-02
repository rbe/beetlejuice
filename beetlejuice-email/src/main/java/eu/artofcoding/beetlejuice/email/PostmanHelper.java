package eu.artofcoding.beetlejuice.email;/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 24.11.12 12:39
 */

import javax.mail.internet.InternetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: rbe
 * Date: 24.11.12
 * Time: 12:39
 */
public class PostmanHelper {
    /**
     * Filter a list of addresses: get rid of duplicates
     * @param address Array of {@link javax.mail.internet.InternetAddress}.
     * @return Array of {@link javax.mail.internet.InternetAddress}.
     */
    public static InternetAddress[] filterAddresses(InternetAddress[] address) {
        Set<String> set = new TreeSet<>();
        List<InternetAddress> addr = new LinkedList<>();
        // Put every address in a set: no duplicates
        for (InternetAddress a : address) {
            set.add(a.getAddress());
        }
        // Look for InternetAddress objects having address found in Set
        for (String s : set) {
            for (InternetAddress a : address) {
                if (a.getAddress().equals(s)) {
                    addr.add(a);
                    break;
                }
            }
        }
        return addr.toArray(new InternetAddress[addr.size()]);
    }
}
