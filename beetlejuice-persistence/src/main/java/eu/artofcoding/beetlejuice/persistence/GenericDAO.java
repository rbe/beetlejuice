/*
 * beetlejuice
 * beetlejuice-persistence
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 13.07.12 15:05
 */

package eu.artofcoding.beetlejuice.persistence;

import eu.artofcoding.beetlejuice.api.persistence.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.*;

/**
 * @param <T> Subtype {@link eu.artofcoding.beetlejuice.api.persistence.GenericEntity} to act as DAO for.
 */
@SuppressWarnings({"EjbInterceptorInspection"})
public abstract class GenericDAO<T extends GenericEntity> implements GenericDAORemote<T> {

    private static final Logger logger = Logger.getLogger(GenericDAO.class.getName());

    /**
     * Persistence context.
     */
    //@PersistenceContext
    protected EntityManager entityManager;

    /**
     * Class of entity.
     */
    private Class<T> entityClass;

    /**
     * Constructor.
     * @param entityClass Class of entity.
     */
    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        T _entity = entityManager.merge(entity);
        return _entity;
    }

    @Override
    public boolean delete(T entity) {
        T _entity = entityManager.merge(entity);
        entityManager.remove(entity);
        return true;
    }

    @Override
    public T findById(Long id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> findAll(String namedQuery, Map<String, Object> parameters) {
        List<T> result = null;
        try {
            TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, entityClass);
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }
            result = query.getResultList();
        } catch (Exception e) {
            StringBuilder builder = listQueryParameters(parameters);
            logger.log(Level.WARNING, String.format("%s#findAll(%s, {%s}): %s", entityClass, namedQuery, builder.toString(), e.getMessage()), e);
        }
        return result;
    }

    @Override
    public List<T> findAll(String namedQuery, Map<String, Object> parameters, int firstResult, int pageSize) {
        List<T> result = null;
        try {
            TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, entityClass);
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }
            // Pagination: set first result and page size
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            // Execute query
            result = query.getResultList();
        } catch (Exception e) {
            StringBuilder builder = listQueryParameters(parameters);
            logger.log(Level.WARNING, String.format("%s#findAll(%s, {%s}, %d, %d): %s", entityClass, namedQuery, builder.toString(), firstResult, pageSize, e.getMessage()), e);
        }
        return result;
    }

    @Override
    public T findOne(String namedQuery, Map<String, Object> parameters) {
        T result = null;
        try {
            TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, entityClass);
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }
            result = query.getSingleResult();
        } catch (Exception e) {
            StringBuilder builder = listQueryParameters(parameters);
            logger.log(Level.WARNING, String.format("%s#findOne(%s, {%s}): %s", entityClass, namedQuery, builder.toString(), e.getMessage()), e);
        }
        return result;
    }

    @Override
    public T findOne(String namedQuery) {
        return findOne(namedQuery, null);
    }

    @Override
    public List<T> dynamicFindWith(Map<String, Object> parameters, String clauseConnector, int firstResult, int pageSize) {
        // Build JPA query
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_O_FROM).append(SPACE).append(entityClass.getSimpleName()).append(SPACE).append(JPA_O);
        // Add conditionals
        if (null != parameters) {
            int keyCount = parameters.size();
            if (keyCount > 0) {
                builder.append(SPACE).append(WHERE);
                int counter = 0;
                for (String k : parameters.keySet()) {
                    Object o = parameters.get(k);
                    if (o instanceof QueryParameter) {
                        QueryParameter q = (QueryParameter) o;
                        int valueCount = q.getValues().length;
                        Object[] values = q.getValues();
                        for (int i = 0; i < values.length; i++) {
                            // " o.<property> LIKE :<named parameter>"
                            builder.append(SPACE).append(JPA_O_DOT).append(k).append(JPA_LIKE_COLON).append(k);
                            if (counter++ < valueCount - 1) {
                                builder.append(SPACE).append(q.getConnector());
                            }
                        }
                    } else {
                        // " o.<property> LIKE :<named parameter>"
                        builder.append(SPACE).append(JPA_O_DOT).append(k).append(JPA_LIKE_COLON).append(k);
                        if (counter++ < keyCount - 1) {
                            builder.append(SPACE).append(clauseConnector);
                        }
                    }
                }
            }
        }
        // Build query
        TypedQuery<T> query = entityManager.createQuery(builder.toString(), entityClass);
        populateQueryParameters(query, parameters);
        // Pagination: set first result and page size
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
        // Execute query and return result list
        List<T> entities = query.getResultList();
        return entities;
    }

    @Override
    public List<T> dynamicFindWith(Map<String, Object> parameters, String clauseConnector) {
        return dynamicFindWith(parameters, clauseConnector, 0, 1000);
    }

    @Override
    public long dynamicCountWith(Map<String, Object> parameters, String clauseConnector) {
        // Build JPA query
        StringBuilder builder = new StringBuilder();
        builder.append(JPA_SELECT_COUNT_O_FROM).append(entityClass.getSimpleName()).append(SPACE).append(JPA_O);
        // Add conditionals
        if (null != parameters && !parameters.isEmpty()) {
            int keyCount = parameters.size();
            if (keyCount > 0) {
                builder.append(SPACE).append(WHERE);
                int i = 0;
                for (String k : parameters.keySet()) {
                    // " o.<property> LIKE :<named parameter>"
                    builder.append(SPACE).append(JPA_O_DOT).append(k).append(JPA_LIKE_COLON).append(k);
                    if (i++ < keyCount - 1) {
                        builder.append(SPACE).append(clauseConnector);
                    }
                }
            }
        }
        // Build query
        TypedQuery<Long> query = entityManager.createQuery(builder.toString(), Long.class);
        populateQueryParameters(query, parameters);
        // Execute query
        return query.getSingleResult();
    }

    @Override
    public List<T> dynamicFindWith(QueryConfiguration queryConfiguration, String clauseConnector, String[] orderBy, int firstResult, int pageSize) {
        Query query;
        if (queryConfiguration.isNativeQuery()) {
            // Build query from list of QueryParameters
            String selectClause = String.format("SELECT * FROM %s o", queryConfiguration.getTableName());
            DynamicQuery<T> dynamicQuery = new DynamicQuery<T>(entityManager, entityClass, queryConfiguration, clauseConnector, orderBy);
            query = dynamicQuery.getNativeQuery(selectClause);
        } else {
            // Build query from list of QueryParameters
            String selectClause = String.format("%s %s %s", SELECT_O_FROM, entityClass.getSimpleName(), JPA_O);
            DynamicQuery<T> dynamicQuery = new DynamicQuery<T>(entityManager, entityClass, queryConfiguration, clauseConnector, orderBy);
            query = dynamicQuery.getQuery(selectClause);
        }
        // Pagination: set first result and page size
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
        // Execute query and return result list
        List<T> entities = query.getResultList();
        return entities;
    }

    @Override
    public long dynamicCountWith(QueryConfiguration queryConfiguration, String clauseConnector) {
        DynamicQuery<T> dynamicQuery = new DynamicQuery<T>(entityManager, entityClass, queryConfiguration, clauseConnector);
        Long count;
        if (queryConfiguration.isNativeQuery()) {
            // Build query from list of QueryParameters
            String selectClause = String.format("SELECT COUNT(*) FROM %s o", queryConfiguration.getTableName());
            // Execute query
            count = ((BigInteger) dynamicQuery.getNativeCountQuery(selectClause).getSingleResult()).longValue();
        } else {
            // Build query from list of QueryParameters
            String selectClause = String.format("%s%s %s", JPA_SELECT_COUNT_O_FROM, entityClass.getSimpleName(), JPA_O);
            // Execute query
            count = dynamicQuery.getCountQuery(selectClause).getSingleResult();
        }
        return count;
    }

    @Override
    public long countAll() {
        // Build JPA query
        StringBuilder builder = new StringBuilder();
        builder.append(JPA_SELECT_COUNT_O_FROM).append(entityClass.getSimpleName()).append(SPACE).append(JPA_O);
        // Create and execute query
        TypedQuery<Long> query = entityManager.createQuery(builder.toString(), Long.class);
        return query.getSingleResult();
    }

    @Override
    public int countNamedQuery(String namedQuery, Map<String, Object> parameters) {
        // Build query
        TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, entityClass);
        if (parameters != null && !parameters.isEmpty()) {
            populateQueryParameters(query, parameters);
        }
        // Execute query and get size of result
        int size = query.getResultList().size();
        return size;
    }

    @Override
    public void populateQueryParameters(Query query, Map<String, Object> parameters) {
        // Check arguments
        if (null != parameters && !parameters.isEmpty()) {
            // Populate query parameters
            Set<Map.Entry<String, Object>> entries = parameters.entrySet();
            if (!entries.isEmpty()) {
                Object value;
                for (Map.Entry<String, Object> entry : entries) {
                    value = entry.getValue();
                    if (value instanceof java.util.Date) {
                        query.setParameter(entry.getKey(), (java.util.Date) value, TemporalType.DATE);
                    } else if (value instanceof String) {
                        // Check for special literal to denote boolean value
                        // Needed because of parameter Map<String, String> filter in org.primefaces.model.LazyDataModel#load()
                        if (value.equals(BEETLEJUICE_BOOL_TRUE)) {
                            query.setParameter(entry.getKey(), Boolean.TRUE);
                        } else if (value.equals(BEETLEJUICE_BOOL_FALSE)) {
                            query.setParameter(entry.getKey(), Boolean.FALSE);
                        } else {
                            query.setParameter(entry.getKey(), value);
                        }
                    } else {
                        query.setParameter(entry.getKey(), value);
                    }
                }
            }
        }
    }

    /**
     * Build String with list of query parameters, used for logging.
     * @param parameters Map with key=named parameter, value.
     * @return StringBuilder.
     */
    private StringBuilder listQueryParameters(Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        if (null != parameters && !parameters.isEmpty()) {
            for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext(); ) {
                String k = iterator.next();
                builder.append(k).append(EQUAL_SIGN).append(parameters.get(k));
                if (builder.length() > 0 && iterator.hasNext()) {
                    builder.append(SPACE);
                }
            }
        }
        return builder;
    }

}
