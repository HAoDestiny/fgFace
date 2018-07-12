package net.sh.rgface.po.base;

/**
 * Created by DESTINY on 2018/3/31.
 */
public class ResultPo {

    private String message;
    private String status;
    private Object data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
