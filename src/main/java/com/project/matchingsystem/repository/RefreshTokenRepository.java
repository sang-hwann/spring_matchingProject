package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
