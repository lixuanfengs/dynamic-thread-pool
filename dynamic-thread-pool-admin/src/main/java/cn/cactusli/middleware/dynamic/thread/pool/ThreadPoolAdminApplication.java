package cn.cactusli.middleware.dynamic.thread.pool;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool
 * Description:
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/26 11:28
 * @Github https://github.com/lixuanfengs
 */
@SpringBootApplication
@Configurable
public class ThreadPoolAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreadPoolAdminApplication.class, args);
    }

}
