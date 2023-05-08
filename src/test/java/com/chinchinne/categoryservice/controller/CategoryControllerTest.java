package com.chinchinne.categoryservice.controller;

import com.chinchinne.categoryservice.annotation.CategoryTest;
import com.chinchinne.categoryservice.dao.CategoryDao;
import com.chinchinne.categoryservice.repository.jpa.CategoryRepository;
import com.chinchinne.categoryservice.repository.mongo.CategoryMongoRepository;
import com.chinchinne.categoryservice.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CategoryTest
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest
{
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryDao categoryDao;

    @MockBean
    CategoryService categoryService;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    CategoryMongoRepository categoryMongoRepository;

    // Static 지정했으나, User-service 생성 후 변경 필요
    final String userId = "967d6988-a1f0-11ed-a8fc-0242ac120002";

    @Test
    void getAllCategoriesTest() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/" + userId +"/all/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
