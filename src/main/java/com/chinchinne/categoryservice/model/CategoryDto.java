package com.chinchinne.categoryservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto
{
    private BigInteger id;
    private String userId;
    private String name;
    private String backColor;
    private String textColor;
    private Long regDate;
    private Long modDate;
}
