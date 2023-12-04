package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.entity.Category;
import com.spring.boot.blog.app.payload.CategoryDto;
import com.spring.boot.blog.app.payload.PostDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll();
    List<PostDto> getPosts(Long id);
}
