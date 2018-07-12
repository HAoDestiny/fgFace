package net.sh.rgface.po;

/**
 * Created by DESTINY on 2018/3/31.
 */
public class WebSocketPo {

    private String message;
    private String status;
    private long timestamp = System.currentTimeMillis();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
