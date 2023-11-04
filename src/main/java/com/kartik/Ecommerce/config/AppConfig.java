package com.kartik.Ecommerce.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
       return http
               .authorizeHttpRequests((authorize) -> authorize
                       .requestMatchers("/api/**")
                       .authenticated()
                       .anyRequest()
                       .permitAll()
               )
               .addFilterBefore((Filter) new JwtValidator(), BasicAuthenticationFilter.class)
               .csrf(AbstractHttpConfigurer::disable)
               .cors(httpSecurityCorsConfigurer -> {
                   CorsConfiguration corsConfiguration = new CorsConfiguration();
                   corsConfiguration.setAllowedOrigins(Arrays.asList(
                           "http://localhost:3000",
                           "http://localhost:4200"
                   ));
                   corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                   corsConfiguration.setAllowCredentials(true);
                   corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                   corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                   corsConfiguration.setMaxAge(3600L);
               })
               .httpBasic(httpSecurityHttpBasicConfigurer -> {})
               .formLogin(httpSecurityFormLoginConfigurer -> {})
               .build();
   }

   @Bean
   public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }
}
