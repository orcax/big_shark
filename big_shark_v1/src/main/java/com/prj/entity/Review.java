package com.prj.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.entity.Project.Phase;

@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    
    private long createrId;
    private long projectId;
    private Phase phase;
    private Boolean liked; // 赞 - 海选
    private String managerName; // 投资经理姓名 - 初审、终审
    private String content; // 评价 - 初审、终审
    private Boolean inclined; // 意向 - 初审、终审
    private String investAmount; // 最多投资额 - 终审
    private String assessedValue; // 最高估值 - 终审
    private Boolean invested; // 是否领投资 - 终审
    
    public Map<String, Object> toSimpleFormat() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", this.getId());
        data.put("phase", this.phase);
        data.put("managerName", this.managerName);
        data.put("createTime", this.createTime);
        if(this.phase == Phase.AUDITION) {
            data.put("liked", this.liked);
        }
        else if(this.phase == Phase.FIRST_REVIEW) {
            data.put("content", this.content);
            data.put("inclined", this.inclined);
        }
        else if(this.phase == Phase.FINAL_REVIEW) {
            data.put("content", this.content);
            data.put("inclined", this.inclined);
            data.put("investAmount", this.investAmount);
            data.put("assessedValue", this.assessedValue);
            data.put("invested", this.invested);
        }
        else if(this.phase == Phase.ACCEPTED) {
            data.put("content", this.content);
        }
        return data;
    }
    
    public Review simpleClone() {
        Review review = new Review();
        review.setCreaterId(this.getCreaterId());
        review.setManagerName(this.getManagerName());
        review.setProjectId(this.getProjectId());
        return review;
    }

    @Column(name = "creater_id", nullable = false)
    @JsonIgnore
    public long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(long createrId) {
        this.createrId = createrId;
    }

    @Column(name = "project_id", nullable = false)
    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Column(name = "manager_name")
    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getInclined() {
        return inclined;
    }

    public void setInclined(Boolean inclined) {
        this.inclined = inclined;
    }

    @Column(name = "invest_amount")
    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    @Column(name = "assessed_value")
    public String getAssessedValue() {
        return assessedValue;
    }

    public void setAssessedValue(String assessedValue) {
        this.assessedValue = assessedValue;
    }

    public Boolean getInvested() {
        return invested;
    }

    public void setInvested(Boolean invested) {
        this.invested = invested;
    }

}
