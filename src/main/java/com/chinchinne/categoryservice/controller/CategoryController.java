package com.chinchinne.categoryservice.controller;

import com.chinchinne.categoryservice.dao.CategoryDao;
import com.chinchinne.categoryservice.domain.document.Categories;
import com.chinchinne.categoryservice.domain.document.MCategory;
import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.exception.CustomException;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.model.ErrorCode;
import com.chinchinne.categoryservice.repository.jpa.CategoryRepository;
import com.chinchinne.categoryservice.repository.mongo.CategoryMongoRepository;
import com.chinchinne.categoryservice.service.CategoryService;
import com.chinchinne.categoryservice.spec.CategoriesSpec;
import com.chinchinne.categoryservice.spec.CategorySpecs;
import com.chinchinne.categoryservice.vo.RequestCategory;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryController
{
    static final int PER_PAGE = 5;

    CategoryDao categoryDao;

    ModelMapper modelMapper;

    CategoryService categoryService;

    CategoryRepository categoryRepository;

    CategoryMongoRepository categoryMongoRepository;

    public CategoryController(CategoryDao categoryDao, ModelMapper modelMapper, CategoryRepository categoryRepository, CategoryService categoryService, CategoryMongoRepository categoryMongoRepository)
    {
        this.categoryDao = categoryDao;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.categoryMongoRepository = categoryMongoRepository;
    }

    @GetMapping("/{userId}/all/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@PathVariable String userId)
    {
        List<Category> categories = categoryRepository.findAll(CategorySpecs.UserId(new UserId(userId)).and(CategorySpecs.DelYn(Common.NO)))
                                                      .orElseGet(ArrayList::new);

        List<CategoryDto> res = categories.stream().map(m -> modelMapper.map(m, CategoryDto.class)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{userId}/categories")
    public ResponseEntity<HashMap<String, Object>> getCategories(@PathVariable String userId, @RequestParam(required = true) int page, @RequestParam(required = false, defaultValue = "") String keywords)
    {
//        Specification<Category> spec = CategorySpecs.UserId(new UserId(userId)).and(CategorySpecs.DelYn(Common.NO));
//        PageRequest pageReq = PageRequest.of( page - 1 , PER_PAGE);
//
//        if( !StringUtils.isEmpty(keywords) )
//        {
//            spec = CategorySpecs.UserId(new UserId(userId)).and(CategorySpecs.DelYn(Common.NO)).and(CategorySpecs.CategoryName(keywords));
//        }
//
//        Page<Category> pageRes = categoryRepository.findAll(spec, pageReq);
//
//        List<CategoryDto> categories = pageRes.getContent().stream().map(m -> modelMapper.map(m, CategoryDto.class)).collect(Collectors.toList());

//        HashMap<String, Object> resHm = new HashMap<>();
//        resHm.put("categories", categories);
//        resHm.put("total", pageRes.getTotalElements());
//        resHm.put("totalPage", pageRes.getTotalPages());
//        resHm.put("perPage", PER_PAGE);
//        resHm.put("page", page);

        int skip = ( page - 1 ) * PER_PAGE;

        List<Categories> categories = categoryMongoRepository.findByUserIdAndKeyword(userId, keywords, skip, PER_PAGE);
        HashMap<String, Integer> totalRes = categoryMongoRepository.findTotalCountByUserIdAndKeyWord(userId, keywords);
        Integer totalCount = !totalRes.isEmpty() ? totalRes.get("categories") : 0;

        HashMap<String, Object> resHm = new HashMap<>();
        resHm.put("categories", categories.size() > 0 ? categories.get(0).getCategories() : new ArrayList<>());
        resHm.put("total", totalCount);
        resHm.put("totalPage", Math.ceil((double) totalCount / (double) PER_PAGE));
        resHm.put("perPage", PER_PAGE);
        resHm.put("page", page);

        return ResponseEntity.status(HttpStatus.OK).body(resHm);
    }

    @GetMapping("/{userId}/category/{categoryId}")
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

    @PostMapping("/{userId}/category")
    public ResponseEntity<CategoryDto> addCategory(@PathVariable String userId, @RequestBody @Valid RequestCategory requestCategory)
    {
        requestCategory.setUserId(userId);

        CategoryDto categoryDto = modelMapper.map(requestCategory, CategoryDto.class);

        CategoryDto res = categoryService.createCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{userId}/category")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String userId, @RequestBody @Valid RequestCategory requestCategory)
    {
        requestCategory.setUserId(userId);

        CategoryDto categoryDto = modelMapper.map(requestCategory, CategoryDto.class);

        CategoryDto res = categoryService.changeCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{userId}/categories")
    public ResponseEntity<List<CategoryDto>> deleteCategory(@PathVariable String userId, @RequestBody List<RequestCategory> requestCategories)
    {
        List<CategoryDto> req = requestCategories.stream().map( reqCategory ->
        {
            reqCategory.setUserId(userId);
            return modelMapper.map(reqCategory, CategoryDto.class);
        }).collect( Collectors.toList() );

        List<CategoryDto> res = categoryService.removeCategory(req);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}