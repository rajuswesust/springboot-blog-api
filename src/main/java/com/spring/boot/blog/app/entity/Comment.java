package com.spring.boot.blog.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private  String body;

    @ManyToOne(fetch = FetchType.LAZY) //default would be Eager
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
