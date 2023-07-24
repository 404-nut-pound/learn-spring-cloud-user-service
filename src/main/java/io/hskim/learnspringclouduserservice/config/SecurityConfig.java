package io.hskim.learnspringclouduserservice.config;

import io.hskim.learnspringclouduserservice.config.security.AuthenticationFilter;
import io.hskim.learnspringclouduserservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepo userRepo;

  private AuthenticationManager authenticationManager;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    authenticationManager =
      authenticationConfiguration.getAuthenticationManager();

    return authenticationManager;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {
    return httpSecurity
      .authorizeHttpRequests(request ->
        request
          .requestMatchers(PathRequest.toH2Console())
          .permitAll()
          .anyRequest()
          .permitAll()
          .and()
          .addFilter(new AuthenticationFilter(authenticationManager, userRepo))
      )
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.frameOptions().disable())
      .build();
  }
}
