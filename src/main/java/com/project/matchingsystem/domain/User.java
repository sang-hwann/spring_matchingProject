package com.project.matchingsystem.domain;

import com.project.matchingsystem.enums.UserRoleEnum;
import com.project.matchingsystem.dto.request.UserProfileRequestDto;
import com.project.matchingsystem.dto.response.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "users")
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String imagePath;

    @Column(nullable = false)
    private String content = "소개글을 입력하세요";

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;

    public User(String username, String password, UserRoleEnum userRole, String nickname) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    // 어드민 가입용
    public User(String username, String password, UserRoleEnum userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public void updateUserProfile(UserProfileRequestDto userProfileRequestDto) {
        this.nickname = userProfileRequestDto.getNickname();
        this.content = userProfileRequestDto.getContent();
        this.imagePath = userProfileRequestDto.getImagePath();
    }

    public void updateImage(String imagePath) {
        this.imagePath = imagePath;
    }

    // 유저 권한 판매자로 수정
    public void permitRoleUser() {
        this.userRole = UserRoleEnum.SELLER;
    }

    //판매자,유저 권한 유저로 권환 전환
    public void dropRoleUser() {
        this.userRole = UserRoleEnum.USER;
    }

    public UserResponseDto toUserResponseDto() {
        return new UserResponseDto(this);
    }

}
