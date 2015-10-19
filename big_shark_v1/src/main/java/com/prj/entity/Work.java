package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "work")
public class Work extends BaseEntity {

    private Long founderId;
    private String company;
    private String position;
    private String startDate;
    private String endDate;

    @Column(name = "founder_id", nullable = false)
    public Long getFounderId() {
        return founderId;
    }

    public void setFounderId(Long founderId) {
        this.founderId = founderId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "start_date")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
