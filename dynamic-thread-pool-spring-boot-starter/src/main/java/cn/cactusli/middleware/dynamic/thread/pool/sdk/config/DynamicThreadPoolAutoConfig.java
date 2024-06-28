package cn.cactusli.middleware.dynamic.thread.pool.sdk.config;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.DynamicThreadPoolService;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.redis.RedisRegistry;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigAdjustListener;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.config
 * Description:
 * 动态配置入口
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/13 17:41
 * @Github https://github.com/lixuanfengs
 */
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
@EnableScheduling
public class DynamicThreadPoolAutoConfig {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    private String applicationName;

    @Bean("redissonClient")
    public RedissonClient redissonClient(DynamicThreadPoolAutoProperties poolAutoProperties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + poolAutoProperties.getHost() + ":" + poolAutoProperties.getPort())
                .setPassword(poolAutoProperties.getPassword())
                .setConnectionPoolSize(poolAutoProperties.getPoolSize())
                .setConnectionMinimumIdleSize(poolAutoProperties.getMinIdleSize())
                .setIdleConnectionTimeout(poolAutoProperties.getIdleTimeout())
                .setConnectTimeout(poolAutoProperties.getConnectTimeout())
                .setRetryAttempts(poolAutoProperties.getRetryAttempts())
                .setRetryInterval(poolAutoProperties.getRetryInterval())
                .setPingConnectionInterval(poolAutoProperties.getPingInterval())
                .setKeepAlive(poolAutoProperties.isKeepAlive());

        RedissonClient redissonClient = Redisson.create(config);
        logger.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", poolAutoProperties.getHost(), poolAutoProperties.getPoolSize(), !redissonClient.isShutdown());
        return redissonClient;
    }

    @Bean
    public IRegistry redisRegistry(RedissonClient redissonClient) {
        return new RedisRegistry(redissonClient);
    }

    @Bean
    public ThreadPoolDataReportJob threadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolDataReportJob(dynamicThreadPoolService, registry);
    }

    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isBlank(applicationName)) {
            applicationName = "缺省的";
            logger.warn("动态线程池，启动提示。spring.application.name 为空，无法获取应用名称，使用缺省的应用名称。");
        }

        // 获取缓存数据，设置本地线程池配置
        RedissonClient redissonClient = applicationContext.getBean(RedissonClient.class);
        Set<String> threadPoolkeys =  threadPoolExecutorMap.keySet();
        for (String threadPoolKey : threadPoolkeys) {
            ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + applicationName + "_" + threadPoolKey).get();
            // 如果缓存中有线程池配置，则设置线程池配置
            if (null == threadPoolConfigEntity) continue;
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolKey);
            threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
            threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        }
        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }

    @Bean
    public ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolConfigAdjustListener(dynamicThreadPoolService, registry);
    }

    @Bean(name = "dynamicThreadPoolRedisTopic")
    public RTopic threadPoolConfigAdjustListenerTopic(RedissonClient redissonClient, ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener) {
        RTopic topic = redissonClient.getTopic(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey() + "_" + applicationName);
        topic.addListener(ThreadPoolConfigEntity.class, threadPoolConfigAdjustListener);
        return topic;
    }

}
