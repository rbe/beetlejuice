/*
 * eu.artofcoding.beetlejuice
 * eu.artofcoding.beetlejuice.api
 * Copyright (C) 2011-2015 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 03.05.15 15:23
 */

package eu.artofcoding.beetlejuice.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public final class SecurityCLI {

    private SecurityCLI() {
        throw new AssertionError();
    }

    /**
     * Provide a CLI interface to generate passwords.
     * @param args
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     *
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String password;
        if (args.length == 1) {
            password = args[0];
        } else {
            System.out.print("Enter password: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            password = reader.readLine();
        }
        byte[] md5PasswordHash = SecurityHelper.makeMD5Password(password);
        String md5PasswordString = new String(md5PasswordHash);
        System.out.println("MD5 password hash: " + md5PasswordString);
        String base64PasswordHash = SecurityHelper.makeBase64EncodedMD5Password(password);
        System.out.println("Base64 encoded password hash: " + base64PasswordHash);
    }

}
