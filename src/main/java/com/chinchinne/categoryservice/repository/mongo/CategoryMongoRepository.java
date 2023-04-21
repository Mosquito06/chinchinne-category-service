package com.chinchinne.categoryservice.repository.mongo;

import com.chinchinne.categoryservice.domain.document.Categories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryMongoRepository extends MongoRepository<Categories, ObjectId>
{
    Optional<Categories> findByUserId(String userId);

    Categories save(Categories categories);
}
