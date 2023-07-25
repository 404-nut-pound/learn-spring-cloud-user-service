package io.hskim.learnspringclouduserservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    switch (response.status()) {
      case 400:
        break;
      case 404:
        if (methodKey.contains("getOrderList")) {
          return new ResponseStatusException(
            HttpStatusCode.valueOf(response.status()),
            "User orders not found."
          );
        }
        break;
    }

    throw new UnsupportedOperationException("Unimplemented method 'decode'");
  }
}
