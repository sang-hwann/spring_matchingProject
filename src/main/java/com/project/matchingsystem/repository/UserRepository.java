package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByImagePath(String imagePath);

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    List<User> findByUserRole(UserRoleEnum userRole);

}
