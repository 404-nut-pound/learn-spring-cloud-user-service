package io.hskim.learnspringclouduserservice.controller;

import io.hskim.learnspringclouduserservice.dto.UserDto.UserRequestDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserSearchDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.ValidatedPostGroup;
import io.hskim.learnspringclouduserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public UserResponseDto postUser(
    @Validated(
      value = { ValidatedPostGroup.class }
    ) @RequestBody UserRequestDto userRequestDto
  ) {
    return userService.postUser(userRequestDto);
  }

  @GetMapping
  public Page<UserResponseDto> getUserList(
    UserSearchDto userSearchDto,
    Pageable pageable
  ) {
    return userService.getUserList(userSearchDto, pageable);
  }

  @GetMapping(value = "/{userId}")
  public UserResponseDto getUser(@PathVariable String userId) {
    return userService.getUserEntity(userId).toDto();
  }
}
