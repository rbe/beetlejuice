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

public final class GravatarHelper {

    private GravatarHelper() {
        throw new AssertionError();
    }

    /**
     * Generate URL for image of a Gravatar avatar.
     * @param email E-mail address.
     * @param size  Size of image.
     * @return URL for Gravatar image.
     */
    public static String getImageURL(final String email, final int size) {
        if (null != email && email.length() > 0) {
            final String checkedEmail = email.trim().toLowerCase();
            try {
                final MessageDigest md = MessageDigest.getInstance("MD5");
                final byte[] hash = md.digest(checkedEmail.getBytes());
                final BigInteger i = new BigInteger(1, hash);
                final String gravatarHash = String.format("%1$032x", i);
                return String.format("http://www.gravatar.com/avatar/%s.jpg?size=%d", gravatarHash, size);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * Generate URL for image of a Gravatar avatar. Size of image is 80 x 80 px.
     * @param email E-mail address.
     * @return URL for Gravatar image.
     */
    public static String getImageURL(final String email) throws BeetlejuiceException {
        return getImageURL(email, 80);
    }

}
