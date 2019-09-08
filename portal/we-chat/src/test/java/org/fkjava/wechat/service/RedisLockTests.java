package org.fkjava.wechat.service;

import org.fkjava.wechat.WeChatApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

// 单元测试由Spring来引导，可以使用Spring的依赖注入
@RunWith(SpringRunner.class)
// 启用Spring Boot的单元测试
@SpringBootTest
// 指定配置文件的类名或者路径
@ContextConfiguration(classes = {WeChatApp.class})
public class RedisLockTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private RedisLock rl;

    @Before
    public void init() {
        rl = new RedisLock("TEST", stringRedisTemplate);
    }

    @Test
    public void testLock() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("线程1开始执行");
//            rl.lock();
            try {
                boolean locked = rl.tryLock(5, TimeUnit.SECONDS);
                System.out.println("线程2加锁: " + locked);
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rl.unlock();
            System.out.println("线程1完成，并且释放了锁");
        });
        t.start();


        Thread t2 = new Thread(() -> {
            System.out.println("线程2开始执行");
//            rl.lock();
            try {
                boolean locked = rl.tryLock(5, TimeUnit.SECONDS);
                System.out.println("线程2加锁: " + locked);
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rl.unlock();
            System.out.println("线程2完成，并且释放了锁");
        });
        t2.start();

        t.join();
        t2.join();

        t.join();
    }

}
