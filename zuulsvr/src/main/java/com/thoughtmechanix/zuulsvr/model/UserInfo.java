package com.thoughtmechanix.zuulsvr.model;

public class UserInfo {
    private String organizationId;
    private String userId;

    public String getOrganizationId() {
        return organizationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
