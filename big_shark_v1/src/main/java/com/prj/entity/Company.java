package com.prj.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    public enum RegisterType {
        // 国内有限合伙, 中外合资, 其他
        NATIONAL, SINO_FOREIGN, OTHER
    }

    private Long projectId;
    private String name;
    private RegisterType registerType;
    private String registerAddress;
    private Timestamp registerTime;
    private String registerCapital;
    private String legalPerson;
    private String legalMobile;
    private String legalEmail;
    private String officeAddress;

    @Column(name = "project_id", nullable = false)
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "register_type")
    @Enumerated(value = EnumType.STRING)
    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    @Column(name = "register_address")
    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    @Column(name = "register_time")
    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    @Column(name = "register_capital")
    public String getRegisterCapital() {
        return registerCapital;
    }

    public void setRegisterCapital(String registerCapital) {
        this.registerCapital = registerCapital;
    }

    @Column(name = "legal_person")
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    @Column(name = "legal_mobile")
    public String getLegalMobile() {
        return legalMobile;
    }

    public void setLegalMobile(String legalMobile) {
        this.legalMobile = legalMobile;
    }

    @Column(name = "legal_email")
    public String getLegalEmail() {
        return legalEmail;
    }

    public void setLegalEmail(String legalEmail) {
        this.legalEmail = legalEmail;
    }

    @Column(name = "office_address")
    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

}
