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
import eu.artofcoding.beetlejuice.jpa.GenericDAOResult;
import org.primefaces.event.SelectEvent;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Deriving class must provide a parameter-less constructor.
 * @param <T> Entity class.
 */
public abstract class AbstractCrudBean<T extends GenericEntity> implements Serializable {

    private static final Logger logger = Logger.getLogger(AbstractCrudBean.class.getName());

    protected String name;

    protected T entity;

    protected Class<T> entityClass;

    /**
     * An ID got from HTTP reuqest, used with preRenderView().
     */
    private Long idFromRequest;

    protected GenericDAORemote<T> genericDAO;

    protected AbstractPrimefacesLazyDataModel<T> primefacesLazyDataModel;

    protected AbstractCrudBean(String name, Class<T> entityClass) {
        this.name = name;
        this.entityClass = entityClass;
    }

    public GenericDAORemote<T> getGenericDAO() {
        return genericDAO;
    }

    public void setGenericDAO(GenericDAORemote<T> genericDAO) {
        this.genericDAO = genericDAO;
    }

    public AbstractPrimefacesLazyDataModel<T> getPrimefacesLazyDataModel() {
        primefacesLazyDataModel.setGenericDAO(genericDAO);
        return primefacesLazyDataModel;
    }

    public void setPrimefacesLazyDataModel(AbstractPrimefacesLazyDataModel<T> primefacesLazyDataModel) {
        this.primefacesLazyDataModel = primefacesLazyDataModel;
    }

    public T getEntity() {
        if (null == entity) {
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public boolean entityHasId() {
        return null != entity && null != entity.getId() && entity.getId() > 0;
    }

    public boolean wasUpdated() {
        return null != entity && null != entity.getVersion() && entity.getVersion() > 0;
    }

    public Long getIdFromRequest() {
        return idFromRequest;
    }

    public void setIdFromRequest(Long idFromRequest) {
        this.idFromRequest = idFromRequest;
    }

    protected GenericDAOResult persist() throws Exception {
        try {
            Long id = entity.getId();
            // Entities' ID is null or 0
            if (null == id || id == 0) { // null == id || (null != id && id == 0)
                genericDAO.create(entity);
                return GenericDAOResult.OK;
            }
            // Entities' ID is > 0
            else if (id > 0) { // null != id && id > 0
                entity = genericDAO.update(entity);
                return GenericDAOResult.OK;
            } else {
                return GenericDAOResult.ERROR;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDirectURL() {
        return "edit_" + name + ".xhtml?id=" + entity.getId();
    }

    /**
     * Process pre-render-view event.
     * E.g. use it to provide a request to managed bean:
     * <p>
     * &lt;f:metadata><br/>
     * &lt;f:viewParam name="id" value="#{bean.idFromRequest}"/><br/>
     * &lt;f:event type="preRenderView" listener="#{bean.preRenderView}"/><br/>
     * &lt;/f:metadata>
     * </p>
     * @throws IllegalAccessException ... when calling entityClass.newInstance().
     * @throws InstantiationException ... when calling entityClass.newInstance().
     */
    public void preRenderView() throws IllegalAccessException, InstantiationException {
        if (null != idFromRequest && idFromRequest > 0) {
            entity = genericDAO.findById(idFromRequest);
        } else if (null != idFromRequest && idFromRequest == 0) {
            entity = entityClass.newInstance();
        }
        logger.info("preRenderView: Got ID from request=" + idFromRequest + " entity=" + entity);
    }

    /**
     * Double-click in PrimeFaces data table.
     * @param event org.primefaces.event.SelectEvent
     */
    public void onRowDblselect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        /*
        Flash flash = context.getExternalContext().getFlash();
        flash.put(name, event.getObject());
        */
        ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
        handler.performNavigation("edit_" + name);
    }

    /**
     * Create, log and return a navigation case.
     * @param navCase navCase is used as a prefix to bean's name, e.g. 'show' becomes 'show_bean'.
     * @return String The navigation case.
     */
    protected String navigate(String navCase) {
        String destination = navCase + "_" + name;
        logger.info("navigate: Navigation case is " + destination);
        return destination;
    }

}
