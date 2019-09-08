package org.fkjava.wechat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
public class RedisLock implements Lock {

    private String key;
    private StringRedisTemplate stringRedisTemplate;
    private long lockTimeout;

    public RedisLock(String key, StringRedisTemplate stringRedisTemplate) {
        this(key, stringRedisTemplate, 50, TimeUnit.SECONDS);
    }

    public RedisLock(String key, StringRedisTemplate stringRedisTemplate, long timeout, TimeUnit unit) {
        this.key = key;
        this.stringRedisTemplate = stringRedisTemplate;
        lockTimeout = unit.toMillis(timeout);
    }

    @Override
    public void lock() {
        //如果被其他程序锁定，则无限锁定
        try {
            lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RuntimeException("加锁失败: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // 跟lock一样的，只是可以被中断
        while (!tryLock()) {
//            log.trace("等待200毫秒以后重试获得锁");
            Thread.sleep(200);
        }
    }

    @Override
    public boolean tryLock() {
        BoundValueOperations<String, String> lockOps = stringRedisTemplate.boundValueOps(key);
        // 如果key不存在，则设置为值为locked的字符串放入数据库
        // 并且该key的有效期是50秒
        // 如果key存在，则返回false
        String value = Thread.currentThread().getName();
        // setIfAbsent对应的Redis命令是setnx
        return lockOps
                .setIfAbsent(value, lockTimeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        long start = System.currentTimeMillis();

        long time = unit.toMillis(timeout);
//        log.trace("当前时间: {}, 等待时长{}，超时时间{}", System.currentTimeMillis(), time, start + time);
        while (!tryLock()) {
//            log.trace("没有得到锁，等待一定时间");
            // 没有锁定就循环，等待200毫秒以后再尝试一次
            // 如果时间已经到达，则不再等待，直接返回false
            if (System.currentTimeMillis() > start + time) {
                // 等待超时
//                log.trace("锁等待超时");
                return false;
            }
            Thread.sleep(200);
        }
        return true;
    }

    @Override
    public void unlock() {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
