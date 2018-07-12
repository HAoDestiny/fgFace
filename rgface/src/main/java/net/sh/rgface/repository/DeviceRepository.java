package net.sh.rgface.repository;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.DeviceEntity;
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
public interface DeviceRepository extends JpaRepository<DeviceEntity, Integer> {

    DeviceEntity findByDeviceCode(String deviceCode);

    @Transactional
    @Query(value = "select * from device where delete_tag >= 0", nativeQuery = true)
    Page<DeviceEntity> findByDeleteTag(Pageable pageable);

    @Transactional
    @Query(value = "select count(*) from device", nativeQuery = true)
    int getDeviceCounts();

    @Modifying
    @Transactional
    @Query(value = "update device set delete_tag = ?2 where id = ?1", nativeQuery = true)
    void updateDeviceDeleteTag(Integer id, int deleteTag);

    @Transactional
    @Query(value = "select * from device where device_name like ?1 or device_code like ?1", nativeQuery = true)
    Page<DeviceEntity> searchDeviceByNameOrCode(String searchParam, Pageable pageable);
}
