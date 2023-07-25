package io.hskim.learnspringclouduserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RefreshScope
@EnableFeignClients
public class LearnSpringCloudUserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnSpringCloudUserServiceApplication.class, args);
  }
  // @Bean
  // @LoadBalanced
  // public RestTemplate restTemplate() {
  //   return new RestTemplate();
  // }
}
