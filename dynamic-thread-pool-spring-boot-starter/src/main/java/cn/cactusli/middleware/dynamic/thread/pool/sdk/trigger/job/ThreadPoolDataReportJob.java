package cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.job;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.job
 * Description:
 *  线程池数据上报任务
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/17 16:13
 * @Github https://github.com/lixuanfengs
 */
public class ThreadPoolDataReportJob {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void execReportThreadPoolList() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池信息：{}", JSON.toJSONString(threadPoolConfigEntities));

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
        }
    }
}
