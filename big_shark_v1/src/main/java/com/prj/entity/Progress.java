package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "progress")
public class Progress extends BaseEntity {

    public enum EventType {
        // 版本更新, 用户推广, 媒体报道, 融资洽谈, 业务合作, 比赛获奖
        VERSION_UPDATE, USER_PROMOTE, MEDIA_COVERAGE, FINANCE_NEGOTIATE, BUSINESS_COOPERATE, CONTEST_AWARD
    }

    private Long projectId;
    private EventType eventType;
    private String description;

    @Column(name = "project_id", nullable = false)
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
