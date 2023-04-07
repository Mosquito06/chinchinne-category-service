package com.chinchinne.categoryservice.domain.entity;


import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import com.chinchinne.categoryservice.domain.value.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@Getter
@Access( AccessType.FIELD )
/* 중복 Account Domain으로 최소한의 정보로만 조회 */
public class Account
{
    @Id
    @Column(name = "SEQ")
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private BigInteger accountId;

    @Embedded
    private UserId userId;

    @Embedded
    private CategoryId categoryId;

    @Column( name = "DEL_YN")
    private Common delYn;
}