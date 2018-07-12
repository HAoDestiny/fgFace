package net.sh.rgface.repository;

import net.sh.rgface.entity.PersonnelCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface PersonnelCountRepository extends JpaRepository<PersonnelCountEntity, Integer> {

    @Transactional
    @Query(value = "select go_out_count from personnel_count", nativeQuery = true)
    int getGoOutCount();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set go_out_count = go_out_count + 1 where id = 1", nativeQuery = true)
    void addPersonnelCouuntByGoOut();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set go_out_count = go_out_count - 1 where id = 1", nativeQuery = true)
    void lessPersonnelCouuntByGoOut();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set normal_count = normal_count + 1 where id = 1", nativeQuery = true)
    void addPersonnelCouuntByNormal();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set normal_count = normal_count - 1 where id = 1", nativeQuery = true)
    void lessPersonnelCouuntByNormal();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set leave_count = leave_count + 1 where id = 1", nativeQuery = true)
    void addPersonnelCouuntByLeave();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set leave_count = leave_count - 1 where id = 1", nativeQuery = true)
    void lessPersonnelCouuntByLeave();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set other_count = other_count + 1 where id = 1", nativeQuery = true)
    void addPersonnelCouuntByOther();

    @Modifying
    @Transactional
    @Query(value = "update personnel_count set other_count = other_count - 1 where id = 1", nativeQuery = true)
    void lessPersonnelCouuntByOther();
}
