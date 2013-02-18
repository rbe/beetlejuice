/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 11.02.13 13:36
 */

package eu.artofcoding.beetlejuice.cdm.store;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReturnLabel implements Serializable {

    private String identCode;

    private String routingCode;

    private String base64;

    private transient Path path;

    //<editor-fold desc="Constructor">

    public ReturnLabel(String identCode, String routingCode, String base64) {
        this.identCode = identCode;
        this.routingCode = routingCode;
        this.base64 = base64;
    }

    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getIdentCode() {
        return identCode;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public String getBase64() {
        return base64;
    }

    public Path getPath() {
        return path;
    }

    //</editor-fold>

    public void saveBinary(Path path) throws IOException {
        this.path = path;
        byte[] b = DatatypeConverter.parseBase64Binary(base64);
        Files.createDirectories(path.getParent());
        Files.write(path, b, StandardOpenOption.CREATE);
    }

}
