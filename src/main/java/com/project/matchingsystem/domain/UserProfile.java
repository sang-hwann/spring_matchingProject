//package com.project.matchingsystem.domain;
//
//import com.project.matchingsystem.dto.UserProfileRequestDto;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.DynamicInsert;
//
//import javax.persistence.*;
//
//@Getter
//@NoArgsConstructor
//@Entity
//@DynamicInsert // 칼럼 생성 시 null 안들어가고 default 값이 들어갈 수 있게 해줌
//public class UserProfile {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(nullable = false, unique = true)
//    private String nickname;
//
//    @Lob // 이미지는 일단 나중에 하는걸로 일단 null로 해놔
//    private byte[] image;
//
//    @Column(columnDefinition = "clob default '소개글을 입력하세요'") // 길이제한을 두지 않음
//    private String content;
//
//    public UserProfile(User user, String nickname) {
//        this.user = user;
//        this.nickname = nickname;
//    }
//
//    public void update(UserProfileRequestDto userProfileRequestDto) {
//        this.nickname = userProfileRequestDto.getNickname();
//        this.content = userProfileRequestDto.getContent();
//        this.image = userProfileRequestDto.getImage();
//    }
//}
