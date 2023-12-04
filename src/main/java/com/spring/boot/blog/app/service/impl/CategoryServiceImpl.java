package com.spring.boot.blog.app.service.impl;

import com.spring.boot.blog.app.entity.Category;
import com.spring.boot.blog.app.entity.Post;
import com.spring.boot.blog.app.payload.CategoryDto;
import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.repository.CategoryRepository;
import com.spring.boot.blog.app.repository.PostRepository;
import com.spring.boot.blog.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;


    @Override
    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();

        // Map Category entities to CategoryDto objects
        return categories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPosts(Long id) {
        List<Post> posts = postRepository.findByCategory_Id(id);
        // Map Post entities to PostDto objects
        return posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Helper method to map Post entity to PostDto
    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setCategoryName(post.getCategory().getName());
        postDto.setCategoryId(post.getCategory().getId());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());
        return postDto;
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getId());
        categoryDto.setCategoryTitle(category.getName());
        categoryDto.setCategoryDescription(category.getDescription());
        return categoryDto;
    }
}
