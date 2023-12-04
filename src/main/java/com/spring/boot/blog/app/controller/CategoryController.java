package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.CategoryDto;
import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // get all
    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    //get posts by category id
    @GetMapping("/{categoryId}/posts")
    public List<PostDto> getAllPostsByCategoryId(@PathVariable(name = "categoryId") Long id) {
        return ResponseEntity.ok(categoryService.getPosts(id)).getBody();
    }
}
