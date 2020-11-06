package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Audit {

    protected LocalDateTime createdDate;
    protected LocalDateTime updatedDate;

    public Audit() {
    }

    @Column(name = "CREATED_DATE")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "UPDATED_DATE")
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

}