package io.hskim.learnspringclouduserservice.controller;

import io.hskim.learnspringclouduserservice.dto.UserDto.UserRequestDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import io.hskim.learnspringclouduserservice.dto.UserDto.ValidatedPostGroup;
import io.hskim.learnspringclouduserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/{serviceType}/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public UserResponseDto postUser(
    @PathVariable String serviceType,
    @Validated(
      value = { ValidatedPostGroup.class }
    ) @RequestBody UserRequestDto userRequestDto
  ) {
    return userService.postUser(userRequestDto);
  }
}
