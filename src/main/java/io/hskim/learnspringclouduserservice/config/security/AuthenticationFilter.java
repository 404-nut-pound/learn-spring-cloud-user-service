package io.hskim.learnspringclouduserservice.config.security;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.hskim.learnspringclouduserservice.dto.LoginDto;
import io.hskim.learnspringclouduserservice.entity.UserEntity;
import io.hskim.learnspringclouduserservice.repo.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private JsonMapper jsonMapper = JsonMapper.builder().build();

  private UserRepo userRepo;

  private Environment env;

  public AuthenticationFilter(
    AuthenticationManager authenticationManager,
    UserRepo userRepo,
    Environment env
  ) {
    super.setAuthenticationManager(authenticationManager);

    this.userRepo = userRepo;

    this.env = env;
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

    String username = ((User) (authResult.getPrincipal())).getUsername();

    UserEntity findUserEntity = userRepo
      .findByEmail(username)
      .orElseThrow(() -> new NotFoundException());

    String token = Jwts
      .builder()
      .setSubject(findUserEntity.getUserId().toString())
      .setExpiration(
        Timestamp.valueOf(
          LocalDateTime
            .now()
            .plusHours(Long.parseLong(env.getProperty("jwt.expire-hours")))
        )
      )
      .signWith(SignatureAlgorithm.HS512, env.getProperty("jwt.secret-key"))
      .compact();

    response.addHeader("token", token);
    response.addHeader("userId", findUserEntity.getUserId().toString());
  }
}
