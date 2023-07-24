package io.hskim.learnspringclouduserservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonController {

  private final Environment env;

  @GetMapping
  public String getWelcome() {
    return "Welcome to User Service!";
  }

  @GetMapping(value = "/message")
  public String getMessage(
    @RequestHeader(
      value = "gateway-request",
      required = false,
      defaultValue = "'gateway-request' header not-provided"
    ) String gatewayRequestHeader
  ) {
    return gatewayRequestHeader;
  }

  @GetMapping(value = "/check")
  public String getCheck() {
    return "Check Application %s:%s\ntoken expire hours: %s\ntoken secret-key: %s".formatted(
        env.getProperty("spring.application.name"),
        env.getProperty("local.server.port"),
        env.getProperty("jwt.expire-hours"),
        env.getProperty("jwt.secret-key")
      );
  }
}
