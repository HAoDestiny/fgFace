package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/2.
 */
@Entity
@Table(name = "account", schema = "rgface", catalog = "")
public class AccountEntity {
    private int id;
    private String accountName;
    private String password;
    private String email;
    private int accountType;
    private int lastLoginTime;
    private String lastLoginIp;
    private String registerIp;
    private int registerTime;
    private int deleteTag;
    private AccountInfoEntity accountInfo; //一对一双向关联
    private Collection<DeviceEntity> devicesById;
    private Collection<PersonnelEntity> personnelById;
    private Collection<RequestLogEntity> requestLogById;

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
    @Column(name = "account_name")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "account_type")
    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    @Basic
    @Column(name = "last_login_time")
    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Basic
    @Column(name = "last_login_ip")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Basic
    @Column(name = "register_ip")
    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    @Basic
    @Column(name = "register_time")
    public int getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    @Basic
    @Column(name = "delete_tag")
    public int getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(int deleteTag) {
        this.deleteTag = deleteTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                accountType == that.accountType &&
                lastLoginTime == that.lastLoginTime &&
                registerTime == that.registerTime &&
                deleteTag == that.deleteTag &&
                Objects.equals(accountName, that.accountName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(lastLoginIp, that.lastLoginIp) &&
                Objects.equals(registerIp, that.registerIp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, accountName, password, email, accountType, lastLoginTime, lastLoginIp, registerIp, registerTime, deleteTag);
    }

    //一对一双向关联
    @OneToOne(mappedBy = "accountByAccountId")
    public AccountInfoEntity getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfoEntity accountInfo) {
        this.accountInfo = accountInfo;
    }

    @OneToMany(mappedBy = "accountByAccountId")
    public Collection<DeviceEntity> getDevicesById() {
        return devicesById;
    }

    public void setDevicesById(Collection<DeviceEntity> devicesById) {
        this.devicesById = devicesById;
    }

    @OneToMany(mappedBy = "accountByAccountId")
    public Collection<PersonnelEntity> getPersonnelById() {
        return personnelById;
    }

    public void setPersonnelById(Collection<PersonnelEntity> personnelById) {
        this.personnelById = personnelById;
    }

    @OneToMany(mappedBy = "accountEntityByAccountId")
    public Collection<RequestLogEntity> getRequestLogById() {
        return requestLogById;
    }

    public void setRequestLogById(Collection<RequestLogEntity> requestLogById) {
        this.requestLogById = requestLogById;
    }
}
