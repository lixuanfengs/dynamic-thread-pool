package cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.valobj;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.sdk.domain.model.valobj
 * Description:
 *  注册中心枚举值对象 key
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/17 15:59
 * @Github https://github.com/lixuanfengs
 */
public enum RegistryEnumVO {

    THREAD_POOL_CONFIG_LIST_KEY("THREAD_POOL_CONFIG_LIST_KEY", "池化配置列表"),
    THREAD_POOL_CONFIG_PARAMETER_LIST_KEY("THREAD_POOL_CONFIG_PARAMETER_LIST_KEY", "池化配置参数"),
    DYNAMIC_THREAD_POOL_REDIS_TOPIC("DYNAMIC_THREAD_POOL_REDIS_TOPIC", "动态线程池监听主题配置");

    private final String key;
    private final String desc;

    RegistryEnumVO(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }


}
