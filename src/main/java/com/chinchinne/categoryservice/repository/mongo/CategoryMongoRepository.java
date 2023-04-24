package com.chinchinne.categoryservice.repository.mongo;

import com.chinchinne.categoryservice.domain.document.Categories;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryMongoRepository extends MongoRepository<Categories, ObjectId>
    {
        Optional<Categories> findByUserId(String userId);

//        Page<Categories> findAll(Specification<Categories> spec, Pageable pageable);
//        Page<Categories> findAll(Pageable pageable);

        Categories save(Categories categories);
}
