package vtc.xueqing.flower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS configuration.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow all origins (development only).
        config.addAllowedOriginPattern("*");
        
        // Allow all request headers.
        config.addAllowedHeader("*");
        
        // Allow all HTTP methods.
        config.addAllowedMethod("*");
        
        // Allow credentials.
        config.setAllowCredentials(true);
        
        // Exposed response headers.
        config.addExposedHeader("Authorization");
        
        // Preflight cache duration in seconds.
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
