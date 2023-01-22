package com.project.matchingsystem.dto.response;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.enums.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private UserRoleEnum userRole;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userRole = user.getUserRole();
    }

}
