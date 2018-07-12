package net.sh.rgface;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by DESTINY on 2018/3/30.
 */

@ServletComponentScan //扫描Listener
@SpringBootApplication
@EnableCaching
//@EnableAsync //@Async异步调用生效
public class RgfaceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RgfaceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RgfaceApplication.class, args);
    }
}

