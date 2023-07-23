package io.hskim.learnspringclouduserservice.entity;

import io.hskim.learnspringclouduserservice.dto.UserDto.UserResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(
  name = "aa_user",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "email", name = "UK_aa_1"),
  }
)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter(value = AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, exclude = {})
@ToString(callSuper = false, exclude = {})
public class UserEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  @Comment(value = "사용자 ID")
  private UUID userId;

  @Column(length = 100, nullable = false)
  @Comment(value = "사용자 이메일")
  private String email;

  @Column(length = 50, nullable = false)
  @Comment(value = "사용자 이름")
  private String userName;

  @Column(length = 100, nullable = false)
  @Comment(value = "비밀번호")
  private String password;

  @LastModifiedDate
  @Column(nullable = false)
  @ColumnDefault(value = "now()")
  @Comment(value = "수정일시")
  private LocalDateTime updatedAt;

  public void setPassword(String password) {
    if (StringUtils.hasText(password)) {
      this.password = password;
    }
  }

  public UserResponseDto toDto() {
    return UserResponseDto
      .builder()
      .userId(this.userId.toString())
      .email(this.email)
      .userName(this.userName)
      .updatedAt(
        this.updatedAt != null
          ? this.updatedAt.format(
              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
          : ""
      )
      .build();
  }
}
