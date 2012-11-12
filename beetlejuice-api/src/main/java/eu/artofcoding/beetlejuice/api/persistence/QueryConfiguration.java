/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 11.11.12 12:45
 */

package eu.artofcoding.beetlejuice.api.persistence;

import java.util.List;

public class QueryConfiguration {

    private QueryVariant queryVariant;

    private List<QueryParameter> queryParameters;

    private String tableName;

    private boolean nativeQuery;

    public QueryVariant getQueryVariant() {
        return queryVariant;
    }

    public void setQueryVariant(QueryVariant queryVariant) {
        this.queryVariant = queryVariant;
    }

    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isNativeQuery() {
        return nativeQuery;
    }

    public void setNativeQuery(boolean nativeQuery) {
        this.nativeQuery = nativeQuery;
    }

}
