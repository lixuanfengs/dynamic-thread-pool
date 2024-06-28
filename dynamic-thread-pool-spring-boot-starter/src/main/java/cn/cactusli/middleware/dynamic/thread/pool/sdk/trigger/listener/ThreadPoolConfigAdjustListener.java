package cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.listener;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import com.alibaba.fastjson2.JSON;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.listener
 * Description:
 *  动态线程池变更监听器
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/21 10:53
 * @Github https://github.com/lixuanfengs
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}", threadPoolConfigEntity.getThreadPoolName(), threadPoolConfigEntity.getCorePoolSize(), threadPoolConfigEntity.getMaximumPoolSize());

        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        // 更新过后，重新上报线程池信息
        registry.reportThreadPool(dynamicThreadPoolService.queryThreadPoolList());
        
        // 上报线程池配置
        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntityCurrent));

    }
}
