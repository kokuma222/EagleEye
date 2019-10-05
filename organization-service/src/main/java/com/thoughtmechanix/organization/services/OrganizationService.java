package com.thoughtmechanix.organization.services;

import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private Tracer tracer;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public Organization getOrg(String organizationId) {
        Span newSpan = tracer.createSpan("getOrgDBCall");

        logger.debug("In the organizationService.getOrg() call");

        try {
            return orgRepo.findById(organizationId);
        } finally {
            newSpan.tag("peer.service", "mysql");
            newSpan.logEvent(Span.CLIENT_RECV);
            tracer.close(newSpan);
        }

//        return orgRepo.findById(organizationId);
    }

    public void saveOrg(Organization org) {
        org.setId(UUID.randomUUID().toString());
        orgRepo.save(org);
    }

    public void updateOrg(Organization org) {
        orgRepo.save(org);
    }

    public void deleteOrg(Organization org) {
        orgRepo.delete(org.getId());
    }
}
