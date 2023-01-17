package com.sparta.matchingsystem.service;

import com.sparta.matchingsystem.dto.ResponseStatusDto;
import com.sparta.matchingsystem.dto.UserProfileRequestDto;
import com.sparta.matchingsystem.dto.UserProfileResponseDto;
import com.sparta.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileResponseDto getUserProfile(Long userId) {
        return null;
    }

    public ResponseStatusDto updateUserProfile(UserProfileRequestDto userProfileRequestDto) {
        return null;
    }

}
