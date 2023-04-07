package com.chinchinne.categoryservice.service;

import com.chinchinne.categoryservice.domain.entity.Account;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.CategoryId;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.exception.CustomException;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.model.ErrorCode;
import com.chinchinne.categoryservice.repository.AccountRepository;
import com.chinchinne.categoryservice.repository.CategoryRepository;
import com.chinchinne.categoryservice.spec.AccountSpecs;
import com.chinchinne.categoryservice.spec.CategorySpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService
{
    ModelMapper modelMapper;

    AccountRepository accountRepository;

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(AccountRepository accountRepository, CategoryRepository categoryRepository, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto)
    {
        Category category = new Category
        (
             new UserId(categoryDto.getUserId())
            ,categoryDto.getName()
            ,categoryDto.getColor()
            ,Common.NO
        );

        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

    @Transactional
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
            ,categoryDto.getColor()
        );

        return modelMapper.map(category, CategoryDto.class);
    }

    @Transactional
    public CategoryDto removeCategory(CategoryDto categoryDto)
    {
        List<Category> categories = categoryRepository.findAll(CategorySpecs.CategoryId(categoryDto.getId()).and(CategorySpecs.DelYn(Common.NO)))
                                                      .orElseGet(ArrayList::new);

        if( categories.isEmpty() )
        {
            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
        }

        Category category = categories.get(0);

        List<Account> accounts = accountRepository.findAll(AccountSpecs.CategoryId(new CategoryId(category.getCategoryId())).and(AccountSpecs.DelYn(Common.NO)))
                                                  .orElseGet(ArrayList::new);

        if( accounts.size() > 0 )
        {
            throw new CustomException(ErrorCode.USING_RECORD);
        }

        category.removeCategory
        (
             new UserId(categoryDto.getUserId())
            ,Common.YES
        );

        return modelMapper.map(category, CategoryDto.class);
    }
}
