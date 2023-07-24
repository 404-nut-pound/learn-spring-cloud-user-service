package io.hskim.learnspringclouduserservice.config.security;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.hskim.learnspringclouduserservice.dto.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private JsonMapper jsonMapper = JsonMapper.builder().build();

  public AuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    try {
      LoginDto loginDto = jsonMapper.readValue(
        request.getInputStream(),
        LoginDto.class
      );

      return getAuthenticationManager()
        .authenticate(
          new UsernamePasswordAuthenticationToken(
            loginDto.getEmail(),
            loginDto.getPassword(),
            new ArrayList<>()
          )
        );
    } catch (IOException e) {
      e.printStackTrace();
    }

    return super.attemptAuthentication(request, response);
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    // super.successfulAuthentication(request, response, chain, authResult);
  }
}
