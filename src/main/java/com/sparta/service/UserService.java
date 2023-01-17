package com.sparta.service;

import com.sparta.dto.ResponseStatusDto;
import com.sparta.dto.SignInRequestDto;
import com.sparta.dto.SignUpRequestDto;
import com.sparta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public ResponseStatusDto signUp(SignUpRequestDto signUpRequestDto) {
        return null;
    }

    public ResponseStatusDto signIn(SignInRequestDto signInRequestDto) {
        return null;
    }

    public ResponseStatusDto signOut() {
        return null;
    }

    public ResponseStatusDto applySellerRole() {
        return null;
    }

}
