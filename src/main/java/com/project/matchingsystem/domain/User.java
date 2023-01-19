package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRoleEnum;

    public User(String username, String password, UserRoleEnum userRoleEnum) {
        this.username = username;
        this.password = password;
        this.userRoleEnum = userRoleEnum;
    }
    // 유저 권한 판매자로 수정
    public void permitRoleUser() {
        this.userRoleEnum = UserRoleEnum.SELLER;
    }
    //판매자,유저 권한 유저로 권환 전환
    public void dropRoleUser() {
        this.userRoleEnum = UserRoleEnum.USER;
    }

    public UserResponseDto toUserResponseDto(){
        return new UserResponseDto(this); //stream
    };
}
