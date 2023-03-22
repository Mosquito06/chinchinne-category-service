package com.chinchinne.categoryservice.dao;

import com.chinchinne.categoryservice.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

;

// mapper, respository 겸용으로 사용
@Mapper
public interface CategoryDao extends Repository<Category, BigInteger>
{
    Optional<List<Category>> findAll(Specification<Category> spec);
}