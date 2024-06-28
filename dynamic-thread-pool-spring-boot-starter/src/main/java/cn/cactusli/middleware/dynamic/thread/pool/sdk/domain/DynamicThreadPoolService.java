package cn.cactusli.middleware.dynamic.thread.pool.sdk.domain;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.domain
 * Description:
 *   动态线程池服务接口的实现
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/14 10:24
 * @Github https://github.com/lixuanfengs
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);

    private final String applicationName;

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolService(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;

    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        Set<String> threadPoolKeys = threadPoolExecutorMap.keySet();
        List< ThreadPoolConfigEntity> threadPoolConfigEntities =  new ArrayList<>(threadPoolKeys.size());
        for (String beanName : threadPoolKeys) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(beanName);
            ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity(applicationName, beanName);
            threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
            threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
            threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
            threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolConfigEntity.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
            threadPoolConfigEntities.add(threadPoolConfigEntity);
        }
        return threadPoolConfigEntities;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (null == threadPoolExecutor) return new ThreadPoolConfigEntity(applicationName, threadPoolName);

        // 线程池配置项
        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity(applicationName, threadPoolName);
        threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
        threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolConfigEntity.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());

        if (logger.isDebugEnabled()) {
            logger.info("动态线程池，配置查询，应用名：{} 线程名：{} 池化配置：{}", applicationName, threadPoolName, JSON.toJSONString(threadPoolConfigEntity));
        }
        return threadPoolConfigEntity;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {

        if (null == threadPoolConfigEntity || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (null == threadPoolExecutor) return;

        // 更新线程池配置 仅更新核心线程数和最大线程数
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
    }

}
