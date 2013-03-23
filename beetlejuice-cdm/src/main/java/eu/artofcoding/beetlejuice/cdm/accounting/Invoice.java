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

package eu.artofcoding.beetlejuice.cdm.accounting;

import eu.artofcoding.beetlejuice.cdm.store.Article;
import eu.artofcoding.beetlejuice.cdm.store.ReturnLabel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice implements Serializable {

    /**
     * Identification of invoice, e.g. an ID or string.
     */
    private String invoiceIdent;

    /**
     * When was this invoice created?
     */
    private Date invoiceDate;

    /**
     * Was this invoice paid totally?
     */
    private boolean invoicePaid;

    /**
     * Company used for shipping of articles in this invoice.
     * DHL, GLS, ...
     */
    private String shippingCompany;

    /**
     * Are all articles from this invoice returnable?
     */
    private boolean canReturn;

    /**
     * ReturnReason: why user cannot return articles from this invoice.
     */
    private String reason;

    /**
     * Articles invoiced.
     */
    private List<Article> articles;

    /**
     * The return label.
     */
    private ReturnLabel returnLabel;

    //<editor-fold desc="Constructor">

    public Invoice(String invoiceIdent, Date invoiceDate, boolean invoicePaid, String shippingCompany, boolean canReturn, String reason, List<Article> articles) {
        this.invoiceIdent = invoiceIdent;
        this.invoiceDate = invoiceDate;
        this.invoicePaid = invoicePaid;
        this.shippingCompany = shippingCompany;
        this.canReturn = canReturn;
        this.reason = reason;
        this.articles = articles;
    }

    public Invoice(String invoiceIdent, Date invoiceDate, boolean invoicePaid, String shippingCompany, boolean canReturn, String reason) {
        this.invoiceIdent = invoiceIdent;
        this.invoiceDate = invoiceDate;
        this.invoicePaid = invoicePaid;
        this.shippingCompany = shippingCompany;
        this.canReturn = canReturn;
        this.reason = reason;
    }

    public Invoice(String invoiceIdent, Date invoiceDate, boolean invoicePaid, boolean canReturn) {
        this.invoiceIdent = invoiceIdent;
        this.invoiceDate = invoiceDate;
        this.invoicePaid = invoicePaid;
        this.canReturn = canReturn;
    }

    //</editor-fold>

    //<editor-fold desc="Getter and Setter">

    public String getInvoiceIdent() {
        return invoiceIdent;
    }

    public void setInvoiceIdent(String invoiceIdent) {
        this.invoiceIdent = invoiceIdent;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public boolean isInvoicePaid() {
        return invoicePaid;
    }

    public void setInvoicePaid(boolean invoicePaid) {
        this.invoicePaid = invoicePaid;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public boolean isCanReturn() {
        return canReturn;
    }

    public void setCanReturn(boolean canReturn) {
        this.canReturn = canReturn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Article> getArticles() {
        if (null == articles) {
            articles = new ArrayList<>();
        }
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public ReturnLabel getReturnLabel() {
        return returnLabel;
    }

    public void setReturnLabel(ReturnLabel returnLabel) {
        this.returnLabel = returnLabel;
    }

    //</editor-fold>

    public void addArticle(Article article) {
        getArticles().add(article);
    }

    /**
     * Get all articles which were marked/should be returned.
     * @return List&lt;Article> with to-be-returned articles of this invoice.
     */
    public List<Article> getReturnArticles() {
        List<Article> _articles = new ArrayList<>(10);
        // Find all articles which should be returned
        for (Article a : articles) {
            if (a.getArticleReturn().isToBeReturned()) {
                _articles.add(a);
            }
        }
        return _articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        if (!invoiceDate.equals(invoice.invoiceDate)) return false;
        if (!invoiceIdent.equals(invoice.invoiceIdent)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceIdent.hashCode();
        result = 31 * result + invoiceDate.hashCode();
        return result;
    }

}
