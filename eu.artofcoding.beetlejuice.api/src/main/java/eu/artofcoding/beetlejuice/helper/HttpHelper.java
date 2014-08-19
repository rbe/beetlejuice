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

public class HttpHelper {

    public static class UserAuthenticator extends Authenticator {

        String user;
        String pass;

        public UserAuthenticator(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pass.toCharArray());
        }

    }

    public static byte[] post(URL url, String username, String password, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
        streamWriter.write(body);
        streamWriter.flush();
        // Read response
        byte[] response = StreamHelper.convertToBytes(connection.getInputStream());
        streamWriter.close();
        connection.disconnect();
        return response;
    }

}
