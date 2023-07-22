package io.hskim.learnspringclouduserservice;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final Environment env;

  @GetMapping(value = "/{serviceType}")
  public String getServiceType(@PathVariable String serviceType) {
    return "Welcome to %s!".formatted(serviceType.replace("-", " "));
  }

  @GetMapping(value = "/{serviceType}/message")
  public String getMessage(
    @PathVariable String serviceType,
    @RequestHeader(
      value = "gateway-request",
      required = false,
      defaultValue = "'gateway-request' header not-provided"
    ) String requestHeader
  ) {
    return requestHeader;
  }

  @GetMapping(value = "/{serviceType}/check")
  public String getCheck(@PathVariable String serviceType) {
    return "Check Application %s:%s".formatted(
        env.getProperty("spring.application.name"),
        env.getProperty("local.server.port")
      );
  }
}
