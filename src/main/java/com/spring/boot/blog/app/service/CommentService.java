package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.payload.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto);
    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentsByPostIdCommentId(Long postId, Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);

     void deleteComment(Long postId, Long commentId);
}
