package com.chinchinne.categoryservice.spec;

import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigInteger;

public class CategorySpecs
{
    public static Specification<Category> CategoryId(BigInteger categoryId)
    {;
        return (root, query, builder) -> builder.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Category> UserId(UserId userId)
    {;
        return (root, query, builder) -> builder.equal(root.get("userId").get("id"), userId.getId());
    }

    public static Specification<Category> DelYn(Common delYn)
    {
        return (root, query, builder) -> builder.equal(root.get("delYn"), delYn);
    }
}
