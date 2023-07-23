package io.hskim.learnspringclouduserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearnSpringCloudUserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnSpringCloudUserServiceApplication.class, args);
  }
}
