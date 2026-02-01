package vtc.xueqing.flower.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前后端整合核心配置：前端静态资源映射 + 首页跳转
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. 配置静态资源访问，映射前端打包的静态文件
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 访问 / 开头的请求，都去 resources/static 目录找对应静态文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    // 2. 配置首页默认跳转，访问根路径 http://localhost:8080 直接返回 index.html
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}