package com.chinchinne.categoryservice.domain.entity;

import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor
@Getter
@Access( AccessType.FIELD )
public class Category
{
    @Id
    @Column(name = "SEQ")
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private BigInteger categoryId;

    @Embedded
    private UserId userId;

    @Column( name = "CATEGORY_NAME" )
    private String categoryName;

    @Column( name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Column( name = "MOD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @Embedded
    @AttributeOverrides
    (
        @AttributeOverride( name = "id", column = @Column( name = "MOD_ID"))
    )
    private UserId modId;

    @Column( name = "DEL_YN")
    private Common delYn;
}
