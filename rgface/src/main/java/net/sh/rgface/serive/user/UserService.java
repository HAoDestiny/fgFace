package net.sh.rgface.serive.user;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.repository.AccountRepository;
import net.sh.rgface.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/4/12.
 */

@Service
public class UserService {

    @Resource
    private AccountRepository accountRepository;

    public AccountEntity login(UserVo userVo, String loginIP) throws ControllerException {

        AccountEntity accountEntity = accountRepository.findByAccountNameAndPassword(
                userVo.getAccount(),
                userVo.getPassword()
        );

        //密码错误
        if (accountEntity == null) {
            throw new ControllerException("密码错误");
        }

        //账号异常
        if (accountEntity.getDeleteTag() == 1) {
            throw new ControllerException("账号异常");
        }

        accountEntity.setLastLoginIp(loginIP);
        accountEntity.setLastLoginTime((int) (System.currentTimeMillis() / 1000));

        accountRepository.saveAndFlush(accountEntity);

        return accountEntity;
    }
}
