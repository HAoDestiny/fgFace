package net.sh.rgface.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by DESTINY on 2018/3/30.
 */

@Configuration
public class WebSocketConfig {

    /**
     * 本地运行 添加注解@Bean
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
