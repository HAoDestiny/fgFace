package net.sh.rgface.serive.admin;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.AccountInfoEntity;
import net.sh.rgface.entity.RecordingEntity;
import net.sh.rgface.entity.RequestLogEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.repository.AccountInfoRepository;
import net.sh.rgface.repository.AccountRepository;
import net.sh.rgface.repository.RecordingRepository;
import net.sh.rgface.repository.RequestLogRepository;
import net.sh.rgface.serive.base.RedisService;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.admin.account.AddAccountVo;
import net.sh.rgface.vo.admin.account.AdminLoginVo;
import net.sh.rgface.vo.admin.account.AdminUpdateAccountInfoVo;
import net.sh.rgface.vo.admin.account.AdminUpdatePasswordVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by DESTINY on 2018/5/2.
 */

@Service
public class AdminAccountService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private AccountInfoRepository accountInfoRepository;

    public Map<String, Object> login(AdminLoginVo adminLoginVo, String loginIP) {

        AccountEntity accountEntity = accountRepository.findByAccountNameAndPassword(
                adminLoginVo.getAccountName(),
                adminLoginVo.getPassword()
        );

        Map<String, Object> map = new HashMap<>();

        //密码错误
        if (accountEntity == null) {

            map.put("status", 0);
            return map;
        }

        //账号异常
        if (accountEntity.getDeleteTag() == 1) {

            map.put("status", 1);
            return map;
        }

        //权限不足
        if (accountEntity.getAccountType() == 0) {

            map.put("status", 2);
            return map;
        }

        accountEntity.setLastLoginIp(loginIP);
        accountEntity.setLastLoginTime((int) (System.currentTimeMillis() / 1000));

        accountRepository.saveAndFlush(accountEntity);

        map.put("accountEntity", accountEntity);

        return map;
    }

    // 0操作失败 1操作成功
    public int addAccount(AddAccountVo addAccountVo, String ip) throws ControllerException {

        AccountEntity accountEntity = accountRepository.findByAccountName(addAccountVo.getAccountName());

        if (accountEntity != null) {
            throw new ControllerException("用户名已存在");
        }

        accountEntity = new AccountEntity();
        AccountInfoEntity accountInfoEntity = new AccountInfoEntity();

        accountEntity.setAccountName(addAccountVo.getAccountName());
        accountEntity.setPassword(addAccountVo.getAccountPasswordRePass());
        accountEntity.setEmail(addAccountVo.getAccountEmail());
        accountEntity.setRegisterIp(ip);
        accountEntity.setRegisterTime((int) (System.currentTimeMillis() / 1000));
        //accountEntity.setLastLoginIp(ip);
        accountEntity.setLastLoginTime(0);
        accountEntity.setAccountType(addAccountVo.getAccountType()); //权限

        accountRepository.saveAndFlush(accountEntity);

        accountInfoEntity.setAccountId(accountEntity.getId());
        accountInfoEntity.setAccountIntroduction(addAccountVo.getAccountIntroduction());
        accountInfoEntity.setAccountContactInformation(addAccountVo.getAccountContactInformation());
        accountInfoEntity.setAccountContacts(addAccountVo.getAccountContacts());

        if (addAccountVo.getAccountNotes() != null) {
            accountInfoEntity.setAccountNotes(addAccountVo.getAccountNotes());
        }

        accountInfoRepository.saveAndFlush(accountInfoEntity);

        return 1;
    }

    //获取列表 管理员
    public Page<AccountEntity> getAdminList(int pageCode, int pageSize, SearchVo searchVo) {

        Sort sort = new Sort(Sort.Direction.DESC, "register_time");

        Pageable pageable = PageRequest.of(pageCode - 1, pageSize, sort); //page从0开始

        Page<AccountEntity> page = null;

        //全部
        if (searchVo.getType() == 0) {

            page = accountRepository.findByAccountType(pageable);
        }

        //搜索
        if (searchVo.getType() == 1) {

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = accountRepository.searchAdminAccountByAccountName("%" + searchVo.getSearchParam() + "%", pageable);


            }
        }

        return page;
    }

    //获取列表 普通用户
    public Page<AccountEntity> getUserList(int pageCode, int pageSize, SearchVo searchVo) {

        Sort sort = new Sort(Sort.Direction.DESC, "register_time");

        Pageable pageable = PageRequest.of(pageCode - 1, pageSize, sort); //page从0开始

        Page<AccountEntity> page = null;

        //全部
        if (searchVo.getType() == 0) {
            page = accountRepository.findByAccountType(0, pageable);
        }

        //搜索
        if (searchVo.getType() == 1) {

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = accountRepository.searchUserAccountByAccountName("%" + searchVo.getSearchParam() + "%", pageable);
            }
        }

        return page;
    }

    public AccountEntity getAccountInfoByAccountId(int id) throws ControllerException {

        return getAccountEntityById(id);
    }

    //设置账号状态 0启用 1禁用
    public boolean setAdminAccountStartUpOrDisabled(int id, int type) {

        Optional<AccountEntity> accountEntity = accountRepository.findById(id);

        if (accountEntity == null) {
            return false;
        }

        if (type == 0) {
            accountRepository.updateDeleteType(id, 0);
        }

        if (type == 1) {
            accountRepository.updateDeleteType(id, 1);
        }

        return true;
    }

    //更新
    public boolean updateAdminAccountInfo(AdminUpdateAccountInfoVo adminUpdateAccountInfoVo) throws ControllerException {

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(adminUpdateAccountInfoVo.getAccountId());

        if (accountEntityOptional == null) {
            return false;
        }

        AccountEntity accountEntity = accountEntityOptional.get();

        if (accountEntity == null) {
            return false;
        }

        AccountInfoEntity accountInfoEntity = accountEntity.getAccountInfo();

        if (accountInfoEntity == null) {
            return false;
        }


        if (!adminUpdateAccountInfoVo.getAccountName().equals(accountEntity.getAccountName())) { //修改名称

            AccountEntity accountEntitys = accountRepository.findByAccountName(adminUpdateAccountInfoVo.getAccountName());
            if (accountEntitys != null) {
                throw new ControllerException("用户名已存在");
            }
        }

        accountEntity.setAccountName(adminUpdateAccountInfoVo.getAccountName());
        accountEntity.setEmail(adminUpdateAccountInfoVo.getAccountEmail());
        //accountEntity.setPassword(adminUpdateAccountInfoVo.getAccountPasswordRePass());
        //accountEntity.setAccountType(adminUpdateAccountInfoVo.getAccountType() + 1);

        accountRepository.saveAndFlush(accountEntity);

        accountInfoEntity.setAccountIntroduction(adminUpdateAccountInfoVo.getAccountIntroduction());
        accountInfoEntity.setAccountContacts(adminUpdateAccountInfoVo.getAccountContacts());
        accountInfoEntity.setAccountContactInformation(adminUpdateAccountInfoVo.getAccountContactInformation());
        accountInfoEntity.setAccountNotes(adminUpdateAccountInfoVo.getAccountNotes());

        accountInfoRepository.saveAndFlush(accountInfoEntity);

        return true;
    }

    //权限
    public boolean adminAuthorityManager(int id, int type) {

        Optional<AccountEntity> accountEntity = accountRepository.findById(id);

        if (accountEntity == null) {
            return false;
        }

        //限制
        if (type == 0) {
            accountRepository.updateAccountType(id, 1);
        }

        //提升
        if (type == 1) {
            accountRepository.updateAccountType(id, 2);
        }

        return true;
    }

    //更新密码
    public boolean updatePassword(AdminUpdatePasswordVo adminUpdatePasswordVo, String accountName) {

        AccountEntity accountEntity = accountRepository.findByAccountNameAndPassword(
                accountName,
                adminUpdatePasswordVo.getOldPassword()
        );

        if (accountEntity == null) {
            return false;
        }

        accountRepository.updatePasswordById(adminUpdatePasswordVo.getNewPassword(), accountEntity.getId());

        return true;
    }

    private AccountEntity getAccountEntityById(int id) throws ControllerException {

        if (id <= 0) {
            throw new ControllerException("参数错误");
        }

        return accountRepository.findById(id).get();
    }
}
