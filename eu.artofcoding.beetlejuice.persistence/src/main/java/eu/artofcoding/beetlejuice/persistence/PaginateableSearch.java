/*
 * beetlejuice
 * beetlejuice-persistence
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 03.09.12 10:20
 */

package eu.artofcoding.beetlejuice.persistence;

import eu.artofcoding.beetlejuice.api.persistence.GenericEntity;
import eu.artofcoding.beetlejuice.api.persistence.QueryConfiguration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PaginateableSearch<T extends GenericEntity> implements Serializable {

    //<editor-fold desc="Members">

    /**
     * DAO.
     */
    private GenericDAO<T> dao;

    /**
     * The search term.
     */
    private String searchTerm;

    /**
     * Named query.
     */
    private String namedQuery;

    /**
     * Parameters for named query.
     */
    private Map<String, Object> queryParameterMap;

    /**
     * Offset.
     */
    private int offset;

    /**
     * Size of page (currentPage to fetch).
     */
    private int pageSize = 25;

    /**
     * Turn-around mode?
     * Decides about how pages are turned at beginning or end, e.g.:
     * last page + 1 -> first page or last page.
     */
    private boolean turnAroundMode;

    /**
     * Number of pages depending on total row count
     */
    private int pageCount;

    /**
     * Total row count for query.
     */
    private long totalRowCount;

    /**
     * Maximum number of rows to show.
     */
    private long maxRowCount;

    /**
     * Index of current page in relation to total result set.
     */
    private int currentPageIndex;

    /**
     * List with entities on current page, size <= pageSize.
     */
    private List<T> currentPage;

    /**
     * Index of selected entity, 0 <= index < pageSize.
     */
    private int indexOnCurrentPage;

    /**
     * Index of selected entity, 0 <= index < totalRowCount.
     */
    private int selectedEntityTotalIndex;

    /**
     * Selected entity reference.
     */
    private T selectedEntity;

    private QueryConfiguration queryConfiguration;

    private String clauseConnector;

    private String[] orderBy;

    //</editor-fold>

    /**
     * Constructor.
     * @param dao DAO to use for queries, <code>instanceof GenericDAO&lt;T></code>.
     */
    public PaginateableSearch(GenericDAO<T> dao) {
        this.dao = dao;
    }

    public boolean isTurnAroundMode() {
        return turnAroundMode;
    }

    public void setTurnAroundMode(boolean turnAroundMode) {
        this.turnAroundMode = turnAroundMode;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public List<T> executeSearch(String namedQuery, Map<String, Object> parameters) {
        this.namedQuery = namedQuery;
        this.queryParameterMap = parameters;
        this.clauseConnector = null;
        // Execute named query (first page)
        offset = 0;
        currentPage = dao.findAll(namedQuery, parameters, offset, pageSize);
        // Count rows, pages
        totalRowCount = dao.countNamedQuery(namedQuery, parameters);
        pageCount = Math.max(1, (int) Math.ceil(1.0 * totalRowCount / pageSize));
        // Return first page
        return currentPage;
    }

    public List<T> executeSearch(QueryConfiguration queryConfiguration, String clauseConnector, String[] orderBy) {
        this.namedQuery = null;
        this.queryParameterMap = null;
        this.queryConfiguration = queryConfiguration;
        this.clauseConnector = clauseConnector;
        this.orderBy = orderBy;
        // Execute query (first page)
        offset = 0;
        if (queryConfiguration.isNativeQuery()) {
            currentPage = dao.dynamicFindWith(queryConfiguration, clauseConnector, orderBy, offset, pageSize);
        } else {
            currentPage = dao.dynamicFindWith(queryConfiguration, clauseConnector, orderBy, offset, pageSize);
        }
        // Count rows, pages
        totalRowCount = dao.dynamicCountWith(queryConfiguration, clauseConnector);
        pageCount = Math.max(1, (int) Math.ceil(1.0 * totalRowCount / pageSize));
        // Return first page
        return currentPage;
    }

    public List<T> executeSearch(QueryConfiguration queryConfiguration, String clauseConnector) {
        return executeSearch(queryConfiguration, clauseConnector, null);
    }

    public void gotoPage(int page) {
        // Set page index
        currentPageIndex = page;
        // Calculate offset in result set
        offset = currentPageIndex * pageSize;
        // Execute query for page
        if (queryConfiguration.isNativeQuery()) {
            currentPage = dao.dynamicFindWith(queryConfiguration, clauseConnector, orderBy, offset, pageSize);
        } else {
            // Named query
            if (null != namedQuery && null != queryParameterMap) {
                currentPage = dao.findAll(namedQuery, queryParameterMap, offset, pageSize);
            } else if (null != queryConfiguration.getQueryParameters() && null != clauseConnector) {
                currentPage = dao.dynamicFindWith(queryConfiguration, clauseConnector, orderBy, offset, pageSize);
            }
        }
        // Reset pointer
        indexOnCurrentPage = 0;
        selectedEntityTotalIndex = offset;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public long getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(long maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    //<editor-fold desc="Pagination">

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets a new page size.
     * If we have executed the query before, go to first page.
     * @param pageSize New page size.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (totalRowCount > 0) {
            gotoPage(0);
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<T> getCurrentPage() {
        return currentPage;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public int getSelectedEntityTotalIndex() {
        return selectedEntityTotalIndex;
    }

    public T getSelectedEntity() {
        return selectedEntity;
    }

    public T selectEntityById(Long id) {
        selectedEntity = null;
        // Find entity on current page
        T entity = null;
        for (int _indexOnCurrentPage = 0; _indexOnCurrentPage < currentPage.size(); _indexOnCurrentPage++) {
            entity = currentPage.get(_indexOnCurrentPage);
            if (entity.getId().equals(id)) {
                selectedEntity = entity;
                indexOnCurrentPage = _indexOnCurrentPage;
                selectedEntityTotalIndex = currentPageIndex * pageSize + indexOnCurrentPage;
            }
        }
        // TODO Entity not found on current page
        if (null == selectedEntity) {
        }
        return selectedEntity;
    }

    public T selectEntityByIndex(Long index) {
        selectedEntity = null;
        // Calculate page for index
        int neededPage = (int) (index / pageSize);
        gotoPage(neededPage);
        return selectedEntity;
    }

    private void pointToFirstEntityOnCurrentPage() {
        indexOnCurrentPage = 0;
        selectedEntity = currentPage.get(indexOnCurrentPage);
        selectedEntityTotalIndex = currentPageIndex * pageSize + indexOnCurrentPage;
    }

    private void pointToLastEntityOnCurrentPage() {
        indexOnCurrentPage = currentPage.size() - 1;
        selectedEntityTotalIndex = currentPageIndex * pageSize + indexOnCurrentPage;
    }

    private void pointToFirstEntityOnFirstPage() {
        selectedEntityTotalIndex = 0;
        indexOnCurrentPage = 0;
    }

    private void pointToLastEntityOnLastPage() {
        selectedEntityTotalIndex = (int) totalRowCount;
        indexOnCurrentPage = (int) totalRowCount - 1;
    }

    public boolean pointerIsAtLastEntity() {
        boolean onLastPage = currentPageIndex == pageCount - 1;
        boolean afterLastEntity = currentPageIndex * pageSize + indexOnCurrentPage == totalRowCount;
        return onLastPage && afterLastEntity;
    }

    public boolean hasNextPage() {
        return turnAroundMode || currentPageIndex < pageCount - 1;
    }

    public void gotoNextPage() {
        int _offset = offset + pageSize;
        // Out of bounds...
        if (_offset > totalRowCount) {
            outOfBoundsNext();
        } else {
            int pageNumber = _offset / pageSize;
            gotoPage(pageNumber);
            // Position pointer on first entity on current page (we're coming from the bottom of the previous page)
            pointToFirstEntityOnCurrentPage();
        }
    }

    public T next() {
        indexOnCurrentPage++;
        selectedEntityTotalIndex++;
        // Check page: go to next page?
        if (indexOnCurrentPage == pageSize || pointerIsAtLastEntity()) { // selectedEntityTotalIndex > totalRowCount
            gotoNextPage();
        }
        selectedEntity = currentPage.get(indexOnCurrentPage);
        return selectedEntity;
    }

    private void outOfBoundsNext() {
        if (turnAroundMode) {
            // Position pointer on first entity on first page (we're coming from the bottom of last page)
            selectedEntityTotalIndex = 0;
            gotoPage(0);
        } else {
            gotoPage(pageCount - 1);
            // Position pointer on last entity on last page
            indexOnCurrentPage = currentPage.size() - 1;
            selectedEntityTotalIndex = (int) totalRowCount - 1;
        }
    }

    public boolean hasPreviousPage() {
        return turnAroundMode || currentPageIndex > 0;
    }

    public void gotoPreviousPage() {
        int _offset = offset - pageSize;
        // Out of bounds...
        if (_offset < 0) {
            if (selectedEntityTotalIndex < 0) {
                if (turnAroundMode) {
                    // Position pointer on last entity on last page (turn around)
                    pointToLastEntityOnLastPage();
                } else {
                    // Position pointer on first entity on first page (we can't go back anymore)
                    pointToFirstEntityOnFirstPage();
                }
            }
        } else {
            int pageNumber = _offset / pageSize;
            gotoPage(pageNumber);
            // Position pointer on last entity on page (we're coming from the top of the next page)
            pointToLastEntityOnCurrentPage();
        }
    }

    public T previous() {
        indexOnCurrentPage--;
        selectedEntityTotalIndex--;
        // Check page: go to previous page 
        if (indexOnCurrentPage < 0) {
            gotoPreviousPage();
        }
        selectedEntity = currentPage.get(indexOnCurrentPage);
        return selectedEntity;
    }

    //</editor-fold>

}
