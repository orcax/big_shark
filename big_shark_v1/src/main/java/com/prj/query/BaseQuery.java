package com.prj.query;

import java.util.List;

import org.hibernate.criterion.Criterion;

/**
 * The Class BaseQuery.
 */
public abstract class BaseQuery {

    /** The page number. */
    private int pageNumber = 1;

    /** The page size. */
    private int pageSize   = 20;

    /**
     * Gets the conditions.
     *
     * @return the conditions
     */
    public abstract List<Criterion> getConditions();

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the new page number
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
