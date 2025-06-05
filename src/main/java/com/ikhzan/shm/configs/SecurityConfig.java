package com.ikhzan.shm.configs;

import com.ikhzan.shm.services.UserDetailsServiceImpl;
import com.ikhzan.shm.utils.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/api/sensors/**",
            "/api/auth/register",
            "/api/auth/login",
            "/css/**",
            "/js/**",
            "/favicon.ico",
            "/api/predict",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/sensors/obtain-token").permitAll()
                        .requestMatchers("/api/sensors/**").authenticated()
                        .requestMatchers(  "/swagger-ui/**","/v3/api-docs/**").authenticated()
                        .anyRequest().authenticated()
                )
//                .formLogin(form -> form
//                        .loginPage("/login") // Specify the login page URL
//                        .permitAll() // Allow everyone to access the login page
//                        .defaultSuccessUrl("/", true) // Redirect to home page after successful login
//                        .failureUrl("/login?error=true") // Redirect to login page with error parameter on failure
//                        .failureHandler((request, response, authenticationException) -> {
//                            // Custom failure handling
//                            response.sendRedirect("/login?error=true&message=" + authenticationException.getMessage());
//                        })
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

}
