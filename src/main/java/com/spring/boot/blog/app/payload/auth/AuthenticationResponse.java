package com.spring.boot.blog.app.payload.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
   private int statusCode;
   private String message;

   @JsonProperty("refresh_token")
   private String refreshToken;

   @JsonProperty("access_token")
   private String accessToken;
}
