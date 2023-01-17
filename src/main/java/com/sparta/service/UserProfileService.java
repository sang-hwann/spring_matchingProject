package com.sparta.service;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.UserProfileRequestDto;
import com.sparta.dto.UserProfileResponseDto;
import com.sparta.repository.UserRepository;
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
