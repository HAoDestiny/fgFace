package net.sh.rgface.serive.faceStream;

import com.alibaba.fastjson.JSON;
import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.DataServer;
import com.ha.facecamera.configserver.DataServerConfig;
import com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import com.ha.facecamera.configserver.events.CameraDisconnectedEventHandler;
import com.ha.facecamera.configserver.events.CaptureCompareDataReceivedEventHandler;
import com.ha.facecamera.configserver.pojo.CaptureCompareData;
import lombok.extern.slf4j.Slf4j;
import net.sh.rgface.ApplicationContextRegister;
import net.sh.rgface.config.ApplicationInitRunner;
import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.FileEntity;
import net.sh.rgface.entity.PersonnelCountEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.po.file.FaceMessagePo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.WebSocketPo;
import net.sh.rgface.serive.base.FileService;
import net.sh.rgface.serive.admin.AdminDeviceService;
import net.sh.rgface.serive.admin.AdminRecordingService;
import net.sh.rgface.serive.personnel.PersonnelCountService;
import net.sh.rgface.serive.personnel.PersonnelService;
import net.sh.rgface.util.StreamSessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by DESTINY on 2018/3/30.
 */

//@WebListener
@Slf4j
@Component
@ServerEndpoint(value = "/api/stream/{device-id}")
public class StreamDataWebSocketService {

    Session mSession;
    String deviceID;

    private static int onlineCount = 0;

    private Lock mLock = new ReentrantLock();

    private ConfigServer configServer;
    private DataServer mDataServer;

    private static StreamSessionManager streamSessionManager;

    private FilePo filePo;
    private ResultPo resultPo;
    private WebSocketPo webSocketPo;
    private FaceMessagePo faceMessagePo;

    private FileService fileService;
    private PersonnelService personnelService;
    private AdminDeviceService adminDeviceService;
    private AdminRecordingService adminRecordingService;
    private PersonnelCountService personnelCountService;

    private boolean isFirstStartDataService;

    private StreamDataWebSocketService streamDataWebSocketService;

    public StreamDataWebSocketService() {

        ApplicationContext alt = ApplicationContextRegister.getApplicationContext();

        this.fileService = alt.getBean(FileService.class);
        this.personnelService = alt.getBean(PersonnelService.class);
        this.adminDeviceService = alt.getBean(AdminDeviceService.class);
        this.adminRecordingService = alt.getBean(AdminRecordingService.class);
        this.personnelCountService = alt.getBean(PersonnelCountService.class);

        mDataServer = new DataServer();
        webSocketPo = new WebSocketPo();
        configServer = ApplicationInitRunner.getConfigServer();

        isFirstStartDataService = ApplicationInitRunner.getIsFirstStartDataService();
    }

    @Autowired
    public void setStreamSessionManager(StreamSessionManager streamSessionManager) {
        StreamDataWebSocketService.streamSessionManager = streamSessionManager;
    }

