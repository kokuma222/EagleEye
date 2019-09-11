package com.thoughtmechanix.authentication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_orgs")
public class UserOrganization {
    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    @Id
    @Column(name = "user_name", nullable = false)
    private String userName;

    public String getOrganizationId() {
        return organizationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
