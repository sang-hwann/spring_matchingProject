package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    void beforeEach() {
//        ItemRequestDto itemRequestDto = new ItemRequestDto("itemA", null, "", 10000);
//        User user = new User("userA123", "password123", UserRoleEnum.USER);
//        Item item = new Item(itemRequestDto, user);
//        userRepository.save(user);
//        itemRepository.save(item);
//        transactionRepository.save(new Transaction(item, user));
    }

    @Test
    void joinUserProfile() {
        List<Object[]> objects = transactionRepository.findByItemIdWithUserProfile(1L);
        log.info("start");
        for (Object[] object : objects) {
            Transaction transaction = (Transaction) object[0];
            log.info("transaction.getItem()={}", transaction.getItem());
            String nickname = (String) object[1];
            log.info("nickname={}", nickname);
        }
        log.info("end");
    }

}