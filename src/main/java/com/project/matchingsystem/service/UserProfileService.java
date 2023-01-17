package com.project.matchingsystem.service;

import com.project.matchingsystem.dto.ResponseStatusDto;
import com.project.matchingsystem.dto.UserProfileRequestDto;
import com.project.matchingsystem.dto.UserProfileResponseDto;
import com.project.matchingsystem.repository.UserRepository;
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
