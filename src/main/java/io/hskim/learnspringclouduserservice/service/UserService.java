package io.hskim.learnspringclouduserservice.service;

import io.hskim.learnspringclouduserservice.client.OrderServiceClient;
import io.hskim.learnspringclouduserservice.dto.OrderDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserRequestDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserSearchDto;
import io.hskim.learnspringclouduserservice.entity.UserEntity;
import io.hskim.learnspringclouduserservice.repo.UserRepo;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  @Value("${order-service.url}")
  private String orderServiceUrl;

  // private final RestTemplate restTemplate;

  private final OrderServiceClient orderServiceClient;

  private final BCryptPasswordEncoder passwordEncoder;

  private final UserRepo userRepo;

  private final CircuitBreakerFactory circuitBreakerFactory;

  public UserResponseDto postUser(UserRequestDto userRequestDto) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper
      .getConfiguration()
      .setMatchingStrategy(MatchingStrategies.STANDARD);
    modelMapper.getConfiguration().setSkipNullEnabled(true);

    UserEntity userEntity = modelMapper.map(userRequestDto, UserEntity.class);

    // 어째서인지 ModelMapper가 동작하지 않음
    System.out.println(userEntity.toString());

    return userRepo
      .save(
        UserEntity
          .builder()
          .email(userRequestDto.getEmail())
          .userName(userRequestDto.getUserName())
          .password(passwordEncoder.encode(userRequestDto.getPassword()))
          .build()
      )
      .toDto();
  }

  public Page<UserResponseDto> getUserList(
    UserSearchDto userSearchDto,
    Pageable pageable
  ) {
    return userRepo
      .findAll(
        Example.of(
          UserEntity
            .builder()
            .email(userSearchDto.getEmail())
            .userName(userSearchDto.getUserName())
            .build(),
          ExampleMatcher
            .matchingAny()
            .withMatcher("email", matcher -> matcher.contains())
            .withMatcher("userName", matcher -> matcher.contains())
        ),
        pageable
      )
      .map(UserEntity::toDto);
  }

  public UserResponseDto getUserDto(String userId) {
    UserResponseDto findUserResponseDto = userRepo
      .findById(UUID.fromString(userId))
      .orElseThrow(() -> new NotFoundException())
      .toDto();

    // 로드 밸런서를 통한 REST API 연결
    // ResponseEntity<String> response = restTemplate.exchange(
    //   "%s/order/%s/orders".formatted(orderServiceUrl, userId),
    //   HttpMethod.GET,
    //   null,
    //   new ParameterizedTypeReference<String>() {}
    // );

    // if (response.getStatusCode().is2xxSuccessful()) {
    //   JsonMapper jsonMapper = JsonMapper.builder().build();

    //   try {
    //     Map<String, Object> firstMap = jsonMapper.readValue(
    //       response.getBody(),
    //       new TypeReference<Map<String, Object>>() {}
    //     );

    //     if (firstMap.containsKey("content")) {
    //       List<OrderDto> orderList = jsonMapper.readValue(
    //         jsonMapper.writeValueAsString(firstMap.get("content")),
    //         new TypeReference<List<OrderDto>>() {}
    //       );

    //       findUserResponseDto.setOrderList(orderList);
    //     }
    //   } catch (JsonProcessingException e) {
    //     e.printStackTrace();
    //   }
    // }

    // FeignClient를 통한 REST API 연결
    // Page<OrderDto> orderList = orderServiceClient.getOrderList(userId);

    // CircuitBreaker 처리
    org.springframework.cloud.client.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerFactory.create(
      "circuitBreaker"
    );

    Page<OrderDto> orderList = circuitBreaker.run(
      () -> orderServiceClient.getOrderList(userId),
      throwable -> null
    );

    if (orderList != null) {
      findUserResponseDto.setOrderList(orderList.getContent());
    } else {
      findUserResponseDto.setOrderList(new ArrayList<>());
    }

    return findUserResponseDto;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    UserEntity findUserEntity = userRepo
      .findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException(username));

    return new User(
      username,
      findUserEntity.getPassword(),
      true,
      true,
      true,
      true,
      new ArrayList<>()
    );
  }
}
