package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/23.
 */
@Entity
@Table(name = "request_log", schema = "rgface", catalog = "")
public class RequestLogEntity {
    private int id;
    private String ip;
    private String url;
    private String type;
    private String method;
    private String classMethod;
    private Integer statusCode;
    private Integer actionStatus;
    private Integer startTime;
    private Integer endTime;
    private Integer createTime;
    private Integer deleteTag;
    private Integer accountId;
    private AccountEntity accountEntityByAccountId;

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
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "method")
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Basic
    @Column(name = "class_method")
    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    @Basic
    @Column(name = "status_code")
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Basic
    @Column(name = "start_time")
    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "action_status")
    public Integer getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Basic
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "end_time")
    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "create_time")
    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "delete_tag")
    public Integer getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(Integer deleteTag) {
        this.deleteTag = deleteTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLogEntity that = (RequestLogEntity) o;
        return id == that.id &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(url, that.url) &&
                Objects.equals(type, that.type) &&
                Objects.equals(method, that.method) &&
                Objects.equals(classMethod, that.classMethod) &&
                Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(actionStatus, that.actionStatus) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(deleteTag, that.deleteTag);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ip, url, type, method, classMethod, statusCode, actionStatus, startTime, endTime, createTime, deleteTag);
    }

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public AccountEntity getAccountEntityByAccountId() {
        return accountEntityByAccountId;
    }

    public void setAccountEntityByAccountId(AccountEntity accountEntityByAccountId) {
        this.accountEntityByAccountId = accountEntityByAccountId;
    }
}
