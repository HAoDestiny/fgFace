package net.sh.rgface.serive.faceStream;

import com.ha.facecamera.configserver.events.StreamDataReceivedEventHandler;
import net.sh.rgface.util.StreamSessionManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StreamDataReceiver implements StreamDataReceivedEventHandler {

    private static Lock mLock = new ReentrantLock(true);

    @Override
    public void onStreamDataReceived(String deviceID, byte[] h264) {

       // System.out.println("onStreamDataReceived init------------------------");

        StreamSessionManager streamSessionManager = StreamDataWebSocketService.getStreamSessionManager();

        if (streamSessionManager != null) {

            StreamDataWebSocketService streamDataWebSocketService = streamSessionManager.getStreamDataWebSocketService(deviceID);

            if (streamDataWebSocketService != null && streamDataWebSocketService.mSession.isOpen()) {

                mLock.lock();

                try {

                    streamDataWebSocketService.mSession.getBasicRemote().sendBinary(ByteBuffer.wrap(h264));
                    //System.out.println(ByteBuffer.wrap(h264)+"");

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    mLock.unlock();
                }
            }
        }

//		for(StreamServerEndpoint sse : StreamServerEndpoint.webSocketSet) {
//			if(sse.deviceID.equals(deviceID)) {
//				try {
//					sse.session.getBasicRemote().sendBinary(ByteBuffer.wrap(h264));
//				} catch (IOException e) {
//
//					e.printStackTrace();
//				}
//			}
//		}
    }

}
