package com.aibangpai.platform.lockregistry;

import com.aibangpai.platform.service.imp.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RedissonTest {

    @Autowired
    private RedisLockService redisLockService;

    @Test
    void contextLoads() throws InterruptedException {

        redisLockService.failLock();
    }

}
