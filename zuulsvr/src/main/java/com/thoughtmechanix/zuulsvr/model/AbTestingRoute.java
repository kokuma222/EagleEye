package com.thoughtmechanix.zuulsvr.model;

public class AbTestingRoute {
    private String serviceName;
    private String active;
    private String endpoint;
    private Integer weight;

    public String getServiceName() {
        return serviceName;
    }

    public String getActive() {
        return active;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
