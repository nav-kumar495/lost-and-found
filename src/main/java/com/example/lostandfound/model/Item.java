package com.example.lostandfound.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "items")
public class Item {

    @Id
    private String id;

    private String title;
    private String description;
    private String location;
    private LocalDate dateLostOrFound;

    private ItemType type;

    private String contactName;
    private String contactEmail;
    private String postedByUsername; // Tracks the login session string
    
    private String itemStatus = "OPEN";
    
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ItemType {
        LOST,
        FOUND
    }
}
