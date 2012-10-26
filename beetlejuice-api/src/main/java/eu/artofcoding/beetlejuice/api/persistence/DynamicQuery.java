/*
 * beetlejuice
 * beetlejuice-persistence
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 25.10.12 15:12
 */

package eu.artofcoding.beetlejuice.api.persistence;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.*;

/**
 * @param <T> Type of entity.
 */
public class DynamicQuery<T> {

    //<editor-fold desc="Members">

    /**
     * The entity manager.
     */
    private EntityManager entityManager;

    /**
     * The entity class we build a query for.
     */
    private Class<T> entityClass;

    /**
     * Parameters for query.
     */
    private List<QueryParameter> queryParameters;

    /**
     * Connector (AND, OR) between different queries against a field.
     */
    private String clauseConnector;

    /**
     * Generated parameters, used to bind values to query.
     */
    private Map<String, Object> parameters;

    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * Constructor.
     * @param entityManager   EntityManager.
     * @param entityClass     Type of entity.
     * @param queryParameters List with QueryParameters of type &lt;O>.
     * @param clauseConnector Connector between queries against a field, can be AND or OR.
     */
    public DynamicQuery(EntityManager entityManager, Class<T> entityClass, List<QueryParameter> queryParameters, String clauseConnector) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.queryParameters = queryParameters;
        this.clauseConnector = clauseConnector;
    }

    //</editor-fold>

    //<editor-fold desc="Getters">

    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    public String getClauseConnector() {
        return clauseConnector;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    //</editor-fold>

    /**
     * Build a JPA query based on list of QueryParameters.
     * @param selectClause SELECT-clause to start with.
     * @return StringBuilder containing JPA query.
     */
    private StringBuilder buildQuery(String selectClause) {
        // Build JPA query
        StringBuilder builder = new StringBuilder(selectClause);
        // Add conditionals
        parameters = new HashMap<>();
        if (null != queryParameters) {
            int size = queryParameters.size();
            if (size > 0) {
                builder.append(SQL_SPACE_WHERE);
                int queryParameterCounter = 0;
                int queryValueCounter;
                for (QueryParameter q : queryParameters) {
                    int valueCount = q.getValues().length;
                    if (valueCount > 0) {
                        // Append clauseConnector between different QueryParameter
                        if (queryParameterCounter > 0 && queryParameterCounter < size) {
                            builder.append(SPACE).append(clauseConnector);
                        }
                        //
                        queryValueCounter = 0;
                        builder.append(SPACE_LEFT_PARANTHESIS);
                        for (Object v : q.getValues()) {
                            // Create named parameter and add it to parameter-map
                            String paramName = String.format("%s%d", q.getParameterName(), queryValueCounter);
                            parameters.put(paramName, v);
                            //
                            if (v instanceof String) {
                                String op = LIKE;
                                if (null != q.getOperator()) {
                                    op = q.getOperator();
                                }
                                // LOWER(o.<property>) LIKE :<named parameter>
                                builder.append(JPA_LOWER).append(JPA_O_DOT).append(q.getParameterName()).append(RIGHT_PARANTHESIS).
                                        append(SPACE).append(op).append(SPACE_COLON).append(paramName);
                            } else if (v instanceof java.util.Date) {
                                String op = EQUAL_SIGN;
                                if (null != q.getOperator()) {
                                    op = q.getOperator();
                                }
                                // o.<property> = :<named parameter>
                                builder.append(JPA_O_DOT).append(q.getParameterName()).append(RIGHT_PARANTHESIS).
                                        append(SPACE).append(op).append(SPACE_COLON).append(paramName);
                            }
                            if (queryValueCounter < valueCount - 1) {
                                builder.append(SPACE).append(q.getConnector()).append(SPACE);
                            }
                            queryValueCounter++;
                        }
                        builder.append(RIGHT_PARANTHESIS);
                        // Increase counter
                        queryParameterCounter++;
                    }
                }
            }
        }
        return builder;
    }

    /**
     * Generate a JPA query to query for entities.
     * @param selectClause SELECT-clause to start with.
     * @return
     */
    public TypedQuery<T> getQuery(String selectClause) {
        StringBuilder builder = buildQuery(selectClause);
        TypedQuery<T> query = entityManager.createQuery(builder.toString(), entityClass);
        bindQueryParameter(query);
        return query;
    }

    /**
     * Generate a JPA query to count entities.
     * @param selectClause SELECT-clause to start with.
     * @return
     */
    public TypedQuery<Long> getCountQuery(String selectClause) {
        StringBuilder builder = buildQuery(selectClause);
        TypedQuery<Long> countQuery = entityManager.createQuery(builder.toString(), Long.class);
        bindQueryParameter(countQuery);
        return countQuery;
    }

    /**
     * Bind QueryParameter to previously generated JPA query.
     * @param query The JPA query.
     */
    private void bindQueryParameter(TypedQuery<?> query) {
        if (parameters.size() > 0) {
            for (String k : parameters.keySet()) {
                Object val = parameters.get(k);
                if (val instanceof String) {
                    // TODO Make wildcards configurable for LIKE
                    query.setParameter(k, "%" + val + "%");
                } else {
                    query.setParameter(k, val);
                }
            }
        }
    }

}