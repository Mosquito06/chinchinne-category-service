package com.chinchinne.categoryservice.converter;

import com.chinchinne.categoryservice.domain.model.Common;

import javax.persistence.Converter;

@Converter( autoApply = true )
public class CommonConverter extends CodeValueConverter<Common>
{
    public CommonConverter()
    {
        super(Common.class);
    }
}
