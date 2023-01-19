package com.project.matchingsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Transaction extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private TransactionStatusEnum transactionStatus;

    public Transaction(Item item, User user) {
        this.item = item;
        this.user = user;
        this.transactionStatus = TransactionStatusEnum.HOLD;
    }

    public void updateStatusToComplete() {
        this.transactionStatus = TransactionStatusEnum.COMPLETE;
    }

    public void cancelStatusToCancel() {
        this.transactionStatus = TransactionStatusEnum.CANCEL;
    }

}
