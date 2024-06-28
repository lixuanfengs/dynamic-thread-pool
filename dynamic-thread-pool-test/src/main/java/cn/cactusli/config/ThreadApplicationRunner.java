package cn.cactusli.config;

/**
 * Package: cn.cactusli.config
 * Description:
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/28 10:56
 * @Github https://github.com/lixuanfengs
 */

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadApplicationRunner {

    @Bean("threadApplicationRunner11")
    public ApplicationRunner threadApplicationRunner11(ExecutorService threadPoolExecutor02, ExecutorService threadPoolExecutor01) {
        return args -> {

            // 创建一个随机时间生成器
            Random random = new Random();
            // 用于切换线程池
            boolean useFirstPool = true;

           while (true) {
               // 随机时间，用于模拟任务启动延迟
               int initialDelay = random.nextInt(10) + 1; // 1到10秒之间
               // 随机休眠时间，用于模拟任务执行时间
               int sleepTime = random.nextInt(10) + 1; // 1到10秒之间

               // 选择线程池并提交任务
               ExecutorService selectedThreadPool = useFirstPool ? threadPoolExecutor02 : threadPoolExecutor01;
               // 切换到另一个线程池
               useFirstPool = !useFirstPool;

               // 提交任务到线程池
               selectedThreadPool.submit(() -> {
                   try {
                       // 模拟任务启动延迟
                       TimeUnit.SECONDS.sleep(initialDelay);
                       System.out.println("Task started after " + initialDelay + " seconds.");

                       // 模拟任务执行
                       TimeUnit.SECONDS.sleep(sleepTime);
                       System.out.println("Task executed for " + sleepTime + " seconds.");
                   } catch (InterruptedException e) {
                       Thread.currentThread().interrupt();
                   }
               });
               // 主线程休眠，用于模拟任务提交间隔
               Thread.sleep(random.nextInt(50) + 1);
           }
        };
    }
}
