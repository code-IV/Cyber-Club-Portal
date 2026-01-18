package com.cyberclub.identity.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cyberclub.identity.repository.MembershipRepo;

@Service
public class MembershipService {
    private final MembershipRepo repo;

    public MembershipService(MembershipRepo repo){
        this.repo = repo;
    }

    public void saveMembership(UUID userId){
        repo.createDefaultMembership(userId);
    }
}
