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

package eu.artofcoding.beetlejuice.jpa;

import eu.artofcoding.beetlejuice.api.GenericDAORemote;
import eu.artofcoding.beetlejuice.api.GenericEntity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <T> {@link GenericEntity} to act as DAO for
 */
@SuppressWarnings({"EjbInterceptorInspection"})
public abstract class GenericDAO<T extends GenericEntity> implements GenericDAORemote<T> {

    private static final Logger logger = Logger.getLogger(GenericDAO.class.getName());

    /**
     * Persistence context.
     */
    @PersistenceContext
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
        // Build JQL query
        TypedQuery<T> query = null;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT o FROM ").append(entityClass.getSimpleName()).append(" o");
        // Add conditionals
        if (null != parameters) {
            int keyCount = parameters.size();
            if (keyCount > 0) {
                builder.append(" WHERE");
                int i = 0;
                for (String k : parameters.keySet()) {
                    // o.<property> LIKE :<named parameter>
                    builder.append(" o.").append(k).append(" LIKE :").append(k);
                    if (i++ < keyCount - 1) {
                        builder.append(" ").append(clauseConnector);
                    }
                }
            }
        }
        // Build query
        query = entityManager.createQuery(builder.toString(), entityClass);
        // Set parameters
        if (null != parameters && !parameters.isEmpty()) {
            for (String k : parameters.keySet()) {
                query.setParameter(k, parameters.get(k));
            }
        }
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
    public long countAll() {
        // Build JQL query
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(o) FROM ").append(entityClass.getSimpleName()).append(" o");
        // Build query
        TypedQuery<Long> query = entityManager.createQuery(builder.toString(), Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countAllWithCondition(Map<String, Object> parameters, String clauseConnector) {
        // Build JQL query
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(o) FROM ").append(entityClass.getSimpleName()).append(" o");
        // Add conditionals
        if (null != parameters && !parameters.isEmpty()) {
            int keyCount = parameters.size();
            if (keyCount > 0) {
                builder.append(" WHERE");
                int i = 0;
                for (String k : parameters.keySet()) {
                    // o.<property> LIKE :<named parameter>
                    builder.append(" o.").append(k).append(" LIKE :").append(k);
                    if (i++ < keyCount - 1) {
                        builder.append(" ").append(clauseConnector);
                    }
                }
            }
        }
        // Build query
        TypedQuery<Long> query = entityManager.createQuery(builder.toString(), Long.class);
        // Set parameters
        if (null != parameters && !parameters.isEmpty()) {
            for (String k : parameters.keySet()) {
                query.setParameter(k, parameters.get(k));
            }
        }
        // Execute query
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

    /**
     * Method that will populate parameters if they are passed not null and empty.
     * @param query      Previously created {@link javax.persistence.Query}.
     * @param parameters Map with parameters.
     */
    protected void populateQueryParameters(Query query, Map<String, Object> parameters) {
        Object val;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            val = entry.getValue();
            if (val instanceof java.util.Date) {
                query.setParameter(entry.getKey(), (java.util.Date) val, TemporalType.DATE);
            } else {
                query.setParameter(entry.getKey(), val);
            }
        }
    }

    /**
     * Build String with list of query parameters, used for logging.
     * @param parameters
     * @return StringBuilder.
     */
    private StringBuilder listQueryParameters(Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        if (null != parameters && !parameters.isEmpty()) {
            for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext(); ) {
                String k = iterator.next();
                builder.append(k).append("=").append(parameters.get(k));
                if (builder.length() > 0 && iterator.hasNext()) {
                    builder.append(" ");
                }
            }
        }
        return builder;
    }

}
