package io.hskim.learnspringclouduserservice.service;

import io.hskim.learnspringclouduserservice.dto.UserDto.UserRequestDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import io.hskim.learnspringclouduserservice.entity.UserEntity;
import io.hskim.learnspringclouduserservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
}
