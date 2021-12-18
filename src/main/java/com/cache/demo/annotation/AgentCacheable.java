package com.cache.demo.annotation;

/**
 * @author york
 * @create 2021-03-24 下午3:39
 **/
public @interface AgentCacheable {

    String domain() default "";

    String domainKey() default "";

    String key() default "";

    String fieldKey() default "";

    String condition() default "";

    String expireTime() default "";
}
