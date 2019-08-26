package com.thoughtmechanix.organization.services;

import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepo;

    public Organization getOrg(String organizationId) {
        return orgRepo.findById(organizationId);
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
