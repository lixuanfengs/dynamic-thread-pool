package cn.cactusli.middleware.dynamic.thread.pool.sdk.domain;

import cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.domain
 * Description:
 *  动态线程池服务接口
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/13 18:38
 * @Github https://github.com/lixuanfengs
 */
public interface IDynamicThreadPoolService {

    List<ThreadPoolConfigEntity> queryThreadPoolList();

    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
