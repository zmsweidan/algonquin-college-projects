package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditListener {

    @PrePersist
    public void setCreatedDate(Auditable auditable) {
        Audit audit = auditable.getAudit();
        audit.setCreatedDate(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdatedDate(Auditable auditable) {
        Audit audit = auditable.getAudit();
        audit.setUpdatedDate(LocalDateTime.now());
    }
}