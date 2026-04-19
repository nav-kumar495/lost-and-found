package com.example.lostandfound.repository;

import com.example.lostandfound.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByTypeOrderByCreatedAtDesc(Item.ItemType type);
    List<Item> findAllByOrderByCreatedAtDesc();
    List<Item> findByPostedByUsernameOrderByCreatedAtDesc(String username);
    
    @Query("{ '$or': [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    List<Item> searchByKeyword(String query);

    @Query("{ 'type': ?0, '$or': [ { 'title': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    List<Item> searchByTypeAndKeyword(Item.ItemType type, String query);
}
