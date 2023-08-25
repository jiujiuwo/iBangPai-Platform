package com.aibangpai.platform.service;

public interface LockService {
    String lock();
    void failLock();
    String properLock();
}