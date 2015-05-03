/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 14.02.13 17:54
 */

package eu.artofcoding.beetlejuice.cdm;

public class Group extends Base {

    private String groupIdent;

    private String groupName;

    private Group parent;

    public Group(String groupIdent, String groupName) {
        this.groupIdent = groupIdent;
        this.groupName = groupName;
    }

    public Group(String groupIdent, String groupName, Group parent) {
        this.groupIdent = groupIdent;
        this.groupName = groupName;
        this.parent = parent;
    }

    public String getGroupIdent() {
        return groupIdent;
    }

    public void setGroupIdent(String groupIdent) {
        this.groupIdent = groupIdent;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

}
