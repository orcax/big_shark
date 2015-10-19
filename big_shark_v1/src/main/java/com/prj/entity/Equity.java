package com.prj.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "equity")
public class Equity extends BaseEntity {

    public enum InvestType {
        // 原始股东, 增资, 股权转让
        ORIGINAL, ADDITIONAL, TRANSFER
    }

    public enum Currency {
        // 人民币, 美元
        RMB, USD
    }

    private Long projectId;
    private String investor;
    private Timestamp investTime;
    private InvestType investType;
    private Currency investCurrency;
    private String investAmount;
    private String estimateAmount;
    private String sharePercentage;
    private String board;

    @Column(name = "project_id", nullable = false)
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    @Column(name = "invest_time")
    public Timestamp getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Timestamp investTime) {
        this.investTime = investTime;
    }

    @Column(name = "invest_type")
    @Enumerated(value = EnumType.STRING)
    public InvestType getInvestType() {
        return investType;
    }

    public void setInvestType(InvestType investType) {
        this.investType = investType;
    }

    @Column(name = "invest_currency")
    @Enumerated(value = EnumType.STRING)
    public Currency getInvestCurrency() {
        return investCurrency;
    }

    public void setInvestCurrency(Currency investCurrency) {
        this.investCurrency = investCurrency;
    }

    @Column(name = "invest_amount")
    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    @Column(name = "estimate_amount")
    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    @Column(name = "share_percentage")
    public String getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(String sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

}
