package cn.cactusli.middleware.dynamic.thread.pool.types;

import lombok.*;

import java.io.Serializable;

/**
 * Package: cn.cactusli.middleware.dynamic.thread.pool.types
 * Description:
 *
 * @Author 仙人球⁶ᴳ | 微信：Cactusesli
 * @Date 2024/6/27 9:29
 * @Github https://github.com/lixuanfengs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -356547565140249285L;

    private String code;
    private String info;
    private T data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Code {
        SUCCESS("0000", "调用成功"),
        UN_ERROR("0001", "调用失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        ;

        private String code;
        private String info;

    }

}
