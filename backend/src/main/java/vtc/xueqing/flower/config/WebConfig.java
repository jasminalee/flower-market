package vtc.xueqing.flower.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前后端整合核心配置：前端静态资源映射 + SPA 路由配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:./uploads/images/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 映射静态资源到 classpath:/static/
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        
        // 2. 映射上传的图片文件 - 统一使用 /images/** 路径
        String normalizedUploadPath = uploadPath.replace("\\", "/");
        if (!normalizedUploadPath.endsWith("/")) {
            normalizedUploadPath += "/";
        }
        
        // Map /images/** to file system path [uploadPath]/
        // For example: /images/products/main/xxx.jpg -> uploads/images/products/main/xxx.jpg
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + normalizedUploadPath);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 重要：SPA 应用必须这样配置，让所有非 API 路由都返回 index.html
        // 这样前端的 Vue Router 才能正确处理路由
        
        // 1. 根路径 / 返回 index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        
        // 2. 所有其他非 API、非静态资源的路由都返回 index.html
        // 这个配置的优先级要比 addResourceHandlers 低，所以对 /api, /images, /static 等没有影响
        registry.addViewController("/{spring:^(?!api|images|static|assets)[^\\.]*}").setViewName("forward:/index.html");
        
        // 3. 多层路由（如 /merchant/products）也转发到 index.html
        registry.addViewController("/{spring:^(?!api|images|static|assets)[^\\.]*}/{spring:^[^\\.]*}").setViewName("forward:/index.html");
        
        // 4. 更深层的路由支持
        registry.addViewController("/{spring:^(?!api|images|static|assets)[^\\.]*}/{spring:^[^\\.]*}/{spring:^[^\\.]*}").setViewName("forward:/index.html");
    }
}