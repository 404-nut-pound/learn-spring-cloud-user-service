package io.hskim.learnspringclouduserservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping(value = "/{serviceType}")
  public String getFirstService(@PathVariable String serviceType) {
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
}
