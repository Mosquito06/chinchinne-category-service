package com.chinchinne.categoryservice.repository.mongo;

import org.bson.types.ObjectId;
import com.chinchinne.categoryservice.domain.document.Categories;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository( value = "categoryMongoRepository" )
public interface CategoryMongoRepository extends MongoRepository<Categories, ObjectId>
    {
        @Aggregation( pipeline =
        {
             "{ $match : { userId: ?0 } }"
            ,"{ $unwind : '$categories' }"
            ,"{ $match : {'categories.name' : { $regex : ?1}}}"
            ,"{ $skip : ?2 }"
            ,"{ $limit : ?3 }"
            ,"{ $group : { _id : '$userId', categories : { $push : '$categories' } } }"
            ,"{ $project : { _id: 0, userId : '$_id', categories : '$categories' } }"
        })
        //@Query( value = "{ 'userId': { $eq : ?0 }, 'categories' : { $slice : [0, 5] }}" )
        List<Categories> findByUserIdAndKeyword(@Param("userId") String userId, @Param("keyword") String keyword, @Param("skip") int skip, @Param("limit") int limit);

        @Aggregation(pipeline =
        {
             "{ $match : { userId: ?0 } }"
            ,"{ $unwind : '$categories' }"
            ,"{ $match : {'categories.name' : { $regex : ?1}}}"
            ,"{ $group : { _id : '$userId', categories : { $push : '$categories' } } }"
            ,"{ $project : { _id: 0, categories : { $size :  '$categories'} } }"
        })
        HashMap<String, Integer> findTotalCountByUserIdAndKeyWord(@Param("userId") String userId, @Param("keyword") String keyword);

        Categories save(Categories categories);
}
