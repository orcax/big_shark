package com.prj.query;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.prj.entity.Account.Role;

/**
 * The Class CustomerQuery.
 */
public class CustomerQuery extends BaseQuery {

    /** The is active. */
    private boolean active = true;

    /** The role. */
    private Role    role   = Role.CUSTOMER;

    /** 
     * @see com.prj.query.BaseQuery#getConditions()
     */
    @Override
    public List<Criterion> getConditions() {
        List<Criterion> conditions = new ArrayList<Criterion>();
        conditions.add(Restrictions.eq("active", active));
        conditions.add(Restrictions.eq("role", role));
        return conditions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
