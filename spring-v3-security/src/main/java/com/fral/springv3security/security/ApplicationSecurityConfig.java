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

import java.util.concurrent.TimeUnit;

import static com.fral.springv3security.security.ApplicationUserPermission.*;
import static com.fral.springv3security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // deprecated
// prePostEnabled = @PreAuthorize
@EnableMethodSecurity()
public class ApplicationSecurityConfig {

    /**
     * HttpBasic Authentication:
     *  - Always send Authorization header with base64 encoded password
     *  - We cannot log out
     *  - It requires HTTPs (it is recommended)
     * Form Authentication:
     *  - The first time it returns 200 status code, + sessionCookieID
     *  - The sessionID expires after 30 minutes of inactivity
     *  - The session ID can be modified with rememberMe() with defaults to 2 weeks
     * @param http
     * @return
     * @throws Exception
     */
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

//                        .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())

                        .anyRequest().authenticated()
            )
            // Until here Basic Authentication
//            .httpBasic(Customizer.withDefaults());

            // From here, form-based authentication
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
                    .passwordParameter("password") // This both parameter names are by default
                    .usernameParameter("username") // If we needed to change it we can do it, so the form names should change as well.
                    .defaultSuccessUrl("/courses", true))
            .rememberMe(remember -> remember
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingverysecured")
                    .rememberMeParameter("remember-me"))
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // If csrf enabled, then this should be removed, since this should be POST request.
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails annaSmithUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("annasmith")
                .password("password")
//                .roles(STUDENT.name()) // ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails mariaJonesUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("mariajones")
                .password("password")
//                .roles(ADMIN.name()) // ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomTraineeUser = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("tom")
                .password("password")
//                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
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
