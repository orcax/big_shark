package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "project_file")
public class ProjectFile extends BaseEntity {

    public enum Kind {
        DOC, JPG, PDF
    }

    private long projectId;
    private String name;
    private Kind kind;
    private long size;

    @Column(name = "project_id", nullable = false)
    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    @Column(nullable = false)
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
