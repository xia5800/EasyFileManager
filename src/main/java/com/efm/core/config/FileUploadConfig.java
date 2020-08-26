package com.efm.core.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置
 * @author gcc
 * @date 2020/8/24 16:52
 */
@Configuration
public class FileUploadConfig {

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大10G
        factory.setMaxFileSize(DataSize.of(10, DataUnit.GIGABYTES));
        // 多个文件上传数据总大小100G
        factory.setMaxRequestSize(DataSize.of(100, DataUnit.GIGABYTES));
        return factory.createMultipartConfig();
    }

}
