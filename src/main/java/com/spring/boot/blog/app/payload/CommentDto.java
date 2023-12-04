package com.spring.boot.blog.app.payload;

import com.spring.boot.blog.app.entity.Post;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private long id;

    @NotEmpty(message = "message should not be null or empty")
    private String name;

    @Email
    @NotEmpty(message = "email should not be null or empty")
    private String email;

    @NotEmpty
    @Size(min = 3, message = "comment body must be minimum 3 character")
    private  String body;
}
