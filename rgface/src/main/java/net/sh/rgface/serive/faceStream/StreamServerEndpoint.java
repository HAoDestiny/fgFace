package net.sh.rgface.serive.faceStream;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.ConfigServerConfig;
import com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import com.ha.facecamera.configserver.events.StreamDataReceivedEventHandler;
import org.springframework.stereotype.Component;

//@WebListener
//@Component
//@ServerEndpoint("/api/stream/{device-id}")
public class StreamServerEndpoint implements ServletContextListener{
    static CopyOnWriteArraySet<StreamServerEndpoint> webSocketSet = new CopyOnWriteArraySet<StreamServerEndpoint>();
    private static Object locker = new Object();
    private static ConfigServer configServer;
    private static StreamDataReceiver streamDataReceiver;
    
    Session session;
	String deviceID;
    
	@OnOpen
    public void onOpen(@PathParam("device-id") String deviceID, Session session) {
    	this.session = session;
		System.out.println("------------------------------- open");
    	if(deviceID == null || deviceID.trim().isEmpty()) return;

    	this.deviceID = deviceID;

        webSocketSet.add(this);

        synchronized (locker) {
        	if(!configServer.getIsStreamStartByDeviceNo(deviceID))
        		configServer.startStreamByDeviceNo(deviceID);
        }
    }

    @OnClose
    public void onClose(@PathParam("device-id") String deviceID) {

		System.out.println("webSocket onClose----------");

        webSocketSet.remove(this);
        synchronized (locker) {
        	boolean allDisconnected = true;
        	for(StreamServerEndpoint sse : webSocketSet) {
        		if(sse.deviceID.equals(deviceID)) {
        			allDisconnected = false;
        			break;
        		}
        	}

        	if(allDisconnected) configServer.stopStreamByDeviceNo(deviceID);
        }
    }
    
    @OnError
    public void onError(Throwable t) {
    	
    }

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		System.out.println("-------------------------------stop");

		configServer.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("-------------------------------start");

		System.out.println(arg0.getServletContext().getContextPath());
		configServer = new ConfigServer();
		streamDataReceiver = new StreamDataReceiver();
		configServer.onStreamDataReceived(streamDataReceiver);
		configServer.start(10112, new ConfigServerConfig());

		configServer.onCameraConnected(new CameraConnectedEventHandler() {
			@Override
			public void onCameraConnected(String s) {
				System.out.println("---------- 设备连接-Code：" + s);
			}
		});
	}
}
