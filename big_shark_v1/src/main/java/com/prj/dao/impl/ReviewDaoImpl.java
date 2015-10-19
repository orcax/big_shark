package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.ReviewDao;
import com.prj.entity.Review;

@Repository("ReviewDaoImpl")
public class ReviewDaoImpl extends BaseDaoHibernateImpl<Review, Long> implements
        ReviewDao {
    
    public ReviewDaoImpl() {
        super(Review.class);
    }

}
