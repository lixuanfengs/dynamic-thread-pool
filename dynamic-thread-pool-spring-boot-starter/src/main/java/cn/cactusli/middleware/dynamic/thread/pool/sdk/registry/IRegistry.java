package cn.cactusli.middleware.dynamic.thread.pool.sdk.registry;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.registry
 * Description:
 *   注册中心接口
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/17 15:27
 * @Github https://github.com/lixuanfengs
 */
public interface IRegistry {

    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities);

    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
