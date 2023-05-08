package com.chinchinne.categoryservice.service;

import com.chinchinne.categoryservice.config.TransactionConfig;
import com.chinchinne.categoryservice.domain.document.Categories;
import com.chinchinne.categoryservice.domain.document.MCategory;
import com.chinchinne.categoryservice.domain.document.MColor;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.exception.CustomException;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.model.ErrorCode;
import com.chinchinne.categoryservice.repository.jpa.AccountRepository;
import com.chinchinne.categoryservice.repository.jpa.CategoryRepository;
import com.chinchinne.categoryservice.repository.mongo.CategoryMongoRepository;
import com.chinchinne.categoryservice.spec.CategorySpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.sql.Timestamp.valueOf;

@Service
public class CategoryService
{
    ModelMapper modelMapper;

    MongoTemplate mongoTemplate;

    AccountRepository accountRepository;

    CategoryRepository categoryRepository;

    CategoryMongoRepository categoryMongoRepository;

    @Autowired
    public CategoryService(AccountRepository accountRepository, CategoryRepository categoryRepository, CategoryMongoRepository categoryMongoRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMongoRepository = categoryMongoRepository;
    }

    @Transactional( value = TransactionConfig.TRANSACTION_MANAGER )
    public CategoryDto createCategory(CategoryDto categoryDto)
    {
        Category category = new Category
        (
             new UserId(categoryDto.getUserId())
            ,categoryDto.getName()
            ,categoryDto.getBackColor()
            ,categoryDto.getTextColor()
            ,Common.NO
        );

        categoryRepository.save(category);

        // MongoDB Upsert
        MColor color = new MColor(categoryDto.getBackColor(), categoryDto.getTextColor());
        MCategory mCategory = new MCategory(category.getCategoryId(), categoryDto.getName(), color, valueOf(LocalDateTime.now()));

        Query query = new Query(Criteria.where("userId").is(categoryDto.getUserId()));
        Update update = new Update();

        update.setOnInsert("userId", categoryDto.getUserId());
        update.addToSet("categories", mCategory);

        mongoTemplate.upsert(query, update, Categories.class);

        return modelMapper.map(category, CategoryDto.class);
    }

    @Transactional( value = TransactionConfig.TRANSACTION_MANAGER )
    public CategoryDto changeCategory(CategoryDto categoryDto)
    {
        List<Category> categories = categoryRepository.findAll(CategorySpecs.CategoryId(categoryDto.getId()).and(CategorySpecs.DelYn(Common.NO)))
                                                      .orElseGet(ArrayList::new);

        if( categories.isEmpty() )
        {
            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
        }

        Category category = categories.get(0);
        category.changeCategory
        (
             new UserId(categoryDto.getUserId())
            ,categoryDto.getName()
            ,categoryDto.getBackColor()
            ,categoryDto.getTextColor()
        );

        // MongoDB Upsert
        Query query = new Query
        (
            Criteria.where("userId").is(categoryDto.getUserId()).and("categories").elemMatch(Criteria.where("id").is(category.getCategoryId()))
        );
        query.fields().include("categories.$");

        Update update = new Update();

        update.set("categories.$.name", category.getCategoryName());
        update.set("categories.$.color", new MColor(category.getBackColor(), category.getTextColor()));
        update.set("categories.$.modDate", category.getModDate());

        mongoTemplate.updateFirst(query, update, Categories.class);

        return modelMapper.map(category, CategoryDto.class);
    }

    @Transactional( value = TransactionConfig.TRANSACTION_MANAGER )
    public List<CategoryDto> removeCategory(List<CategoryDto> categoryDtos)
    {
        List<BigInteger> bigIntegerIds = categoryDtos.stream().map( dto -> dto.getId()).collect(Collectors.toList());
        List<Category> categories = categoryRepository.findByCategoryIdInAndDelYn(bigIntegerIds, Common.NO).orElseGet(ArrayList::new);

        if( categories.isEmpty() )
        {
            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
        }

        List<CategoryId> categoryIds = categories.stream().map( category -> new CategoryId(category.getCategoryId()) ).collect(Collectors.toList());

        boolean exist = accountRepository.existByCategoryIdIn(categories.get(0).getUserId(), categoryIds, Common.NO);

        if( exist )
        {
            throw new CustomException(ErrorCode.USING_RECORD);
        }

        categoryRepository.removeByCategoryIdIn
        (
             new UserId(categoryDtos.get(0).getUserId())
            ,bigIntegerIds
            ,Common.YES
        );

        List<BigInteger> MCategoryIds =  categoryIds.stream().map( category ->category.getId() ).collect(Collectors.toList());

        // MongoDB Upsert
        Aggregation aggregation = Aggregation.newAggregation
        (
             Aggregation.match( Criteria.where("userId").is(categories.get(0).getUserId().getId()) )
            ,Aggregation.unwind("categories")
            ,Aggregation.match( Criteria.where("categories.id").nin(MCategoryIds) )  // is(categories.get(0).getUserId())
            ,Aggregation.group("_id").first("userId").as("userId").push("categories").as("categories")
        );

        Categories mCategories = mongoTemplate.aggregate(aggregation, "category", Categories.class).getMappedResults().get(0);

        Query query = new Query(Criteria.where("userId").is(mCategories.getUserId()));
        Update update = new Update();

        update.setOnInsert("userId", mCategories.getUserId());
        update.set("categories", mCategories.getCategories());

        mongoTemplate.upsert(query, update, Categories.class);

        return categories.stream().map( m -> modelMapper.map(m, CategoryDto.class) ).collect(Collectors.toList());
    }
}
