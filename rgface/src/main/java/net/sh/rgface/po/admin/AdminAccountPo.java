package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/3.
 */
public class AdminAccountPo {

    private int accountId;
    private String accountName;
    private String email;
    private String registerIp;
    private String lastLoginIp;

    private String accountIntroduction; //简介
    private String accountContacts; //联系人
    private String accountContactInformation; //联系方式
    private String accountNotes; //备注

    private int accountType;
    private int registerTime;
    private int lastLoginTime;
    private int deleteTag;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(int deleteTag) {
        this.deleteTag = deleteTag;
    }

    public String getAccountIntroduction() {
        return accountIntroduction;
    }

    public void setAccountIntroduction(String accountIntroduction) {
        this.accountIntroduction = accountIntroduction;
    }

    public String getAccountContacts() {
        return accountContacts;
    }

    public void setAccountContacts(String accountContacts) {
        this.accountContacts = accountContacts;
    }

    public String getAccountContactInformation() {
        return accountContactInformation;
    }

    public void setAccountContactInformation(String accountContactInformation) {
        this.accountContactInformation = accountContactInformation;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }
}
