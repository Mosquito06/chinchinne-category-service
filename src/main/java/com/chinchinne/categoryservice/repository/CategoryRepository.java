package com.chinchinne.categoryservice.repository;

import com.chinchinne.categoryservice.domain.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends Repository<Category, BigInteger>
{
    Optional<List<Category>> findAll(Specification<Category> spec);
}
