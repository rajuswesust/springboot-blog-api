package com.spring.boot.blog.app.repository;

import com.spring.boot.blog.app.entity.Post;
import com.spring.boot.blog.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByCategory_Id(Long categoryId);

}
