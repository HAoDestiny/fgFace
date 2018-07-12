package net.sh.rgface.vo;

/**
 * Created by DESTINY on 2018/5/3.
 */
public class BaseVo {

    //0启用用户、限制权限、允许人脸识别、启用设备
    //1禁用用户、提升权限、限制人脸识别、禁用设备
    private int type = -1;
    private int id = 0;
    private int excelType = -1; //0 personnelExcel 1 admin

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExcelType() {
        return excelType;
    }

    public void setExcelType(int excelType) {
        this.excelType = excelType;
    }
}
