package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.payload.PostResponse;
import com.spring.boot.blog.app.service.PostService;
import com.spring.boot.blog.app.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        System.out.println("get posts : " + pageNo + ", " + pageSize + ", " + sortBy + ", " + sortDir);
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        System.out.println("create new entry : " + postDto);
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long postId) {
        System.out.println("get post by id: " + postId);
        System.out.println("-->" +  postService.getPost(postId));
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long postId, @Valid @RequestBody  PostDto postDto) {
        PostDto postResponse = postService.updatePost(postId, postDto);

        System.out.println("update post by id: " + postId);
        System.out.println("-->" +  postResponse);

        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(name="id") Long postId) {
        postService.deleteById(postId);
        return new ResponseEntity<>("Post is deleted successfully", HttpStatus.OK);
    }

}
