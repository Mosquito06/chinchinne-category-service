package com.chinchinne.categoryservice.domain.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

import static java.sql.Timestamp.valueOf;

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

    @Field( name = "regDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Field( name = "modDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    public MCategory(String name, MColor color, Date regDate)
    {
        this.name = name;
        this.color = color;
        this.regDate = regDate;
    }

    // valueOf(LocalDateTime.now())
    //public void
}
