package net.sh.rgface.util;

import net.sh.rgface.serive.faceStream.StreamDataWebSocketService;
import org.apache.mina.util.CopyOnWriteMap;
import org.springframework.stereotype.Component;

/**
 * Created by DESTINY on 2018/3/30.
 */
@Component
public class StreamSessionManager {

    private static CopyOnWriteMap<Object, StreamDataWebSocketService> copyOnWriteMap = new CopyOnWriteMap<>();

    public StreamDataWebSocketService getStreamDataWebSocketService(String id) {
        return copyOnWriteMap.get(id);
    }

    public void setStreamDataWebSocketService(String key, StreamDataWebSocketService streamDataWebSocketService) {
        copyOnWriteMap.put(key, streamDataWebSocketService);
    }

    public boolean isOnline(String id) {
       return copyOnWriteMap.containsKey(id);
    }

    public void removeService(String id) {
        copyOnWriteMap.remove(id);
    }
}
