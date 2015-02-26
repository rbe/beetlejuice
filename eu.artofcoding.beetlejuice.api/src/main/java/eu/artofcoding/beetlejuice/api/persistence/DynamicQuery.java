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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.AND;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.COLON;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.COMMA;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.DOT;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.EQUAL_SIGN;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.INNER_JOIN;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.IS_NOT_NULL;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.JPA_LOWER;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.JPA_O;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.JPA_O_DOT;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.LEFT_PARANTHESIS;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.LIKE;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.OR;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.ORDER_BY;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.QUESTION_MARK;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.RIGHT_PARANTHESIS;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.SPACE;
import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.WHERE;

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
     * Configuration of query.
     */
    private QueryConfiguration queryConfiguration;

    /**
     * Connector (AND, OR) between different queries against a field.
     */
    private String clauseConnector;

    /**
     * ORDER BY
     */
    private String[] orderBy;

    /**
     * Generated parameters, used to bind values to query.
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * Maximum number of rows to return.
     */
    private long maxRows;

    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * Constructor.
     * @param entityManager      EntityManager.
     * @param entityClass        Type of entity.
     * @param queryConfiguration Configuration for query.
     * @param clauseConnector    Connector between queries against a field, can be AND or OR.
     */
    public DynamicQuery(EntityManager entityManager, Class<T> entityClass, QueryConfiguration queryConfiguration, String clauseConnector) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.queryConfiguration = queryConfiguration;
        this.clauseConnector = clauseConnector;
    }

    /**
     * Constructor.
     * @param entityManager      EntityManager.
     * @param entityClass        Type of entity.
     * @param queryConfiguration Configuration for query.
     * @param clauseConnector    Connector between queries against a field, can be AND or OR.
     * @param orderBy            ORDER BY.
     */
    public DynamicQuery(EntityManager entityManager, Class<T> entityClass, QueryConfiguration queryConfiguration, String clauseConnector, String[] orderBy) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.queryConfiguration = queryConfiguration;
        this.clauseConnector = clauseConnector;
        this.orderBy = orderBy;
    }

    //</editor-fold>

    //<editor-fold desc="Getters">

    public QueryConfiguration getQueryConfiguration() {
        return queryConfiguration;
    }

    public String getClauseConnector() {
        return clauseConnector;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public long getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(long maxRows) {
        this.maxRows = maxRows;
    }

    //</editor-fold>

    /**
     * Variant 2.<br/>
     * <pre>
     * SELECT o
     *   FROM aTable
     *        INNER JOIN aTable o2 ON o.id = o2.id
     *        INNER JOIN aTable o3 ON o.id = o3.id
     *  WHERE
     *        (
     *             LOWER(b1.field1) LIKE '%schiff%'
     *          OR LOWER(b1.field2) LIKE '%schiff%'
     *          OR LOWER(b1.field3) LIKE '%schiff%'
     *        )
     *        AND
     *        (
     *             LOWER(b2.field1) LIKE '%haus%'
     *          OR LOWER(b2.field2) LIKE '%haus%'
     *          OR LOWER(b2.field3) LIKE '%haus%'
     *        )
     * </pre>
     * @param builder StringBuilder for JPA query.
     */
    private void buildQueryVariant2(StringBuilder builder) {
        List<QueryParameter> queryParameters = queryConfiguration.getQueryParameters();
        // Count values
        Set<Object> _values = new HashSet<Object>();
        for (QueryParameter queryParameter : queryParameters) {
            Collections.addAll(_values, queryParameter.getValues());
        }
        Object[] values = _values.toArray(new Object[_values.size()]);
        int valueCount = values.length;
        // IDs for inner join, where condition
        List<String> ids = new ArrayList<String>();
        for (int i = 0, valuesSize = values.length; i < valuesSize; i++) {
            ids.add(String.format("o%d", i));
        }
        // INNER JOIN
        for (String id : ids) {
            builder.append(SPACE)
                    .append(INNER_JOIN).append(SPACE).append(queryConfiguration.getTableName()).append(SPACE).append(id)
                    .append(SPACE).append("ON o.id = ").append(id).append(".id");
        }
        // " WHERE"
        builder.append(SPACE).append(WHERE);
        // QueryParameters
        for (int valueCountIdx = 0; valueCountIdx < valueCount; valueCountIdx++) {
            // " ("
            builder.append(SPACE).append(LEFT_PARANTHESIS);
            for (int i = 0, queryParametersSize = queryParameters.size(); i < queryParametersSize; i++) {
                // Create named parameter and add it to parameter-map
                String paramName = String.format("%d", valueCountIdx + 1);
                parameters.put(paramName, values[valueCountIdx]);
                // "LOWER(<parameter name>) = LIKE :<paramName>"
                QueryParameter q = queryParameters.get(i);
                builder.append(JPA_LOWER).append(LEFT_PARANTHESIS).append(JPA_O_DOT).append(q.getParameterName()).append(RIGHT_PARANTHESIS)
                        .append(SPACE).append(LIKE).append(SPACE).append(QUESTION_MARK).append(paramName);
                // Add connector
                if (i < queryParametersSize - 1) {
                    builder.append(SPACE).append(OR).append(SPACE);
                }
            }
            // ")"
            builder.append(RIGHT_PARANTHESIS);
            // " OP"
            if (valueCountIdx < valueCount - 1) {
                builder.append(SPACE).append(AND);
            }
        }
    }

    /**
     * Variant 1.<br/>
     * <pre>
     * SELECT o
     *  FROM AnEntity o
     * WHERE
     *       (
     *                LOWER(o.field1) LIKE :field10
     *         AND/OR LOWER(o.field1) LIKE :field11
     *         AND/OR o.field1 IS NOT NULL
     *       )
     *       AND/OR
     *       (
     *                LOWER(o.field2) LIKE :field20
     *         AND/OR LOWER(o.field2) LIKE :field21
     *       )
     *       AND/OR ...
     * </pre>
     * @param builder StringBuilder for JPA query.
     */
    private void buildQueryVariant1(StringBuilder builder) {
        List<QueryParameter> queryParameters = queryConfiguration.getQueryParameters();
        int size = queryParameters.size();
        // " WHERE"
        builder.append(SPACE).append(WHERE);
        // QueryParameters
        for (int queryParameterIdx = 0, queryParametersSize = queryParameters.size(); queryParameterIdx < queryParametersSize; queryParameterIdx++) {
            QueryParameter q = queryParameters.get(queryParameterIdx);
            int valueCount = q.getValuesCount();
            if (valueCount > 0) {
                // Append clauseConnector between different QueryParameter
                if (queryParameterIdx > 0 && queryParameterIdx < size) {
                    // " OP"
                    builder.append(SPACE).append(clauseConnector);
                }
                buildVariant1Condition(builder, q, JPA_O);
            }
        }
    }

    /**
     * Build a JPA query based on list of QueryParameters.
     * @param selectClause SELECT-clause to start with.
     * @return StringBuilder containing JPA query.
     */
    private StringBuilder buildQuery(String selectClause) {
        // Build JPA query
        StringBuilder builder = new StringBuilder(selectClause);
        // Add conditionals
        List<QueryParameter> queryParameters = queryConfiguration.getQueryParameters();
        if (null != queryParameters) {
            int size = queryParameters.size();
            if (size > 0) {
                switch (queryConfiguration.getQueryVariant()) {
                    case Variant1:
                        buildQueryVariant1(builder);
                        break;
                    case Variant2:
                        buildQueryVariant2(builder);
                        break;
                }

            }
        }
        // ORDER BY
        addOrderBy(builder);
        return builder;
    }

    /**
     * Generate a JPA query to query for entities.
     * @param selectClause SELECT-clause to start with.
     * @return {@link TypedQuery}
     */
    public TypedQuery<T> getQuery(String selectClause) {
        TypedQuery<T> query = null;
        if (!queryConfiguration.isNativeQuery()) {
            StringBuilder builder = buildQuery(selectClause);
            query = entityManager.createQuery(builder.toString(), entityClass);
            bindQueryParameter(query);
        }
        return query;
    }

    /**
     * Generate a native query to query for entities.
     * @param selectClause SELECT-clause to start with.
     * @return {@link Query}
     */
    public Query getNativeQuery(String selectClause) {
        Query query = null;
        if (queryConfiguration.isNativeQuery()) {
            StringBuilder builder = buildQuery(selectClause);
            query = entityManager.createNativeQuery(builder.toString(), entityClass);
            bindNativeQueryParameter(query);
        }
        return query;
    }

    /**
     * Generate a JPA query to count entities.
     * @param selectClause SELECT-clause to start with.
     * @return {@link TypedQuery}
     */
    public TypedQuery<Long> getCountQuery(String selectClause) {
        TypedQuery<Long> countQuery = null;
        if (!queryConfiguration.isNativeQuery()) {
            StringBuilder builder = buildQuery(selectClause);
            countQuery = entityManager.createQuery(builder.toString(), Long.class);
            bindQueryParameter(countQuery);
        }
        return countQuery;
    }

    /**
     * Generate a native query to count entities.
     * @param selectClause SELECT-clause to start with.
     * @return {@link Query}
     */
    public Query getNativeCountQuery(String selectClause) {
        Query countQuery = null;
        if (queryConfiguration.isNativeQuery()) {
            StringBuilder builder = buildQuery(selectClause);
            countQuery = entityManager.createNativeQuery(builder.toString());
            bindNativeQueryParameter(countQuery);
        }
        return countQuery;
    }

    /**
     * Add ORDER BY clause.
     * @param builder StringBuilder for JPA query.
     */
    private void addOrderBy(StringBuilder builder) {
        // ORDER BY
        if (builder.length() > 0 && null != orderBy) {
            builder.append(SPACE).append(ORDER_BY).append(SPACE);
            for (int i = 0, orderByLength = orderBy.length; i < orderByLength; i++) {
                String o = orderBy[i];
                builder.append(o);
                if (i < orderByLength - 1) {
                    builder.append(COMMA).append(SPACE);
                }
            }
        }
    }

    /**
     * <pre>
     * (
     *          LOWER(o.field1) LIKE :field10
     *   AND/OR LOWER(o.field1) LIKE :field11
     *   AND/OR o.field1 IS NOT NULL
     * )
     * </pre>
     * @param builder     StringBuilder for JPA query.
     * @param q           {@link QueryParameter}.
     * @param entityAlias Alias for entity, e.g. "o" or "o1".
     */
    private void buildVariant1Condition(StringBuilder builder, QueryParameter q, String entityAlias) {
        // " ("
        builder.append(SPACE).append(LEFT_PARANTHESIS);
        //
        String op;
        Object[] values = q.getValues();
        int valueCount = q.getValuesCount();
        for (int queryValueIdx = 0, valuesLength = values.length; queryValueIdx < valuesLength; queryValueIdx++) {
            Object v = values[queryValueIdx];
            // Create named parameter and add it to parameter-map
            String paramName = String.format("%s%d", q.getParameterName(), queryValueIdx);
            parameters.put(paramName, v);
            //
            if (v instanceof String) {
                op = null != q.getOperator() ? q.getOperator() : LIKE;
                if (op.equals(LIKE)) {
                    // "LOWER(<entityAlias>.<property>) LIKE :<named parameter>"
                    builder.append(JPA_LOWER).append(LEFT_PARANTHESIS).
                            append(entityAlias).append(DOT).append(q.getParameterName()).
                            append(RIGHT_PARANTHESIS).
                            append(SPACE).append(op).append(SPACE).append(COLON).append(paramName);
                }
            } else if (v instanceof Date) {
                op = null != q.getOperator() ? q.getOperator() : EQUAL_SIGN;
                // "<entityAlias>.<property> = :<named parameter>) OP :<named parameter>"
                builder.append(entityAlias).append(DOT).append(q.getParameterName()).
                        append(RIGHT_PARANTHESIS).
                        append(SPACE).append(op).append(SPACE).append(COLON).append(paramName);
            }
            // " OP "
            if (queryValueIdx < valueCount - 1) {
                builder.append(SPACE).append(q.getConnector()).append(SPACE);
            }
        }
        if (q.isAddIsNotNull()) {
            // " OR <entityAlias>.<property> IS NOT NULL"
            builder.append(SPACE).append(OR).append(SPACE).
                    append(entityAlias).append(DOT).append(q.getParameterName()).append(SPACE).append(IS_NOT_NULL);
        }
        // ")"
        builder.append(RIGHT_PARANTHESIS);
    }

    /**
     * Bind QueryParameter to previously generated JPA query.
     * @param query {@link Query}, the JPA query.
     */
    private void bindQueryParameter(Query query) {
        if (parameters.size() > 0) {
            for (String k : parameters.keySet()) {
                Object val = parameters.get(k);
                if (val instanceof String) {
                    // TODO Make wildcards configurable for LIKE
                    query.setParameter(k, String.format("%%%s%%", val));
                } else {
                    query.setParameter(k, val);
                }
            }
        }
    }

    /**
     * Bind QueryParameter to previously generated JPA query.
     * @param query {@link Query}, the JPA query.
     */
    private void bindNativeQueryParameter(Query query) {
        if (parameters.size() > 0) {
            for (String k : parameters.keySet()) {
                int p = Integer.valueOf(k);
                Object val = parameters.get(k);
                if (val instanceof String) {
                    // TODO Make wildcards configurable for LIKE
                    query.setParameter(p, String.format("%%%s%%", val));
                } else {
                    query.setParameter(p, val);
                }
            }
        }
    }

}
