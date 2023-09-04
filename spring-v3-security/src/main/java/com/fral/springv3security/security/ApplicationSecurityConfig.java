package com.fral.springv3security.security;

import com.fral.springv3security.auth.ApplicationUserService;
import com.fral.springv3security.jwt.JwtConfig;
import com.fral.springv3security.jwt.JwtTokenVerifier;
import com.fral.springv3security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import static com.fral.springv3security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // deprecated
// prePostEnabled = @PreAuthorize
@EnableMethodSecurity()
public class ApplicationSecurityConfig {

    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public ApplicationSecurityConfig(ApplicationUserService applicationUserService,
                                     PasswordEncoder passwordEncoder,
                                     JwtConfig jwtConfig,
                                     SecretKey secretKey) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    //region Basic & Form Based Authentication
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
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//            .authorizeHttpRequests(
//                authorize -> authorize
////                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
////                        .requestMatchers(new AntPathRequestMatcher("index")).permitAll()
////                        .requestMatchers(new AntPathRequestMatcher("/css/*")).permitAll()
////                        .requestMatchers(new AntPathRequestMatcher("/js/*")).permitAll()
//
////                        .requestMatchers("/", "index", "/css/*", "/js/*").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/*", "/js/*").permitAll()
//                        .requestMatchers("/api/**").hasRole(STUDENT.name())
//
////                        .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
////                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
////                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
////                        .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
//
//                        .anyRequest().authenticated()
//            )
//            // Until here Basic Authentication
////            .httpBasic(Customizer.withDefaults());
//
//            // From here, form-based authentication
//            .formLogin(form -> form
//                    .loginPage("/login")
//                    .permitAll()
//                    .passwordParameter("password") // This both parameter names are by default
//                    .usernameParameter("username") // If we needed to change it we can do it, so the form names should change as well.
//                    .defaultSuccessUrl("/courses", true))
//            .rememberMe(remember -> remember
//                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                    .key("somethingverysecured")
//                    .rememberMeParameter("remember-me"))
//            .logout(logout -> logout
//                    .logoutUrl("/logout")
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // If csrf enabled, then this should be removed, since this should be POST request.
//                    .clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID", "remember-me")
//                    .logoutSuccessUrl("/login"));
//
//        return http.build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        UserDetails annaSmithUser = User.builder()
//                .passwordEncoder(passwordEncoder()::encode)
//                .username("annasmith")
//                .password("password")
////                .roles(STUDENT.name()) // ROLE_STUDENT
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails mariaJonesUser = User.builder()
//                .passwordEncoder(passwordEncoder()::encode)
//                .username("mariajones")
//                .password("password")
////                .roles(ADMIN.name()) // ROLE_ADMIN
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomTraineeUser = User.builder()
//                .passwordEncoder(passwordEncoder()::encode)
//                .username("tom")
//                .password("password")
////                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                .build();
//
//        manager.createUser(annaSmithUser);
//        manager.createUser(mariaJonesUser);
//        manager.createUser(tomTraineeUser);
//
//        return manager;
//    }
    //endregion

    //region Database authentication
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        authorize -> authorize
//                            .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/*", "/js/*").permitAll()
//                            .requestMatchers("/api/**").hasRole(STUDENT.name())
//                            .anyRequest().authenticated()
//                )
////                .authenticationManager(getAuthenticationManager(http))
//                .authenticationProvider(daoAuthenticationProvider())
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                        .passwordParameter("password") // This both parameter names are by default
//                        .usernameParameter("username") // If we needed to change it we can do it, so the form names should change as well.
//                        .defaultSuccessUrl("/courses", true))
//                .rememberMe(remember -> remember
//                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                        .key("somethingverysecured")
//                        .rememberMeParameter("remember-me"))
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // If csrf enabled, then this should be removed, since this should be POST request.
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "remember-me")
//                        .logoutSuccessUrl("/login"));
//
//        return http.build();
//    }

//    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
//        // Configure AuthenticationBuilder
//        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
////        authManagerBuilder.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder());
//
//        // Get AuthenticationManager
////        return authManagerBuilder.build();
//
//        return authManagerBuilder.authenticationProvider(daoAuthenticationProvider())
//                .build();
//    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setUserDetailsService(applicationUserService);
//
//        return provider;
//    }
    //endregion


    //region JSON Web Tokens Authentication
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
//                .authenticationProvider(daoAuthenticationProvider())
                .sessionManagement(manager -> manager
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                    authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/*", "/js/*").permitAll()
                        .requestMatchers("/api/**").hasRole(STUDENT.name())
                        .anyRequest().authenticated()
                );

        return http.build();
    }

//    @Bean
//    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
//        // Configure AuthenticationBuilder
//        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authManagerBuilder.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder);
//
//        // Get AuthenticationManager
//        return authManagerBuilder.build();
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
    //endregion
}
