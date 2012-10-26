/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 24.10.12 17:51
 */

package eu.artofcoding.beetlejuice.api.persistence;

import java.io.Serializable;

public class QueryParameter implements Serializable {

    private String parameterName;

    private Object[] values;

    private String operator;

    private String connector;

    public QueryParameter(String parameterName, Object[] values, String operator, String connector) {
        this.parameterName = parameterName;
        this.values = values;
        this.operator = operator;
        this.connector = connector;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

}
