/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 14.01.13 11:56
 */

package eu.artofcoding.beetlejuice.helper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public final class HttpHelper {

    private HttpHelper() {
        throw new AssertionError();
    }

    public static byte[] post(final URL url, String username, final String password, final String body) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (null != username) {
            Authenticator.setDefault(new UserAuthenticator(username, password));
        }
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
        // Write request
        final OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
        streamWriter.write(body);
        streamWriter.flush();
        // Read response
        final byte[] response = StreamHelper.convertToBytes(connection.getInputStream());
        streamWriter.close();
        connection.disconnect();
        return response;
    }

    public static class UserAuthenticator extends Authenticator {

        private String user;

        private String pass;

        public UserAuthenticator(final String user, final String pass) {
            this.user = user;
            this.pass = pass;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pass.toCharArray());
        }

    }

}
