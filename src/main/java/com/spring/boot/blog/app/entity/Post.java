package com.spring.boot.blog.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="content", nullable = false, length = 1000)
    private String content;

    //bidirectional
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    // Many-to-one relationship: Many posts can belong to one user
    //uni
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // Name of the foreign key column in the 'posts' table
    private User user; // Reference to the User entity

    //uni
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
