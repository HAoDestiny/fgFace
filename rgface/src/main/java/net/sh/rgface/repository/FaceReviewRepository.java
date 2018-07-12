package net.sh.rgface.repository;

import net.sh.rgface.entity.FaceReviewEntity;
import net.sh.rgface.entity.PersonnelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface FaceReviewRepository extends JpaRepository<FaceReviewEntity, Integer> {

    FaceReviewEntity findById(int faceReviewId);

    @Query(value = "select * from face_review where status = ?1", nativeQuery = true)
    Page<FaceReviewEntity> findByStatus(int status, Pageable pageable);

    @Transactional
    @Query(value = "select * from face_review where status = ?3 and create_time >= ?1 and create_time <= ?2", nativeQuery = true)
    Page<FaceReviewEntity> searchFaceReviewByCreateTime(int startTime, int endTime, int status, Pageable pageable);

    @Transactional
    @Query(value = "select * from face_review inner join personnel " +
            "where (" +
            "personnel.truename like ?1 or " +
            "personnel.ic_card like ?1 or " +
            "personnel.personnel_code like ?1" +
            ") " +
            "and face_review.status = ?2 and personnel.id = face_review.personnel_id order by face_review.create_time DESC", nativeQuery = true)
    Page<FaceReviewEntity> searchFaceReviewByTruenameOrIcCardOrCode(String searchParam, int status, Pageable pageable);

    @Transactional
    @Query(value = "select * from face_review inner join personnel " +
            "where (" +
            "personnel.truename like ?1 or " +
            "personnel.ic_card like ?1 or " +
            "personnel.personnel_code like ?1" +
            ") " +
            "and personnel.id = face_review.personnel_id " +
            "and face_review.create_time >= ?2 " +
            "and face_review.create_time <= ?3 " +
            "and face_review.status = ?4 " +
            "order by face_review.create_time DESC", nativeQuery = true)
    Page<FaceReviewEntity> searchFaceReviewBySearchAll(String searchParam, int startTime, int EndTime, int status, Pageable pageable);
}
