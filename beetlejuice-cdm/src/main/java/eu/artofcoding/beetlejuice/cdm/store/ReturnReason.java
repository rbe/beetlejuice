/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 05.01.13 13:49
 */

package eu.artofcoding.beetlejuice.cdm.store;

import java.io.Serializable;
import java.util.List;

public class ReturnReason implements Serializable {

    private Integer id;

    /**
     * The reason.
     */
    private String reason;

    /**
     * Detailed description for this reason.
     */
    private String description;

    /**
     * User entered notice.
     */
    private String notice;

    /**
     * Nested reasons to clarify this one.
     */
    private List<ReturnReason> nestedReasons;

    //<editor-fold desc="Constructor">

    public ReturnReason(Integer id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public ReturnReason(Integer id, String reason, String description) {
        this(id, reason);
        this.description = description;
    }

    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public Integer getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<ReturnReason> getNestedReasons() {
        return nestedReasons;
    }

    public void setNestedReasons(List<ReturnReason> nestedReasons) {
        this.nestedReasons = nestedReasons;
    }

    //</editor-fold>

}
