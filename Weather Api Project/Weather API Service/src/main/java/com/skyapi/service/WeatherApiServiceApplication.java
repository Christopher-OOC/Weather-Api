package com.skyapi.service;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(value="com.skyapi.service.repository")
@EntityScan(value="com.skyapi.common")
@ComponentScan(value="com.skyapi.service.controller")
@ComponentScan(value="com.skyapi.service.service")

@SpringBootApplication
public class WeatherApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
