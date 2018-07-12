package net.sh.rgface.po.file;

/**
 * Created by DESTINY on 2018/4/19.
 */
public class FaceMessagePo {

    private int sex;
    private int fileId;
    private String trueName; //姓名
    private String personnelClassCode; //班级
    private String personnelCode; //学号
    private String personnelGrade; //年级
    private int personnelType; //类型
    private int personnelStatus = -1; //状态
    private int deviceType;

    private FilePo filePo;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPersonnelClassCode() {
        return personnelClassCode;
    }

    public void setPersonnelClassCode(String personnelClassCode) {
        this.personnelClassCode = personnelClassCode;
    }

    public String getPersonnelCode() {
        return personnelCode;
    }

    public void setPersonnelCode(String personnelCode) {
        this.personnelCode = personnelCode;
    }

    public String getPersonnelGrade() {
        return personnelGrade;
    }

    public void setPersonnelGrade(String personnelGrade) {
        this.personnelGrade = personnelGrade;
    }

    public int getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(int personnelType) {
        this.personnelType = personnelType;
    }

    public int getPersonnelStatus() {
        return personnelStatus;
    }

    public void setPersonnelStatus(int personnelStatus) {
        this.personnelStatus = personnelStatus;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public FilePo getFilePo() {
        return filePo;
    }

    public void setFilePo(FilePo filePo) {
        this.filePo = filePo;
    }
}
