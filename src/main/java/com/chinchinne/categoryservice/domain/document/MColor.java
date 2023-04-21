package com.chinchinne.categoryservice.domain.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
public class MColor
{
    @Id
    private ObjectId colorId;

    @Field("background")
    private String background;

    @Field("text")
    private String text;

    public MColor(String background, String text)
    {
        this.background = background;
        this.text = text;
    }
}
