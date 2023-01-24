package com.project.matchingsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String sellerName;

    public Chatting(String roomId, String roomName, String userName, String sellerName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userName = userName;
        this.sellerName = sellerName;
    }

}
