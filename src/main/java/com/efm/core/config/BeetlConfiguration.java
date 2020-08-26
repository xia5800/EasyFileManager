package com.efm.core.config;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beetl配置
 * @author gcc
 * @date 2020/8/24 12:30
 */
@Configuration
public class BeetlConfiguration {
    /** 模板根目录 */
    @Value("${beetl.templatesPath:templates}")
    private String templatesPath;
    /** 模板后缀 */
    @Value("${beetl.suffix:html}")
    private String suffix;
    /** 开启热加载 */
    @Value("${beetl.dev:true}")
    private boolean dev;

    @Bean(name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration bguc = new BeetlGroupUtilConfiguration();
        // 配置Beetl资源加载器
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = BeetlConfiguration.class.getClassLoader();
        }
        bguc.setResourceLoader(new ClasspathResourceLoader(loader, templatesPath));
        bguc.init();
        return bguc;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        // 设置后缀后即可在controller中使用 return "/hello" 而不必 return "hello.html"
        beetlSpringViewResolver.setSuffix(".".concat(suffix));
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        return beetlSpringViewResolver;
    }

}

