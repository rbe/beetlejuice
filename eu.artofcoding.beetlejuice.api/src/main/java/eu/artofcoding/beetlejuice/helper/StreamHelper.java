/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 14.01.13 12:59
 */

package eu.artofcoding.beetlejuice.helper;

import java.io.*;

public class StreamHelper {

    private static final int BUF_SIZE = 128 * 1024;

    public static byte[] convertToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUF_SIZE);
        byte[] buf = new byte[BUF_SIZE];
        int len;
        while ((len = stream.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return baos.toByteArray();
    }

    public static void saveToFile(InputStream stream, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[BUF_SIZE];
        int len;
        while ((len = stream.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.close();
    }

    public static void saveToFile(byte[] b, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(b);
        fos.close();
    }

}
