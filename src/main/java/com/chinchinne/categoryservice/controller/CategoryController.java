package com.chinchinne.categoryservice.controller;

import com.chinchinne.categoryservice.dao.CategoryDao;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.exception.CustomException;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.model.ErrorCode;
import com.chinchinne.categoryservice.repository.CategoryRepository;
import com.chinchinne.categoryservice.service.CategoryService;
import com.chinchinne.categoryservice.spec.CategorySpecs;
import com.chinchinne.categoryservice.vo.RequestCategory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoryController
{
    CategoryDao categoryDao;

    ModelMapper modelMapper;

    CategoryService categoryService;

    CategoryRepository categoryRepository;

    public CategoryController(CategoryDao categoryDao, ModelMapper modelMapper, CategoryRepository categoryRepository, CategoryService categoryService)
    {
        this.categoryDao = categoryDao;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{userId}/categories")
    public ResponseEntity<List<CategoryDto>> getMemo(@PathVariable String userId)
    {
        Optional<List<Category>> memo = categoryRepository.findAll(CategorySpecs.UserId(new UserId(userId)).and(CategorySpecs.DelYn(Common.NO)));

        List<CategoryDto> res = memo.orElseGet(ArrayList::new).stream().map(m -> modelMapper.map(m, CategoryDto.class)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("{userId}/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String userId, @PathVariable BigInteger categoryId)
    {
        List<Category> categories = categoryRepository.findAll(CategorySpecs.CategoryId(categoryId).and(CategorySpecs.DelYn(Common.NO)))
                                                      .orElseGet(ArrayList::new);

        if( categories.isEmpty() )
        {
            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
        }

        CategoryDto category = modelMapper.map(categories.get(0), CategoryDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping("{userId}/category")
    public ResponseEntity<CategoryDto> addCategory(@PathVariable String userId, @RequestBody @Valid RequestCategory requestCategory)
    {
        requestCategory.setUserId(userId);

        CategoryDto categoryDto = modelMapper.map(requestCategory, CategoryDto.class);

        CategoryDto res = categoryService.createCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("{userId}/category")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String userId, @RequestBody @Valid RequestCategory requestCategory)
    {
        requestCategory.setUserId(userId);

        CategoryDto categoryDto = modelMapper.map(requestCategory, CategoryDto.class);

        CategoryDto res = categoryService.changeCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
