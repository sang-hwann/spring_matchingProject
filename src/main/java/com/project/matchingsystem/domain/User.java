package com.project.matchingsystem.domain;

import com.project.matchingsystem.dto.UserProfileRequestDto;
import com.project.matchingsystem.dto.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "users")
@DynamicInsert
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Lob // 이미지는 일단 나중에 하는걸로 일단 null로 해놔
    private byte[] image;

    @Column(columnDefinition = "clob default '소개글을 입력하세요'") // 길이제한을 두지 않음
    private String content;

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
        this.image = userProfileRequestDto.getImage();
    }

    // 유저 권한 판매자로 수정
    public void permitRoleUser() {
        this.userRole = UserRoleEnum.SELLER;
    }
    //판매자,유저 권한 유저로 권환 전환
    public void dropRoleUser() {
        this.userRole = UserRoleEnum.USER;
    }

    public UserResponseDto toUserResponseDto(){
        return new UserResponseDto(this);
    };
}
