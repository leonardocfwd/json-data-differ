package com.waesst.jsondiffer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring Boot. 
 * 
 * I have decided to use Spring Boot in order to facilitate proof of concept and to have dinamyc and easy-to-use HTTP endpoints.
 * 
 * @author Leonardo Nelson
 *
 */
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
