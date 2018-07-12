package net.sh.rgface.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by DESTINY on 2018/4/12.
 */

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptors())
                .addPathPatterns("/sh/**")
                .excludePathPatterns("/sh/")
                .excludePathPatterns("/sh/login")
                .excludePathPatterns("/sh/static/**")

                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/static/admin/**")

                .addPathPatterns("/personnel/**")
                .excludePathPatterns("/personnel/")
                .excludePathPatterns("/personnel/login")
                .excludePathPatterns("/personnel/static/**")
                .excludePathPatterns("/personnel/static/admin/**")

                .addPathPatterns("/api/**")
                .addPathPatterns("/api/user/login")
                .excludePathPatterns("/api/stream")
                .excludePathPatterns("/api/webTime/getWebTime")
                .excludePathPatterns("/api/base/getPublicKey")
                .excludePathPatterns("/api/verification");


        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/sh/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/personnel/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/admin/static/admin/**").addResourceLocations("classpath:/static/admin/");
        super.addResourceHandlers(registry);
    }
}
