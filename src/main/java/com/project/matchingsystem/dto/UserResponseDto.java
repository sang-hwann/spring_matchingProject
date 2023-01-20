package com.project.matchingsystem.dto;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.domain.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private UserRoleEnum userRoleEnum;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userRoleEnum = user.getUserRole();
    }

}
