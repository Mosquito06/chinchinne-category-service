package com.chinchinne.categoryservice.domain.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
public class MCategory
{
    @Id
    private ObjectId categoryId;

    @Field("name")
    private String name;

    @Field("color")
    private MColor color;

    public MCategory(String name, MColor color)
    {
        this.name = name;
        this.color = color;
    }
}
