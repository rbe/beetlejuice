/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 7/5/12 8:42 AM
 */

package eu.artofcoding.beetlejuice.helper;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SecurityHelper {

    private SecurityHelper() {
        throw new AssertionError();
    }

    /**
     * Make a hashed password.
     * @param password
     * @return
     * @throws java.security.NoSuchAlgorithmException
     *
     */
    public static byte[] makeHashedPassword(String algorithm, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] passwordBytes = password.getBytes();
        return md.digest(passwordBytes);
    }

    /**
     * Make a MD5 hashed password.
     * @param password
     * @return
     * @throws java.security.NoSuchAlgorithmException
     *
     */
    public static byte[] makeMD5Password(String password) throws NoSuchAlgorithmException {
        return makeHashedPassword("MD5", password);
    }

    /**
     * Make a Base64 encoded MD5 hashed password.
     * @param password
     * @return
     * @throws java.security.NoSuchAlgorithmException
     *
     */
    public static String makeBase64EncodedMD5Password(String password) throws NoSuchAlgorithmException {
        byte[] passwordHash = makeMD5Password(password);
        return DatatypeConverter.printBase64Binary(passwordHash);
    }

}
