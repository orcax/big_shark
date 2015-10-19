package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "equity_file")
public class EquityFile extends BaseEntity {

    public enum Type {
        // 工商营业执照, 组织机构代码证, 税务登记证, 工商章程, 股东会决议, 投资协议, 年度审计报告资料
        BIZ_LICENSE, ORG_CODE_CERT, TAX_REG_CERT, CORP_ARTICLE, SHAREHOLDER_RESOLUTE, INVEST_AGREE, AUDIT_REPORT
    }

    public enum Kind {
        DOC, JPG, PDF
    }

    private Long equityId;
    private Type type;
    private String name;
    private Kind kind;
    private Long size;

    public Long getEquityId() {
        return equityId;
    }

    public void setEquityId(Long equityId) {
        this.equityId = equityId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
