package net.sh.rgface.repository;

import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface FaceRepository extends JpaRepository<FaceEntity, Integer> {

    FaceEntity findById(int FaceId);

    FaceEntity findByFaceUri(String uri);

}
