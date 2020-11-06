package com.algonquincollege.cst8277.models;

public interface Auditable {
    public Audit getAudit();
    public void setAudit(Audit audit);
}
