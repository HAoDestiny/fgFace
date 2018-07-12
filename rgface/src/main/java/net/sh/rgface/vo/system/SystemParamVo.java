package net.sh.rgface.vo.system;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/24.
 */
public class SystemParamVo {

    @NotNull(message = "网站名称")
    private String cmsName;

    @NotNull(message = "网站首页")
    private String homePage;

    @NotNull(message = "当前版本")
    private String version;

    @NotNull(message = "开发作者")
    private String author;

    @NotNull(message = "服务器环境")
    private String server;

    @NotNull(message = "数据库版本")
    private String dataBase;

    @NotNull(message = "最大上传限制")
    private String maxUpload;

    private String userRights;

    private String keywords;

    @NotNull(message = "版权信息")
    private String powerby;

    private String description;

    @NotNull(message = "网站备案号")
    private String record;

    public String getCmsName() {
        return cmsName;
    }

    public void setCmsName(String cmsName) {
        this.cmsName = cmsName;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getMaxUpload() {
        return maxUpload;
    }

    public void setMaxUpload(String maxUpload) {
        this.maxUpload = maxUpload;
    }

    public String getUserRights() {
        return userRights;
    }

    public void setUserRights(String userRights) {
        this.userRights = userRights;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPowerby() {
        return powerby;
    }

    public void setPowerby(String powerby) {
        this.powerby = powerby;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
