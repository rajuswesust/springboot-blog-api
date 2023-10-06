package com.spring.boot.blog.app.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

//@NotEmpty @Size @NotNull etc are for validation
//have to add dependency spring-boot-starter-validation
//PostDto is used as request object in createPost and updatePost rest apis
//@Valid annotation have to be used in these rest controllers to enable the validation in addition to @RequestBody
@Data
public class PostDto {
    private Long id;

    //should not be null or empty
    //at least 2 character
    @NotEmpty
    @Size(min = 2, message = "post title should be at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 5, message = "post title should be at least 5 characters")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentDto> comments;
}
