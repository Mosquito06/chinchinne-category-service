package com.chinchinne.categoryservice.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.chinchinne.categoryservice.repository.jpa")
@EnableMongoRepositories(basePackages = "com.chinchinne.categoryservice.repository.mongo")
public class TransactionConfig
{
    public static final String JPA_TRANSACTION_MANAGER = "jpaTransactionManager";
    public static final String MONGO_TRANSACTION_MANAGER = "mongoTransactionManager";
    public static final String TRANSACTION_MANAGER = "transactionManager";

    @Bean
    public MongoClient mongoClient()
    {
        return MongoClients.create("mongodb://localhost:27017/chinchinne");
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient)
    {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "chinchinne");
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory)
    {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    @Bean(name = JPA_TRANSACTION_MANAGER)
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = MONGO_TRANSACTION_MANAGER)
    public PlatformTransactionManager mongoTransactionManager(MongoTransactionManager mongoTransactionManager)
    {
        return mongoTransactionManager;
    }

    @Bean(name = TRANSACTION_MANAGER)
    public JtaTransactionManager transactionManager(PlatformTransactionManager jpaTransactionManager, PlatformTransactionManager mongoTransactionManager)
    {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager((TransactionManager) jpaTransactionManager);
        transactionManager.setTransactionManager((TransactionManager) mongoTransactionManager);
        return transactionManager;
    }
}
