package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class ApiApplication
 *
 * Main entry point for the Spring Boot application.
 */
@SpringBootApplication
public class ApiApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args Command line arguments (not used in this application).
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}