package com.fral.springv3security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringV3SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringV3SecurityApplication.class, args);
	}

}
