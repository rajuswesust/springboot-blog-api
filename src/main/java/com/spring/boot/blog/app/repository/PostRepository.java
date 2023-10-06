package com.spring.boot.blog.app.repository;

import com.spring.boot.blog.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
}
