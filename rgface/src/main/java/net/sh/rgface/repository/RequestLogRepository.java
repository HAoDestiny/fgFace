package net.sh.rgface.repository;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.RequestLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLogEntity, Integer> {

    @Transactional
    @Query(value = "select * from request_log where delete_tag >= 0", nativeQuery = true)
    Page<RequestLogEntity> findByDeleteTag(Integer deleteTag, Pageable pageable);

    @Transactional
    @Query(value = "select * from request_log order by create_time DESC limit 12", nativeQuery = true)
    List<RequestLogEntity> findByCreateTime();

    @Transactional
    @Query(value = "select * from request_log inner join account " +
            "where " +
            "(" +
            "account_name like ?1 " +
            "or action_status like ?1" +
            ") and account.id = request_log.account_id order by create_time DESC", nativeQuery = true)
    Page<RequestLogEntity> searchRequestLogByNameOrStatus(String searchParam, Pageable pageable);

    @Transactional
    @Query(value = "select * from request_log where create_time >= ?1 and create_time <= ?2 order by create_time DESC", nativeQuery = true)
    Page<RequestLogEntity> searchRequestLogByCreateTime(int startTime, int endTime, Pageable pageable);

    @Transactional
    @Query(value = "select * from request_log inner join account " +
            "where " +
            "(" +
            "account_name like ?1 " +
            "or action_status like ?1" +
            ") " +
            "and " +
            "( " +
            "request_log.create_time >= ?2 and request_log.create_time <= ?3" +
            ")" +
            "and account.id = request_log.account_id order by create_time DESC", nativeQuery = true)
    Page<RequestLogEntity> searchRequestLogByAll(String searchParam, int startTime, int endTime, Pageable pageable);
}
