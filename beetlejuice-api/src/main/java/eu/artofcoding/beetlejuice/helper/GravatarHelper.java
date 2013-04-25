/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 25.03.13 08:24
 */

package eu.artofcoding.beetlejuice.helper;

import eu.artofcoding.beetlejuice.api.BeetlejuiceException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GravatarHelper {

    /**
     * Generate URL for image of a Gravatar avatar.
     * @param email E-mail address.
     * @param size  Size of image, standars is 80 x 80 px.
     * @return URL for Gravatar image.
     */
    public static String getImageURL(String email, int size) {
        if (null != email && email.length() > 0) {
            email = email.trim().toLowerCase();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hash = md.digest(email.getBytes());
                BigInteger i = new BigInteger(1, hash);
                String gravatarHash = String.format("%1$032x", i);
                return String.format("http://www.gravatar.com/avatar/%s.jpg?size=%d", gravatarHash, size);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public static String getImageURL(String email) throws BeetlejuiceException {
        return getImageURL(email, 80);
    }

}
