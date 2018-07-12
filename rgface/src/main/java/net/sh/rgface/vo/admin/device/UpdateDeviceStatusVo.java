package net.sh.rgface.vo.admin.device;

/**
 * Created by DESTINY on 2018/5/28.
 */
public class UpdateDeviceStatusVo {

    //0对比开关 1去除重复 2设置重复时间
    private int type = 0;

    //状态值 0开启 1关闭 2设置重复时间
    private int status = -1;

    private int repeatTimeInterval = 1;

    //相似度
    private int contrastLike = 10;

    private String deviceCode;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepeatTimeInterval() {
        return repeatTimeInterval;
    }

    public void setRepeatTimeInterval(int repeatTimeInterval) {
        this.repeatTimeInterval = repeatTimeInterval;
    }

    public int getContrastLike() {
        return contrastLike;
    }

    public void setContrastLike(int contrastLike) {
        this.contrastLike = contrastLike;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
