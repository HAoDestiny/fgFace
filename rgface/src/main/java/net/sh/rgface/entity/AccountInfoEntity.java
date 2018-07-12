package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/2.
 */
@Entity
@Table(name = "account_info", schema = "rgface", catalog = "")
public class AccountInfoEntity {
    private int id;
    private int accountId;
    private String accountIntroduction;
    private String accountContactInformation;
    private String accountContacts;
    private String accountNotes;
    private int createTime;
    private AccountEntity accountByAccountId; //一对一双向关联

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
    @Column(name = "account_id")
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "account_introduction")
    public String getAccountIntroduction() {
        return accountIntroduction;
    }

    public void setAccountIntroduction(String accountIntroduction) {
        this.accountIntroduction = accountIntroduction;
    }

    @Basic
    @Column(name = "account_contact_information")
    public String getAccountContactInformation() {
        return accountContactInformation;
    }

    public void setAccountContactInformation(String accountContactInformation) {
        this.accountContactInformation = accountContactInformation;
    }

    @Basic
    @Column(name = "account_contacts")
    public String getAccountContacts() {
        return accountContacts;
    }

    public void setAccountContacts(String accountContacts) {
        this.accountContacts = accountContacts;
    }

    @Basic
    @Column(name = "account_notes")
    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }

    @Basic
    @Column(name = "create_time")
    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInfoEntity that = (AccountInfoEntity) o;
        return id == that.id &&
                accountId == that.accountId &&
                createTime == that.createTime &&
                Objects.equals(accountIntroduction, that.accountIntroduction) &&
                Objects.equals(accountContactInformation, that.accountContactInformation) &&
                Objects.equals(accountContacts, that.accountContacts) &&
                Objects.equals(accountNotes, that.accountNotes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, accountId, accountIntroduction, accountContactInformation, accountContacts, accountNotes, createTime);
    }

    //一对一双向关联
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public AccountEntity getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(AccountEntity accountByAccountId) {
        this.accountByAccountId = accountByAccountId;
    }
}
