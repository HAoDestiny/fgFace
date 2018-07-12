package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/24.
 */
@Entity
@Table(name = "system_param", schema = "rgface", catalog = "")
public class SystemParamEntity {
    private int id;
    private String cmsName;
    private String homePage;
    private String sVersion;
    private String sAuthor;
    private String sServer;
    private String sDataBase;
    private String maxUpload;
    private String userRights;
    private String keyWords;
    private String powerBy;
    private String description;
    private String record;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cms_name")
    public String getCmsName() {
        return cmsName;
    }

    public void setCmsName(String cmsName) {
        this.cmsName = cmsName;
    }

    @Basic
    @Column(name = "home_page")
    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Basic
    @Column(name = "s_version")
    public String getsVersion() {
        return sVersion;
    }

    public void setsVersion(String sVersion) {
        this.sVersion = sVersion;
    }

    @Basic
    @Column(name = "s_author")
    public String getsAuthor() {
        return sAuthor;
    }

    public void setsAuthor(String sAuthor) {
        this.sAuthor = sAuthor;
    }

    @Basic
    @Column(name = "s_server")
    public String getsServer() {
        return sServer;
    }

    public void setsServer(String sServer) {
        this.sServer = sServer;
    }

    @Basic
    @Column(name = "s_data_base")
    public String getsDataBase() {
        return sDataBase;
    }

    public void setsDataBase(String sDataBase) {
        this.sDataBase = sDataBase;
    }

    @Basic
    @Column(name = "max_upload")
    public String getMaxUpload() {
        return maxUpload;
    }

    public void setMaxUpload(String maxUpload) {
        this.maxUpload = maxUpload;
    }

    @Basic
    @Column(name = "user_rights")
    public String getUserRights() {
        return userRights;
    }

    public void setUserRights(String userRights) {
        this.userRights = userRights;
    }

    @Basic
    @Column(name = "key_words")
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Basic
    @Column(name = "power_by")
    public String getPowerBy() {
        return powerBy;
    }

    public void setPowerBy(String powerBy) {
        this.powerBy = powerBy;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "record")
    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemParamEntity that = (SystemParamEntity) o;
        return id == that.id &&
                Objects.equals(cmsName, that.cmsName) &&
                Objects.equals(homePage, that.homePage) &&
                Objects.equals(sVersion, that.sVersion) &&
                Objects.equals(sAuthor, that.sAuthor) &&
                Objects.equals(sServer, that.sServer) &&
                Objects.equals(sDataBase, that.sDataBase) &&
                Objects.equals(maxUpload, that.maxUpload) &&
                Objects.equals(userRights, that.userRights) &&
                Objects.equals(keyWords, that.keyWords) &&
                Objects.equals(powerBy, that.powerBy) &&
                Objects.equals(description, that.description) &&
                Objects.equals(record, that.record);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, cmsName, homePage, sVersion, sAuthor, sServer, sDataBase, maxUpload, userRights, keyWords, powerBy, description, record);
    }
}
