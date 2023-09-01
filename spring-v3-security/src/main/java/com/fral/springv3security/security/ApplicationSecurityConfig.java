package com.fral.springv3security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.fral.springv3security.security.ApplicationUserPermission.*;
import static com.fral.springv3security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // deprecated
@EnableMethodSecurity()
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorize -> authorize
//                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("index")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/css/*")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/js/*")).permitAll()

//                        .requestMatchers("/", "index", "/css/*", "/js/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/*", "/js/*").permitAll()
                        .requestMatchers("/api/**").hasRole(STUDENT.name())
                        .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                        .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails annaSmithUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("annasmith")
                .password("password")
                .roles(STUDENT.name()) // ROLE_STUDENT
                .build();

        UserDetails mariaJonesUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("mariajones")
                .password("password")
                .roles(ADMIN.name()) // ROLE_ADMIN
                .build();

        UserDetails tomTraineeUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("tom")
                .password("password")
                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
                .build();

        manager.createUser(annaSmithUser);
        manager.createUser(mariaJonesUser);
        manager.createUser(tomTraineeUser);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
