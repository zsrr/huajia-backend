package com.sssta.huajia.config;

import com.sssta.huajia.Constants;
import com.stephen.a2.config.A2WebConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ControllerAdvice;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.sssta.huajia.controller", "com.sssta.huajia.exception"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ControllerAdvice.class))
@EnableAsync
public class WebConfig extends A2WebConfig {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected JedisPool getJedisPool() {
        return (JedisPool) getApplicationContext().getBean("jedisPool");
    }

    @Override
    protected String getSuffix() {
        return Constants.TOKEN_SUFFIX;
    }
}
