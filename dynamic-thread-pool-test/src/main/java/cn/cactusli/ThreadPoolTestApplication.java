package cn.cactusli;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Package: cn.cactusli.config
 * Description:
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/13 18:06
 * @Github https://github.com/lixuanfengs
 */
@SpringBootApplication
@Configurable
public class ThreadPoolTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreadPoolTestApplication.class);
    }

}
