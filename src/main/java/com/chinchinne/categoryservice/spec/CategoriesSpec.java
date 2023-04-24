package com.chinchinne.categoryservice.spec;

import com.chinchinne.categoryservice.domain.document.Categories;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.persistence.criteria.Predicate;
import java.math.BigInteger;

public class CategoriesSpec
{
    public static Specification<Categories> UserId(String userId)
    {
        return (root, query, builder) -> builder.equal(root.get("userId"), userId);
    }

//    public static Specification<Category> CategoryName(String keywords)
//    {
//        return (root, query, builder) ->
//        {
//            StringBuilder sb = new StringBuilder();
//            sb.append("%");
//            sb.append(keywords);
//            sb.append("%");
//
//            Criteria criteria = Criteria.where("categories").elemMatch(Criteria.where("name").regex(keywords, "i"));
//
//            return builder.like(root.get("categories").)
//        };
//    }
}
