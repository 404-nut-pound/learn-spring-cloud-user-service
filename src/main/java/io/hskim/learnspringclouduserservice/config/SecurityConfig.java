package io.hskim.learnspringclouduserservice.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity httpSecurity,
    HandlerMappingIntrospector handlerMappingIntrospector
  ) throws Exception {
    return httpSecurity
      .authorizeHttpRequests(request ->
        request
          .requestMatchers(PathRequest.toH2Console())
          .permitAll()
          .anyRequest()
          .permitAll()
      )
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.frameOptions().disable())
      .build();
  }
}
