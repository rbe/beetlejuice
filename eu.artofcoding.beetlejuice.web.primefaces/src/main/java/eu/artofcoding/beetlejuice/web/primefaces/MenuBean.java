/*
 * beetlejuice
 * beetlejuice-web-primefaces
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.09.12 20:42
 */

package eu.artofcoding.beetlejuice.web.primefaces;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MenuBean implements Serializable {

    private CategoryMenuModel menuModel = new CategoryMenuModel();

    public MenuBean() {
        try {
            MenuCategory computers = new MenuCategory("Computers");
            List<Category> computerChildren = new ArrayList<>();
            computerChildren.add(new MenuCategory("Hello"));
            computers.setChildren(computerChildren);
            menuModel.setCategory(computers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CategoryMenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(CategoryMenuModel menuModel) {
        this.menuModel = menuModel;
    }

}
