package com.example.lostandfound.repository;

import com.example.lostandfound.model.Claim;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends MongoRepository<Claim, String> {
    List<Claim> findByItemId(String itemId);
    List<Claim> findByClaimantUsername(String username);
}
