package net.sh.rgface.vo.admin.face;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Created by DESTINY on 2018/6/13.
 */
public class AdminBatchCopyToDeviceVo {

    @NotNull(message = "设备编号")
    private String deviceCode;

    @NotNull(message = "人脸编号")
    private ArrayList<String> faceIdList;

    @NotNull(message = "复制对象设备编号")
    private String toDeviceCode;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public ArrayList<String> getFaceIdList() {
        return faceIdList;
    }

    public void setFaceIdList(ArrayList<String> faceIdList) {
        this.faceIdList = faceIdList;
    }

    public String getToDeviceCode() {
        return toDeviceCode;
    }

    public void setToDeviceCode(String toDeviceCode) {
        this.toDeviceCode = toDeviceCode;
    }
}
