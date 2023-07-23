package io.hskim.learnspringclouduserservice.repo;

import io.hskim.learnspringclouduserservice.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, UUID> {}
