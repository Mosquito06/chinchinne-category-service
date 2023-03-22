package com.chinchinne.categoryservice.model;

import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto
{
    private BigInteger categoryId;
    private String userId;
    private String categoryName;
}
