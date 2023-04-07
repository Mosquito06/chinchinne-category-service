package com.chinchinne.categoryservice.repository;


import com.chinchinne.categoryservice.domain.entity.Account;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/* 서비스 기반 아키텍처로 서비스 간 호출을 고려하지 않았기 때문에 중복 Repository 생성 */
public interface AccountRepository extends Repository<Account, BigInteger>
{
    Optional<List<Account>> findAll(Specification<Account> spec);
}
