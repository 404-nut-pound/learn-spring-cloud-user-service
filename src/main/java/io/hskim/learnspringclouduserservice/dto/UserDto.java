package io.hskim.learnspringclouduserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class UserDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  @EqualsAndHashCode
  public static class UserSearchDto {

    @Builder.Default
    private String userName = "";

    @Builder.Default
    private String email = "";
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  @EqualsAndHashCode
  public static class UserRequestDto {

    @NotBlank(groups = { ValidatedPatchGroup.class })
    private String userId;

    @NotBlank(groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class })
    @Email(groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class })
    @Size(
      max = 100,
      groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class }
    )
    private String email;

    @NotBlank(groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class })
    @Size(
      max = 50,
      groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class }
    )
    private String userName;

    @NotBlank(groups = { ValidatedPostGroup.class, ValidatedPatchGroup.class })
    private String password;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  @EqualsAndHashCode
  public static class UserResponseDto {

    private String userId;

    private String email;

    private String userName;

    private String updatedAt;

    private List<OrderDto> orderList;
  }

  public interface ValidatedPostGroup {}

  public interface ValidatedPatchGroup {}
}
