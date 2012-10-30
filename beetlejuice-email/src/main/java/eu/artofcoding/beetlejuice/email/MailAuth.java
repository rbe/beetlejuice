/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 29.10.12 14:04
 */

package eu.artofcoding.beetlejuice.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuth {

    private String username;
    private String password;

    private Authenticator authenticator;

    public MailAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Authenticator getAuthenticator() {
        if (null == authenticator) {
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };
        }
        return authenticator;
    }

}
