package net.sh.rgface.config;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.ConfigServerConfig;
import com.ha.facecamera.configserver.DataServer;
import com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.sh.rgface.serive.faceStream.StreamDataReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by DESTINY on 2018/5/16.
 */

@Slf4j
@WebListener
@Order(value = 1) //执行顺序
public class ApplicationInitRunner implements ServletContextListener {

    private static ConfigServer configServer;

    private static StreamDataReceiver streamDataReceiver;

    private static boolean isFirstStartDataService = false;

    public static ConfigServer getConfigServer() {
        return ApplicationInitRunner.configServer;
    }

    public static boolean getIsFirstStartDataService() {
        return ApplicationInitRunner.isFirstStartDataService;
    }

    public static void setIsFirstStartDataService(boolean status) {

        ApplicationInitRunner.isFirstStartDataService = status;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info(" ------------- Init Success 初始化成功");
        log.error(" ------------- Init Success 初始化成功");
        configServer = new ConfigServer();
        streamDataReceiver = new StreamDataReceiver();
        configServer.onStreamDataReceived(streamDataReceiver);
        boolean ret = configServer.start(10002, new ConfigServerConfig()); //穿透端口 启动配置服务

        if (!ret) {
            log.info(" ------------- 启动配置服务器失败");
        }

        configServer.onCameraConnected(new CameraConnectedEventHandler() {
            @Override
            public void onCameraConnected(String s) {
                log.info(" ------------- 设备已连接-配置端口:" + s);
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        if (configServer != null) {
            configServer.stop();
        }
    }
}
