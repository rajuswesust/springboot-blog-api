package com.spring.boot.blog.app.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
    String message;
    int code;
}
