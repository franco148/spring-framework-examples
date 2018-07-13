package com.fral.spring.billing.util.config;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// FIRST VERSION: Uploads to external directory
//		super.addResourceHandlers(registry);
//		registry.addResourceHandler("/uploads/**")
//		.addResourceLocations("file:/D:/CODE/TMP/SpringFrameworkCourse/uploads/");
		
		// SECOND VERSION: Absolute and external directory - uploads folder in the root of the project.
		super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);

	}

}
