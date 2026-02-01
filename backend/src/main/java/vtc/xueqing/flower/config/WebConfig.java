package vtc.xueqing.flower.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前后端整合核心配置：前端静态资源映射 + 首页跳转
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:./uploads/images/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        
        // 映射上传的图片文件 - 直接映射到 products 路径
        // Ensure the upload path ends with separator for proper path concatenation
        String normalizedUploadPath = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        // Map /products/** to the correct physical location: [uploadPath]/products/
        // For example, if uploadPath is "./uploads/images/", then /products/** maps to ./uploads/images/products/**
        registry.addResourceHandler("/products/**")
                .addResourceLocations("file:" + normalizedUploadPath + "products/");
        
        // Also keep the general uploads mapping for other file types if needed
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }

    // 2. 配置首页默认跳转，访问根路径 http://localhost:8080 直接返回 index.html
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}