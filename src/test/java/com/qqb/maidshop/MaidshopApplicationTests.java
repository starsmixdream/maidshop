package com.qqb.maidshop;

import com.qqb.maidshop.mapper.UserMapper;
import com.qqb.maidshop.model.User;
import com.qqb.maidshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MaidshopApplicationTests {

    @Value("${animation.recommend-num}")
    private int num;

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        System.out.println(num);
//        User user = User.builder()
//                .nickname("轻轻白")
//                .password("123456")
//                .build();
//        userService.registerUser(user);
    }

}
