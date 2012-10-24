/*
 * beetlejuice
 * beetlejuice-web-primefaces
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.09.12 20:42
 */
package eu.artofcoding.beetlejuice.web.primefaces;

import eu.artofcoding.beetlejuice.api.GenericDAORemote;
import eu.artofcoding.beetlejuice.api.GenericEntity;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @param <T>
 */
public abstract class AbstractPrimefacesLazyDataModel<T extends GenericEntity> extends LazyDataModel<T> {

    private static final Logger logger = Logger.getLogger(AbstractPrimefacesLazyDataModel.class.getName());

    protected GenericDAORemote<T> genericDAO;

    protected AbstractPrimefacesLazyDataModel(GenericDAORemote<T> genericDAO) {
        this.genericDAO = genericDAO;
    }

    protected GenericDAORemote<T> getGenericDAO() {
        return genericDAO;
    }

    protected void setGenericDAO(GenericDAORemote<T> genericDAO) {
        this.genericDAO = genericDAO;
    }

    @Override
    public Object getRowKey(GenericEntity entity) {
        logger.fine("getRowKey(" + entity.getId() + ")");
        return entity;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filter) {
        logger.fine("Entity " + genericDAO.getEntityClass().getName() + " " + this.getClass().getName() + "#load(first=" + first + ", pageSize=" + pageSize + ", sortField=" + sortField + ", sortOrder=" + sortField + ", filter=" + filter + ")");
        Map<String, Object> map = new HashMap<>();
        for (String k : filter.keySet()) {
            String value = filter.get(k);
            // Check for special literal to denote boolean value
            // Needed because of parameter Map<String, String> filter
            if (value.equals("beetlejuice:BOOL:true")) {
                map.put(k, Boolean.TRUE);
            } else if (value.equals("beetlejuice:BOOL:false")) {
                map.put(k, Boolean.FALSE);
            } else {
                map.put(k, value);
            }
        }
        List<T> entities = genericDAO.dynamicFindWith(map, "AND", first, pageSize);
        return entities;
    }

    @Override
    public int getRowCount() {
        int count = (int) genericDAO.countAll();
        return count;
    }

}
