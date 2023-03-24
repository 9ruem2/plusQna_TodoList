package com.QnaApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.QnaApi"})
public class PlusQnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusQnaApplication.class, args);
	}
}
