package com.fral.spring.billing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{
		//1. FIRST APPROACH. IN MEMORY AUTHENTICATION
		//UserBuilder users = User.withDefaultPasswordEncoder(); //Deprecated
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		build.inMemoryAuthentication()
			 .withUser(users.username("admin").password("password").roles("ADMIN", "USER"))
			 .withUser(users.username("franco").password("password").roles("USER"));
	}
}
