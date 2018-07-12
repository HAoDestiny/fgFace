package net.sh.rgface.repository;

import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {



}
