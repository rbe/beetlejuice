/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 29.11.12 13:51
 */

package eu.artofcoding.beetlejuice.cdm.store;

import eu.artofcoding.beetlejuice.cdm.Group;

import java.io.Serializable;

public class Article implements Serializable {

    /**
     * Article ident, e.g. an ID or name.
     */
    private String articleIdent;

    /**
     * Main group this article belongs to.
     */
    private Group mainGroup;

    /**
     * Subgroup this article belongs to.
     */
    private Group subGroup;

    /**
     * Company used for shipping of this article.
     * DHL, GLS, ...
     */
    private String shippingCompany;

    /**
     * Number of articles delivered to customer.
     */
    private float count;

    /**
     * Description 1.
     */
    private String description1;

    /**
     * Description 2.
     */
    private String description2;

    /**
     * Return this article.
     */
    private ArticleReturn articleReturn;

    //<editor-fold desc="Constructor">

    public Article(String articleIdent, Group mainGroup, Group subGroup, String shippingCompany, float count, String description1, String description2) {
        this.articleIdent = articleIdent;
        this.mainGroup = mainGroup;
        this.subGroup = subGroup;
        this.shippingCompany = shippingCompany;
        this.count = count;
        this.description1 = description1;
        this.description2 = description2;
    }

    public Article(String articleIdent, Group mainGroup, Group subGroup, float count) {
        this.articleIdent = articleIdent;
        this.mainGroup = mainGroup;
        this.subGroup = subGroup;
        this.count = count;
    }

    public Article(String articleIdent, float count) {
        this.articleIdent = articleIdent;
        this.count = count;
    }

    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getArticleIdent() {
        return articleIdent;
    }

    public void setArticleIdent(String articleIdent) {
        this.articleIdent = articleIdent;
    }

    public Group getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(Group subGroup) {
        this.subGroup = subGroup;
    }

    public Group getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(Group mainGroup) {
        this.mainGroup = mainGroup;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public ArticleReturn getArticleReturn() {
        if (null == articleReturn) {
            articleReturn = new ArticleReturn();
        }
        return articleReturn;
    }

    public void setArticleReturn(ArticleReturn articleReturn) {
        this.articleReturn = articleReturn;
    }

    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;

        Article article = (Article) o;

        if (!articleIdent.equals(article.articleIdent)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return articleIdent.hashCode();
    }

}
