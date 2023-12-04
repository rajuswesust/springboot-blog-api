package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.entity.Post;
import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.payload.PostResponse;
import com.spring.boot.blog.app.service.FileService;
import com.spring.boot.blog.app.service.PostService;
import com.spring.boot.blog.app.utils.AppConstants;
import com.spring.boot.blog.app.utils.FileUpload;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private PostService postService;
    private FileService fileService;
    @Value("${project.images}")
    private String path;
    @Autowired
    private FileUpload fileUpload;
    public PostController(PostService postService, FileService fileService) {
        this.postService = postService;
        this.fileService = fileService;
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

//    @PostMapping
//    public ResponseEntity<?> createPost(@RequestBody()  String username) {
//        return new ResponseEntity<>("ok", HttpStatus.CREATED);
//    }


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long postId) {
        System.out.println("get post by id: " + postId);
        System.out.println("-->" +  postService.getPost(postId));
        return ResponseEntity.ok(postService.getPost(postId));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable(name = "userId") Long userId) {
        System.out.println("get post by user id: " + userId);
        System.out.println("-->" +  postService.getPostsByUser(userId));
        return ResponseEntity.ok(postService.getPostsByUser(userId));
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

    @PostMapping("/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file")MultipartFile file) {

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());
        System.out.println(file.getName());

        try {
            if(file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Photo Found!");
            }
            boolean bal = fileUpload.upload(file);
            if(bal)
                return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(Objects.requireNonNull(file.getOriginalFilename())).toUriString());
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong!");
    }


    @PostMapping("/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable(name = "postId") Long postId) throws IOException {

        PostDto postDto = postService.getPost(postId);

        String fileName = fileService.uploadImage(path, image);
        postDto.setBannerImageName(fileName);
        PostDto updatePost = postService.updatePost(postId, postDto);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }


    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


}
