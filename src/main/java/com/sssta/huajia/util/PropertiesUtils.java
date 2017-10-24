package com.sssta.huajia.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    // location为classpath下路径
    public static Properties loadProperties(String location) {
        ClassPathResource classPathResource = new ClassPathResource(location, PropertiesUtils.class.getClassLoader());
        try {
            return PropertiesLoaderUtils.loadProperties(classPathResource);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
