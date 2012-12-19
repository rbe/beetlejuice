/*
 * beetlejuice
 * beetlejuice-cdm
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 12.12.12 16:38
 */

package eu.artofcoding.beetlejuice.cdm;

public class PostalAddress extends Base {

    private String shippingAddress1;

    private String shippingAddress1StreetNumber;

    private String shippingAddress2;

    private String zipcode;

    private String city;

    private String phone;

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress1StreetNumber() {
        return shippingAddress1StreetNumber;
    }

    public void setShippingAddress1StreetNumber(String shippingAddress1StreetNumber) {
        this.shippingAddress1StreetNumber = shippingAddress1StreetNumber;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
