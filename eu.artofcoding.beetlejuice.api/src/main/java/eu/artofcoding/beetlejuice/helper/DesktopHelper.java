/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 11.02.13 10:15
 */

package eu.artofcoding.beetlejuice.helper;

import java.io.File;
import java.io.IOException;

public final class DesktopHelper {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    private DesktopHelper() {
        throw new AssertionError();
    }

    public static void open(File file) throws IOException {
        if (isWindows()) {
            // TODO Windows version? Runtime.getRuntime().exec(String.format("rundll32 url.dll,FileProtocolHandler %s", file.getAbsolutePath()));
            Runtime.getRuntime().exec(String.format("rundll32 SHELL32.DLL,ShellExec_RunDLL %s", file.getAbsolutePath()));
        } else {
            java.awt.Desktop.getDesktop().open(file);
        }
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac")) || OS.contains("os x");
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

}
