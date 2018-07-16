package com.fral.spring.billing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fral.spring.billing.handlers.auth.LoginSuccessHandler;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//FIRST APPROACH DEFINING IN MATCHERS THE AUTHORIZATION
		//SECOND APPROACH USING ANNOTATIONS
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/h2-console/**").permitAll()
//			.antMatchers("/ver/**").hasAnyRole("USER")
//			.antMatchers("/uploads/**").hasAnyRole("USER")
//			.antMatchers("/form/**").hasAnyRole("ADMIN")
//			.antMatchers("/delete/**").hasAnyRole("ADMIN")
//			.antMatchers("/invoices/**").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
			.and()
				.formLogin()
					.successHandler(successHandler)
					.loginPage("/login")
				.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/error_403");
		
		//The following lines should not be enabled. Comment them!
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

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
