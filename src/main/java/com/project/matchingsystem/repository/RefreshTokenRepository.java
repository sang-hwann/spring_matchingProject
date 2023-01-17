package com.sparta.matchingsystem.repository;

import com.sparta.matchingsystem.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}