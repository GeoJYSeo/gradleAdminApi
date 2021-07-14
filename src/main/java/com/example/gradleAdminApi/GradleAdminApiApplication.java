package com.example.gradleAdminApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GradleAdminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradleAdminApiApplication.class, args);
	}

}
