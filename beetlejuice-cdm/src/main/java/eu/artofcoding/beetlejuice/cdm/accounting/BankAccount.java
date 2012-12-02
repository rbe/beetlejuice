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

package eu.artofcoding.beetlejuice.cdm.accounting;

import eu.artofcoding.beetlejuice.cdm.Base;

import java.util.Calendar;

public class BankAccount extends Base {

    private Calendar lastModified;

    private BankAccountType accountType = BankAccountType.NONE;

    private FinanceCompany financeCompany = FinanceCompany.NONE;

    private String accountHolder;

    private String accountNumber;

    private String routingCode;

    private String email;

    //<editor-fold desc="Getter and Setter">

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public FinanceCompany getFinanceCompany() {
        return financeCompany;
    }

    public void setFinanceCompany(FinanceCompany financeCompany) {
        this.financeCompany = financeCompany;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //</editor-fold>

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public void updateLastModified() {
        setLastModified(Calendar.getInstance());
    }

}
