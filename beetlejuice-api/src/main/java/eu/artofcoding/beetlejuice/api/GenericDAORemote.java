/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 7/10/12 4:24 PM
 */

package eu.artofcoding.beetlejuice.api;

import javax.ejb.Remote;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Remote
public interface GenericDAORemote<T extends Serializable> {

    /**
     * Create an entity.
     * @param entity Entity to persist.
     * @return T The entity.
     */
    T create(T entity) throws Exception;

    /**
     * Update an entity.
     * @param entity Entity to update.
     */
    T update(T entity);

    /**
     * Delete an entity.
     * @param entity Entity to delete.
     */
    boolean delete(T entity);

    /**
     * Get an entity by its id.
     * @param id ID of entity to find.
     * @return T Found entity, if any.
     */
    T findById(Long id);

    /**
     * Find all entities.
     * @return List with all entities.
     */
    List<T> findAll();

    /**
     * Find a single entity by a parameterized named query.
     * @param namedQuery Name of named query, {@link javax.persistence.EntityManager#createNamedQuery(String)}.
     * @param parameters Parameters.
     * @return Result, see {@link javax.persistence.TypedQuery#getResultList()}.
     */
    List<T> findAll(String namedQuery, Map<String, Object> parameters);

    /**
     * Find a single entity by a parameterized named query.
     * @param namedQuery Name of named query, {@link javax.persistence.EntityManager#createNamedQuery(String)}.
     * @param parameters Parameters.
     * @return Single result, see {@link javax.persistence.TypedQuery#getSingleResult()}.
     */
    T findOne(String namedQuery, Map<String, Object> parameters);

    /**
     * Convenience method to find one entity by named query but wihtout parameter.
     * @param namedQuery Name of named query, {@link javax.persistence.EntityManager#createNamedQuery(String)}.
     * @return Single result, see {@link javax.persistence.TypedQuery#getSingleResult()}.
     */
    T findOne(String namedQuery);

    /**
     * Dynamically create a query with WHERE ... <clauseConnector> ... clauses of all parameters.
     * @param parameters
     * @return
     */
    List<T> dynamicFindWith(Map<String, Object> parameters, String clauseConnector);

    /**
     * Dynamically create a query with WHERE ... <clauseConnector> ... clauses of all parameters.
     * @param parameters Map with parameters.
     * @return Result, see {@link javax.persistence.TypedQuery#getResultList()}.
     */
    List<T> dynamicFindWith(Map<String, Object> parameters, String clauseConnector, int firstResult, int pageSize);

    /**
     * Count all entities.
     * @return long Count of entites.
     */
    long countAll();

}
