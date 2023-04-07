package com.chinchinne.categoryservice.spec;


import com.chinchinne.categoryservice.domain.entity.Account;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigInteger;

public class AccountSpecs
{
    public static Specification<Account> CategoryId(CategoryId categoryId)
    {
        return (root, query, builder) -> builder.equal(root.get("categoryId").get("id"), categoryId.getId());
    }

    public static Specification<Account> DelYn(Common delYn)
    {
        return (root, query, builder) -> builder.equal(root.get("delYn"), delYn);
    }
}

