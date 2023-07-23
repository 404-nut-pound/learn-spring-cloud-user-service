package io.hskim.learnspringclouduserservice.service;

import io.hskim.learnspringclouduserservice.dto.UserDto.UserRequestDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserSearchDto;
import io.hskim.learnspringclouduserservice.entity.UserEntity;
import io.hskim.learnspringclouduserservice.repo.UserRepo;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final BCryptPasswordEncoder passwordEncoder;

  private final UserRepo userRepo;

  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
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

  public UserEntity getUserEntity(String userId) {
    return userRepo
      .findById(UUID.fromString(userId))
      .orElseThrow(() -> new NotFoundException());
  }
}
