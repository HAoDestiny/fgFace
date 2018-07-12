package net.sh.rgface.repository;

import net.sh.rgface.entity.AccountInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfoEntity, Integer> {



}
