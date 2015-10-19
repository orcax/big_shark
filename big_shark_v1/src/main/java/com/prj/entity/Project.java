package com.prj.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    public enum Phase {
        // 草稿, 海选中, 初审中, 终审中, 已备案
        DRAFT, AUDITION, FIRST_REVIEW, FINAL_REVIEW, ACCEPTED
    }

    public enum Status {
        // 正常, 待观察, 垃圾箱
        NORMAL, WATCHING, RUBBISH
    }


    public enum InvestType {
        // 种子孵化, 天使投资
        SEED, ANGEL
    }

    private long createrId; // 项目创建者
    private String contactName; // 项目负责人
    private String contactEmail; // 联系邮箱
    private String contactMobile; // 常用手机号码
    private String name; // 项目名称
    private Date startDate; // 开始日期
    private String city; // 核心团队所在城市
    private String type; // 项目类型（多选）
    private String description; // 项目简介
    private Phase phase = Phase.DRAFT; // 项目当前阶段
    private Status status = Status.NORMAL; // 项目状态
    private boolean notified = false; // 是否通知
    private InvestType investType = null; // 投资类型 - 初审
    private int fileCount = 0; // 附件数量

    @Column(name = "creater_id", nullable = false)
    @JsonIgnore
    public long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(long createrId) {
        this.createrId = createrId;
    }

    @Column(name = "contact_name", nullable = false)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Column(name = "contact_email", nullable = false)
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Column(name = "contact_mobile", nullable = false)
    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "start_date", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(nullable = false)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(nullable = false)
    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Column(name = "invest_type")
    @Enumerated(value = EnumType.STRING)
    public InvestType getInvestType() {
        return investType;
    }

    public void setInvestType(InvestType investType) {
        this.investType = investType;
    }

    @Column(name = "file_count", nullable = false)
    @JsonIgnore
    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public void incrementFileCount() {
        ++this.fileCount;
    }

    public void decrementFileCount() {
        --this.fileCount;
    }

}
