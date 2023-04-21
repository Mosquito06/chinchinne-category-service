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
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService
{
    ModelMapper modelMapper;

    AccountRepository accountRepository;

    CategoryRepository categoryRepository;

    CategoryMongoRepository categoryMongoRepository;

    @Autowired
    public CategoryService(AccountRepository accountRepository, CategoryRepository categoryRepository, CategoryMongoRepository categoryMongoRepository, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
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

        Categories categories = categoryMongoRepository.findByUserId(categoryDto.getUserId()).orElseGet(Categories::new);

        MColor color = new MColor(categoryDto.getBackColor(), categoryDto.getTextColor());
        MCategory mCategory = new MCategory(categoryDto.getName(), color);

        // 수정 혹은 저장 분기 처리 필요
        if( StringUtils.isEmpty(categories.getUserId()) )
        {
            categories = categories = new Categories(categoryDto.getUserId(), Arrays.asList(mCategory));
        }
        else
        {
            categories.getCategories().add(mCategory);
        }

        categoryMongoRepository.save(categories);

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

        return categories.stream().map( m -> modelMapper.map(m, CategoryDto.class) ).collect(Collectors.toList());
    }
}
