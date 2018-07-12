package net.sh.rgface.repository;

import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.RecordingEntity;
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
public interface PersonnelRepository extends JpaRepository<PersonnelEntity, Integer> {

    PersonnelEntity findById(int personnelId);

    PersonnelEntity findByIcCardAndPassword(String icCard, String password);

    PersonnelEntity findByIcCardAndPasswordAndDeleteTag(String icCard, String password, Integer deleteTag);

    PersonnelEntity findByIcCard(String icCard);

    @Transactional
    @Query(value = "select * from personnel", nativeQuery = true)
    Page<PersonnelEntity> findByDeleteTag(Pageable pageable);

    @Transactional
    @Query(value = "select * from personnel where account_id = ?1", nativeQuery = true)
    Page<PersonnelEntity> findByAccountId(Integer accountId, Pageable pageable);

    @Transactional
    @Query(value = "select * from personnel where personnel_type = ?1", nativeQuery = true)
    Page<PersonnelEntity> findByPersonnelType(Integer personnelType, Pageable pageable);

    @Transactional
    @Query(value = "select count(*) from personnel", nativeQuery = true)
    int getPersonnelCounts();

    @Modifying
    @Transactional
    @Query(value = "update personnel set delete_tag = ?2 where id = ?1", nativeQuery = true)
    void updatePersonnelDeleteTag(Integer personnelId, Integer deleteTag);

    @Modifying
    @Transactional
    @Query(value = "update personnel set pass_tag = ?2 where id = ?1", nativeQuery = true)
    void updatePersonnelPassTag(Integer personnelId, Integer passTag);

    @Modifying
    @Transactional
    @Query(value = "update personnel set password = ?2 where ic_card = ?1", nativeQuery = true)
    void updatePersonnelPasswordByIcCard(String icCard, String password);

    @Transactional
    @Query(value = "select * from personnel where create_time >= ?1 and create_time <= ?2", nativeQuery = true)
    Page<PersonnelEntity> searchPersonnelByCreateTime(int startTime, int endTime, Pageable pageable);

    @Transactional
    @Query(value = "select * FROM personnel " +
            "where " +
            "truename LIKE ?1 " +
            "or " +
            "ic_card LIKE ?1 " +
            "or " +
            "personnel_code LIKE ?1", nativeQuery = true)
    Page<PersonnelEntity> searchPersonnelByTruenameOrIcCardOrCode(String searchParam, Pageable pageable);

    @Transactional
    @Query(value = "select * from personnel " +
            "where" +
            " (truename like ?1 " +
            "or " +
            "ic_card like ?1 " +
            "or " +
            "personnel_code like ?1) " +
            "and " +
            "create_time >= ?2 and create_time <= ?3 ", nativeQuery = true)
    Page<PersonnelEntity> searchPersonnelBySearchAll(String searchParam, int startTime, int EndTime, Pageable pageable);
}
