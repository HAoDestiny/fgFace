package net.sh.rgface.repository;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.PersonnelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    AccountEntity findByAccountName(String accountName);

    AccountEntity findByAccountNameAndPassword(String accountName, String password);

    @Modifying
    @Transactional
    @Query(value = "update account set delete_tag = ?2 where id = ?1", nativeQuery = true)
    void updateDeleteType(Integer id, Integer deleteTag);

    @Modifying
    @Transactional
    @Query(value = "update account set account_type = ?2 where id = ?1", nativeQuery = true)
    void updateAccountType(Integer id, Integer accountType);

    @Modifying
    @Transactional
    @Query(value = "update account set password = ?1 where id = ?2", nativeQuery = true)
    void updatePasswordById(String newPassword, Integer id);

    @Transactional
    @Query(value = "select count(*) from account", nativeQuery = true)
    int getAdminAccountCounts();

    @Transactional
    @Query(value = "select count(*) from account where account_type = 0", nativeQuery = true)
    int getAccountCounts();

    //普通用户
    @Transactional
    @Query(value = "select * from account where account_type = ?1", nativeQuery = true)
    Page<AccountEntity> findByAccountType(int accountType, Pageable pageable);

    @Transactional
    @Query(value = "select * from account where account_name like ?1 and account_type = 0", nativeQuery = true)
    Page<AccountEntity> searchUserAccountByAccountName(String searchParam, Pageable pageable);

    //管理员
    @Transactional
    @Query(value = "select * from account where account_type > 0", nativeQuery = true)
    Page<AccountEntity> findByAccountType(Pageable pageable);

    @Transactional
    @Query(value = "select * from account where register_time >= ?1 and register_time <= ?2", nativeQuery = true)
    Page<AccountEntity> searchAccountByCreateTime(int startTime, int endTime, Pageable pageable);

    @Transactional
    @Query(value = "select * from account where account_name like ?1 and account_type > 0", nativeQuery = true)
    Page<AccountEntity> searchAdminAccountByAccountName(String searchParam, Pageable pageable);
}
