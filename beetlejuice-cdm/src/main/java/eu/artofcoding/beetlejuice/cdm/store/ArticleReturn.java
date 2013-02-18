/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 14.02.13 17:44
 */

package eu.artofcoding.beetlejuice.cdm.store;

import eu.artofcoding.beetlejuice.api.BeetlejuiceConstant;
import eu.artofcoding.beetlejuice.cdm.Base;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ArticleReturn extends Base {

    /**
     * Article capable of being returned?
     */
    private boolean returnable;

    /**
     * Why won't we accept a return?
     */
    private String unreturnableReason;

    /**
     * This article should be sent back.
     */
    private boolean toBeReturned;

    /**
     * How many to return?
     */
    private float returnCount;

    /**
     * Reasons for returning this article, selected by customer.
     */
    private Set<ReturnReason> returnReason = new HashSet<>();

    //<editor-fold desc="Getter and Setter">

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public String getUnreturnableReason() {
        return unreturnableReason;
    }

    public void setUnreturnableReason(String unreturnableReason) {
        this.unreturnableReason = unreturnableReason;
    }

    public boolean isToBeReturned() {
        return toBeReturned;
    }

    public void setToBeReturned(boolean toBeReturned) {
        this.toBeReturned = toBeReturned;
    }

    public float getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(float returnCount) {
        this.returnCount = returnCount;
    }

    public Set<ReturnReason> getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(Set<ReturnReason> returnReason) {
        this.returnReason = returnReason;
    }

    //</editor-fold>

    public void addReturnReason(ReturnReason returnReason) {
        this.returnReason.add(returnReason);
        setToBeReturned(true);
    }

    public void removeReturnReason(ReturnReason returnReason) {
        this.returnReason.remove(returnReason);
    }

    public String getIdForReason(ReturnReason reason) {
        //String format = String.format("article%s_reason%d", articleIdent, reason.getId());
        String format = String.format("%s", reason.getId());
        return format;
    }

    public String getReturnReasonsAsString() {
        StringBuilder builder = new StringBuilder();
        for (Iterator<ReturnReason> iterator = returnReason.iterator(); iterator.hasNext(); ) {
            ReturnReason reason = iterator.next();
            builder.append(reason.getReason());
            while (iterator.hasNext()) {
                builder.append(BeetlejuiceConstant.COMMA).append(BeetlejuiceConstant.SPACE);
            }
        }
        return builder.toString();
    }

}
