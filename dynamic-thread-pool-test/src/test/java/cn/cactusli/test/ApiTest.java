package cn.cactusli.test;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

/**
 * Package: cn.cactusli.test
 * Description:
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/13 18:18
 * @Github https://github.com/lixuanfengs
 */
@Slf4j
@SpringBootTest
public class ApiTest {

    @Resource
    private RTopic dynamicThreadPoolRedisTopic;

    @Test
    public void test_dynamicThreadPoolRedisTopic() throws InterruptedException {
        ThreadPoolConfigEntity threadPoolExecutor01 = new ThreadPoolConfigEntity("dynamic-thread-pool-test-app", "threadPoolExecutor01");
        threadPoolExecutor01.setCorePoolSize(80);
        threadPoolExecutor01.setMaximumPoolSize(120);
        dynamicThreadPoolRedisTopic.publish(threadPoolExecutor01);
        new CountDownLatch(1).await();
    }
}
