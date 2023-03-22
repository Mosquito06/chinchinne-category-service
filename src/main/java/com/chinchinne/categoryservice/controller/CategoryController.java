package com.chinchinne.categoryservice.controller;

import com.chinchinne.categoryservice.dao.CategoryDao;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.spec.CategorySpecs;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoryController
{
    CategoryDao categoryDao;

    ModelMapper modelMapper;

    public CategoryController(CategoryDao categoryDao, ModelMapper modelMapper )
    {
        this.categoryDao = categoryDao;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{userId}/categories")
    public ResponseEntity<List<CategoryDto>> getMemo(@PathVariable String userId)
    {
        Optional<List<Category>> memo = categoryDao.findAll(CategorySpecs.UserId(new UserId(userId)).and(CategorySpecs.DelYn(Common.NO)));

        List<CategoryDto> res = memo.orElseGet(ArrayList::new).stream().map(m -> modelMapper.map(m, CategoryDto.class)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
