package com.chinchinne.categoryservice.repository;


import com.chinchinne.categoryservice.domain.entity.Account;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import com.chinchinne.categoryservice.domain.value.UserId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/* 서비스 기반 아키텍처로 서비스 간 호출을 고려하지 않았기 때문에 중복 Repository 생성 */
public interface AccountRepository extends Repository<Account, BigInteger>
{
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.userId = :userId AND a.categoryId = :categoryId AND a.delYn = :delYn")
    boolean existByCategoryId(@Param("userId") UserId userId, @Param("categoryId") CategoryId categoryId, @Param("delYn") Common delYn);
}
