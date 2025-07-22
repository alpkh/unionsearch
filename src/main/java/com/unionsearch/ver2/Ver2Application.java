package com.unionsearch.ver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.unionsearch.ver2.entity"})
@EnableJpaRepositories(basePackages = {"com.unionsearch.ver2.repository"})
public class Ver2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ver2Application.class, args);
	}

}
