package com.example.lostandfound.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "claims")
public class Claim {

    @Id
    private String id;

    private String itemId;
    private String claimantUsername;
    
    private String proofDetails;
    private String contactPhone;
    private String contactEmail;
    
    private ClaimStatus status = ClaimStatus.PENDING;
    
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ClaimStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
