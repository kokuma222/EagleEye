package com.thoughtmechanix.licenses.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "Authorization";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORG_ID = "tmx-org-id";

    private static final ThreadLocal<String> correlationId= new ThreadLocal<>();
    private static final ThreadLocal<String> authToken= new ThreadLocal<>();
    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> orgId = new ThreadLocal<>();

    public String getCorrelationId() {
        return correlationId.get();
    }

    public String getAuthToken() {
        return authToken.get();
    }

    public String getUserId() {
        return userId.get();
    }

    public String getOrgId() {
        return orgId.get();
    }

    public void setCorrelationId(String cid) {
        correlationId.set(cid);
    }

    public void setAuthToken(String aToken) {
        authToken.set(aToken);
    }

    public void setUserId(String uid) {
        userId.set(uid);
    }

    public void setOrgId(String oid) {
        orgId.set(oid);
    }
}
