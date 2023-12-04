package com.spring.boot.blog.app.service.impl;

import com.spring.boot.blog.app.entity.Category;
import com.spring.boot.blog.app.entity.Post;
import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.exception.ResourceNotFoundException;
import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.payload.PostResponse;
import com.spring.boot.blog.app.repository.CategoryRepository;
import com.spring.boot.blog.app.repository.PostRepository;
import com.spring.boot.blog.app.repository.UserRepository;
import com.spring.boot.blog.app.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Retrieve authenticated user's username

        // Your logic here using the current authenticated username...
        System.out.println("Authenticated user: " + currentUsername);

        User user = userRepository.findByUsername(currentUsername).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists"));

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND, "category does not exists"));

        System.out.println("content length: "+ postDto.getContent().length());
        System.out.println("#"+postDto);
        Post post = mapToEntity(postDto);
        post.setUser(user);
        post.setCategory(category);
        System.out.println("->"+post);

        Post newPost = postRepository.save(post);
        System.out.println(newPost);

        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    //convert entity to dto
    private PostDto mapToDto(Post newPost) {
//        PostDto postDto = new PostDto();
//
//        postDto.setId(newPost.getId());
//        postDto.setTitle(newPost.getTitle());
//        postDto.setDescription(newPost.getDescription());
//        postDto.setContent(newPost.getContent());
//
//        return postDto;
        PostDto postDto = modelMapper.map(newPost, PostDto.class);
        return postDto;
    }

    //convert DTO to entity
    private Post mapToEntity(PostDto postDto) {
//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        return post;

        Post post = modelMapper.map(postDto, Post.class);
        return post;

    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> postList = posts.getContent();

        //List<PostDto> postDtoList =  postList.stream().map(p -> mapToDto(p)).collect(Collectors.toList());
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post p : postList) {
            PostDto postDto = mapToDto(p);
            postDtoList.add(postDto);
        }

        PostResponse postResponse = new PostResponse();
        postResponse.setContents(postDtoList);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPost(Long id) {
       Post post =  postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
       System.out.println(post);

       return mapToDto(post);
    }

    public List<PostDto> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));
        List<Post> postsByUser = postRepository.findByUser(user);

        List<PostDto> responsePosts = postsByUser.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .toList();
        return responsePosts;
    }

    @Override
    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deleteById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        postRepository.deleteById(postId);
    }
}
