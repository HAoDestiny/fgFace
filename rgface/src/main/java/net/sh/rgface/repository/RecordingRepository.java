package net.sh.rgface.repository;

import net.sh.rgface.entity.RecordingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface RecordingRepository extends JpaRepository<RecordingEntity, Integer> {

    @Transactional
    @Query(value = "select * from recording", nativeQuery = true)
    Page<RecordingEntity> findAll(Integer deleteTag, Pageable pageable);

    @Transactional
    @Query(value = "select * from recording order by create_time DESC limit ?1", nativeQuery = true)
    List<RecordingEntity> getNewestRecordingByTime(Integer recordingNum);

    @Transactional
    @Query(value = "select count(*) from recording where delete_tag = 0", nativeQuery = true)
    int getRecordingCounts();

    @Transactional
    @Query(value = "select * from recording where create_time >= ?1 and create_time <= ?2 order by create_time DESC", nativeQuery = true)
    Page<RecordingEntity> searchRecordingByCreateTime(int startTime, int endTime, Pageable pageable);

    @Transactional
    @Query(value = "select * from recording inner join personnel " +
            "where " +
            "(" +
            " personnel.truename like ?1 or" +
            " personnel.ic_card like ?1 or" +
            " personnel.personnel_code like ?1" +
            ")" +
            " and personnel.id = recording.personnel_id " +
            "order by recording.create_time DESC", nativeQuery = true)
    Page<RecordingEntity> searchRecordingByTruenameOrIcCardOrCode(String searchParam, Pageable pageable);

    @Transactional
    @Query(value = "select * from recording inner join personnel " +
            "where" +
            " (" +
            "personnel.truename like ?1 or " +
            "personnel.ic_card like ?1 or " +
            "personnel.personnel_code like ?1" +
            ") " +
            "and recording.create_time >= ?2 " +
            "and recording.create_time <= ?3 " +
            "and personnel.id = recording.personnel_id " +
            "order by recording.create_time DESC", nativeQuery = true)
    Page<RecordingEntity> searchRecordingBySearchAll(String searchParam, int startTime, int EndTime, Pageable pageable);
}
