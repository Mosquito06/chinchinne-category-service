package com.chinchinne.categoryservice.vo;

import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class RequestCategory
{
    private BigInteger id;
    private String userId;

    @NotEmpty
    private String name;

    private String color;
    private Common delYn;
}
