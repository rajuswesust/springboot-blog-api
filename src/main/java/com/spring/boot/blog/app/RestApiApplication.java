package com.spring.boot.blog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.modelmapper.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RestApiApplication {


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").
						allowedOrigins("*");
			}
		};
	}

//	public static void main(String[] args) {
//		// Set up the database connection properties
//		String url = "jdbc:mysql://localhost:3306/testdb";
//		String username = "root";
//		String password = "pa$$wordX1";
//
//		// Create the database connection
//		try (Connection conn = DriverManager.getConnection(url, username, password)) {
//			// Connection successful, do something with the database
//			System.out.println("connected!");
//		} catch (SQLException e) {
//			// Connection failed, handle the exception
//			e.printStackTrace();
//		}
//	}

}
