package com.chinchinne.categoryservice.domain.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.CollectionTable;
import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@Document( collection = "category" )
public class Categories
{
    @Id
    private ObjectId categoriesId;

    @Field("userId")
    private String userId;

    @Field("categories")
    private List<MCategory> categories;

    public Categories(String userId, List<MCategory> categories)
    {
        this.userId = userId;
        this.categories = categories;
    }
}
