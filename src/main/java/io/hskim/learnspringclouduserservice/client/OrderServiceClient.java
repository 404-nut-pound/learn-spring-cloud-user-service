package io.hskim.learnspringclouduserservice.client;

import io.hskim.learnspringclouduserservice.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "order-service-1")
public interface OrderServiceClient {
  @GetMapping(value = "/order/{userId}/orders-ng")
  Page<OrderDto> getOrderList(@PathVariable(value = "userId") String userId);
}
