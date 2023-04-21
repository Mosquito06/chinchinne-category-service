package com.chinchinne.categoryservice.repository.jpa;

import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import com.chinchinne.categoryservice.domain.value.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends Repository<Category, BigInteger>
{
    Optional<List<Category>> findAll(Specification<Category> spec);
    Optional<List<Category>> findByCategoryIdInAndDelYn(List<BigInteger> categoryIds, Common delYn);
    Page<Category> findAll(Specification<Category> spec, Pageable pageable);
    void save(Category category);
    @Modifying( clearAutomatically = true )
    @Query("UPDATE Category c SET c.delYn = :delYn, c.modId = :userId WHERE c.categoryId IN :categoryIds")
    void removeByCategoryIdIn(@Param("userId") UserId userId, @Param("categoryIds") List<BigInteger> categoryIds, @Param("delYn") Common delYn);
}