    @OnOpen
    public void onOpen(@PathParam("device-id") String deviceID, Session session) throws IOException {

        webSocketPo = new WebSocketPo();

        this.mSession = session;

        log.info("session status： " +  this.mSession.isOpen());

        if (deviceID != null && !"".equals(deviceID) && !deviceID.trim().isEmpty()) {
            this.deviceID = deviceID;

        } else {
            mSession.close();
            return;
        }

        addOnlineCount();

        //添加
        streamSessionManager.setStreamDataWebSocketService(this.deviceID, this);

        webSocketPo.setStatus("SUCCESS");
        webSocketPo.setMessage("链接 " + this.deviceID + " 加入");

        sendMessage(JSON.toJSONString(webSocketPo));

        log.info("链接 " + this.deviceID + " 加入，当前链接数为：" + getOnlineCount());

        //首次打开连接数据服务
        if (!isFirstStartDataService) {

            mDataServer.start(10003, new DataServerConfig());

            ApplicationInitRunner.setIsFirstStartDataService(true);

            //连接
            mDataServer.onCameraConnected(new CameraConnectedEventHandler() {
                @Override
                public void onCameraConnected(String s) {
                    log.info("设备已连接 - IP: " + s);
                }
            });

            //关闭
            mDataServer.onCameraDisconnected(new CameraDisconnectedEventHandler() {

                @Override
                public void onCameraDisconnected(String s) {

                }
            });

            //抓拍
            mDataServer.onCaptureCompareDataReceived(new CaptureCompareDataReceivedEventHandler() {

                @Override
                public void onCaptureCompareDataReceived(CaptureCompareData captureCompareData) {

                    log.info("抓拍数据：" + captureCompareData.toJson());

                    if (null != captureCompareData.getPersonID()
                            && null != captureCompareData.getPersonName()
                            && !"".equals(captureCompareData.getPersonID()) && !"".equals(captureCompareData.getPersonName())) {

                        DeviceEntity deviceEntity = null;

                        if (captureCompareData.getCameraID() != null && captureCompareData.getCameraID().length() > 0) {
                            deviceEntity = adminDeviceService.findDeviceEntityByDeviceCode(captureCompareData.getCameraID());
                        }

                        PersonnelEntity personnelEntity = personnelService.getPersonnelByIcCard(captureCompareData.getPersonID());

                        filePo = new FilePo();
                        resultPo = new ResultPo();
                        faceMessagePo = new FaceMessagePo();

                        filePo.setFileHost(fileService.getIMG_ROOT());
                        filePo.setFileType(".jpg");

                        if (personnelEntity != null) {

                            FileEntity fileEntity = fileService.findFileEntityById(personnelEntity.getFileId());

                            if (fileEntity != null) {

                                filePo.setFileName(fileEntity.getFileUri());
                                faceMessagePo.setFileId(fileEntity.getId());
                            }

                            faceMessagePo.setSex(personnelEntity.getSex());
                            faceMessagePo.setTrueName(personnelEntity.getTruename());
                            faceMessagePo.setPersonnelGrade(personnelEntity.getPersonnelGrade());
                            faceMessagePo.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
                            faceMessagePo.setPersonnelCode(personnelEntity.getPersonnelCode());
                            faceMessagePo.setPersonnelType(personnelEntity.getPersonnelType());

                            if (deviceEntity != null) {
                                faceMessagePo.setDeviceType(deviceEntity.getDeviceType()); //设备类型

                                //进
                                if (deviceEntity.getDeviceType() == 0) {

                                    if (personnelEntity.getPersonnelStatus() != 0) {

                                        personnelEntity.setPersonnelStatus(0); //正常

                                        personnelCountService.lessPersonnelCouuntByGoOut();
                                        personnelCountService.addPersonnelCouuntByNormal();

                                        faceMessagePo.setPersonnelStatus(0);
                                    }

                                }

                                //出
                                if (deviceEntity.getDeviceType() == 1) {

                                    if (personnelEntity.getPersonnelStatus() != 1) {

                                        personnelEntity.setPersonnelStatus(1); //外出

                                        personnelCountService.lessPersonnelCouuntByNormal();
                                        personnelCountService.addPersonnelCouuntByGoOut();

                                        faceMessagePo.setPersonnelStatus(1);
                                    }
                                }
                            }
                        }

                        faceMessagePo.setFilePo(filePo);

                        resultPo.setStatus("FACE_RECOGNITION_SUCCESS");
                        resultPo.setData(faceMessagePo);

                        streamDataWebSocketService = streamSessionManager.getStreamDataWebSocketService(deviceID);

                        if (streamDataWebSocketService != null) {

                            streamSessionManager.getStreamDataWebSocketService(deviceID).sendMessage(JSON.toJSONString(resultPo));
                        }

                        personnelService.save(personnelEntity);

                        //添加识别日志
                        if (personnelEntity != null && captureCompareData.getCameraID() != null && captureCompareData.getEnvironmentImageData() != null) {
                             adminRecordingService.addRecording(personnelEntity.getId(), captureCompareData.getCameraID(), captureCompareData.getEnvironmentImageData());
                        }

                    }
                }
            });
        }

        if (mLock.tryLock()) {

            try {

                //Start Video Stream
                if (!configServer.getIsStreamStartByDeviceNo(deviceID)) {

                    configServer.startStreamByDeviceNo(deviceID);
                }

            } catch (Exception e) {
                log.error("startStreamByDeviceNo 失败，线程获取锁失败");
                sendMessage("startStreamByDeviceNo 执行失败");
                this.mSession.close();

            } finally {

                mLock.unlock();
            }

        } else {
            log.info("startStreamByDeviceNo 失败，线程获取锁失败");
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        log.info("来自服务端消息:" + message);

    }

    @OnClose
    public void onClose(@PathParam("device-id") String deviceID) throws IOException {

        subOnlineCount();

        if (deviceID == null || "".equals(deviceID)) {
            log.info("WEB_SOCKET_CLOSE: deviceID is NULL");
            return;
        }

        streamSessionManager.removeService(this.deviceID);

        log.info("设备：" + deviceID +", 已断开连接， 当前链接数为：" + getOnlineCount());

        if (mLock.tryLock()) {
            try {

                if (streamSessionManager.getStreamDataWebSocketService(this.deviceID) == null) {
                    configServer.stopStreamByDeviceNo(deviceID);
                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {
                mLock.unlock();
            }
        } else {
            log.error("stopStreamByDeviceNo 失败，线程获取锁失败");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

        error.printStackTrace();

        log.error("StreamDataWebSocketService Exception!");

    }

    public void sendMessage(String message) {

        mLock.lock();

        try {

            if (this.mSession.isOpen()) {

                this.mSession.getAsyncRemote().sendText(message);

                log.info("成功发送一条消息:" + message);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            mLock.unlock();
        }

    }

//    @Override
//    public void contextDestroyed(ServletContextEvent arg0) {
//
//        logger.info("The Simple Web App. Has Been Removed");
//
//        configServer.stop();
//        mDataServer.stop();
//    }
//
//    @Override
//    public void contextInitialized(ServletContextEvent arg0) {
//
//        logger.info("WebSocket:启动 The Simple Web App. Is Ready");
//
//        configServer = new ConfigServer();
//        streamDataReceiver = new StreamDataReceiver();
//
//        configServer.onStreamDataReceived(streamDataReceiver);
//
//        configServer.start(10011, new ConfigServerConfig()); //穿透端口 启动配置服务
//
//        configServer.onCameraConnected(new CameraConnectedEventHandler() {
//            @Override
//            public void onCameraConnected(String s) {
//                logger.info(" ------------- 设备已连接-配置端口:" + s);
//            }
//        });
//    }

    public static synchronized int getOnlineCount() {
        return StreamDataWebSocketService.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        StreamDataWebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        StreamDataWebSocketService.onlineCount--;
    }

    public static StreamSessionManager getStreamSessionManager() {
        return StreamDataWebSocketService.streamSessionManager;
    }

    public Session getWebSocketServiceSession() {
        return this.mSession;
    }
}
