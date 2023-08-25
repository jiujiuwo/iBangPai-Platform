package com.aibangpai.platform.service.imp;

import com.aibangpai.platform.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Service
public class RedisLockService implements LockService {
    private static final String MY_LOCK_KEY = "redisLocKey";

    @Autowired
    private LockRegistry lockRegistry;

    @Override
    public String lock() {
        var lock = lockRegistry.obtain(MY_LOCK_KEY);
        String returnVal = null;
        if (lock.tryLock()) {
            returnVal = "jdbc lock successful";
        } else {
            returnVal = "jdbc lock unsuccessful";
        }
        lock.unlock();
        return returnVal;
    }

    @Override
    public void failLock() {
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                log.info("Attempting to lock with thread: " + uuid);
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.info("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                } else {
                    log.info("failed to lock with thread: " + uuid);
                }
            } catch (Exception e0) {
                log.info("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                log.info("unlocked with thread: " + uuid);
            }
        };

        Runnable lockThreadTwo = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                log.info("Attempting to lock with thread: " + uuid);
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.info("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                } else {
                    log.info("failed to lock with thread: " + uuid);
                }
            } catch (Exception e0) {
                log.info("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                log.info("unlocked with thread: " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }

    @Override
    public String properLock() {
        Lock lock = null;
        try {
            lock = lockRegistry.obtain(MY_LOCK_KEY);
        } catch (Exception e) {
            // in a production environment this should be a log statement
            System.out.println(String.format("Unable to obtain lock: %s", MY_LOCK_KEY));
        }
        String returnVal = null;
        try {
            if (lock.tryLock()) {
                returnVal = "jdbc lock successful";
            } else {
                returnVal = "jdbc lock unsuccessful";
            }
        } catch (Exception e) {
            // in a production environment this should log and do something else
            e.printStackTrace();
        } finally {
            // always have this in a `finally` block in case anything goes wrong
            lock.unlock();
        }

        return returnVal;
    }
}