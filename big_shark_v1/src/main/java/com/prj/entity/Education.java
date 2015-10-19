package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "education")
public class Education extends BaseEntity {

    private Long founderId;
    private String college;
    private String major;
    private String startDate;
    private String endDate;

    @Column(name = "founder_id", nullable = false)
    public Long getFounderId() {
        return founderId;
    }

    public void setFounderId(Long founderId) {
        this.founderId = founderId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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
