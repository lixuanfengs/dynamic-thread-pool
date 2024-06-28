package cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.redis;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.registry.redis
 * Description:
 *  Redis 注册中心接口实现类
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/17 15:56
 * @Github https://github.com/lixuanfengs
 */
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        RList<Object> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.clear();
        list.addAll(threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }

}
