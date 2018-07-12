package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/7.
 */
public class AdminAccountInfoPo {

    private int accountId;

    private String accountName;

    private String accountEmail;

    private String accountIntroduction; //简介

    private String accountContacts;

    private String accountContactInformation;

    private int accountType;

    private String accountNotes; //备注

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

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
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

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }
}
