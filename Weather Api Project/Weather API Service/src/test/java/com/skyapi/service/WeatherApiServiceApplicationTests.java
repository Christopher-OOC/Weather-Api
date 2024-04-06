package com.skyapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import lombok.Data;

@ComponentScan(value="com.skyapi.service.controller")
@ComponentScan(value="com.skyapi.service.service")

@SpringBootTest

class WeatherApiServiceApplicationTests {
	

	@Test
	void contextLoads() {
	}

}
