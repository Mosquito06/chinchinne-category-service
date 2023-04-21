package com.chinchinne.categoryservice;

import com.chinchinne.categoryservice.annotation.CategoryTest;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.lang.module.Configuration;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@CategoryTest
class CategoryServiceApplicationTests
{
    @Autowired
    DataSource dataSource;

    @Autowired
    MongoClient mongoClient;

    @Test
    void DBConnectionTest()
    {
        try
        {
            Connection con = dataSource.getConnection();
            assertNotNull(con);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    void MongoDbConnectionTest()
    {
        MongoDatabase chinchinne = mongoClient.getDatabase("chinchinne");
        assertNotNull(chinchinne);
    }

}
