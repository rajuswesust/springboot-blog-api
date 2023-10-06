package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.payload.PostResponse;

import java.util.List;

public interface PostService {
  PostDto createPost(PostDto postDto);

  PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPost(Long id);

    PostDto updatePost(Long postId, PostDto postDto);

    void deleteById(Long postId);
}
