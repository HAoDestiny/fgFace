package net.sh.rgface.repository;

import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.SystemParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface SystemParamRepository extends JpaRepository<SystemParamEntity, Integer> {



}
