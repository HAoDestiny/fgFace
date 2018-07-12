package net.sh.rgface.controller.admin;

import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.config.SessionListener;
import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.AccountInfoEntity;
import net.sh.rgface.entity.RequestLogEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminAccountInfoPo;
import net.sh.rgface.po.admin.AdminAccountPo;
import net.sh.rgface.po.admin.AdminLoginPo;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.admin.AdminAccountService;
import net.sh.rgface.serive.admin.AdminRequestLogService;
import net.sh.rgface.serive.base.RedisService;
import net.sh.rgface.serive.base.SessionService;
import net.sh.rgface.util.SessionManagement;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.*;
import net.sh.rgface.vo.admin.account.AddAccountVo;
import net.sh.rgface.vo.admin.account.AdminLoginVo;
import net.sh.rgface.vo.admin.account.AdminUpdateAccountInfoVo;
import net.sh.rgface.vo.admin.account.AdminUpdatePasswordVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/2.
 */

@RestController
@RequestMapping(value = "/api/admin/account", method = RequestMethod.POST)
public class AdminAccountController {

    private int ret;

    @Resource
    private RedisService redisService;

    @Resource
    private SessionService sessionService;

    @Resource
    private AdminAccountService adminAccountService;

    @Resource
    private AdminRequestLogService adminRequestLogService;

    @NotAspect
    @RequestMapping(value = "/login")
    public ResultPo login(@RequestBody @Valid AdminLoginVo adminLoginVo, BindingResult bindingResult,
                          HttpSession session, HttpServletRequest httpServletRequest) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();
        AdminSession adminSession = new AdminSession();

        String verifyCode = session.getAttribute("admin_verificationCode").toString();

        if (adminLoginVo.getVerifyCode() == null || adminLoginVo.getVerifyCode().length() == 0 || !verifyCode.equalsIgnoreCase(adminLoginVo.getVerifyCode())) {
            throw new ControllerException("验证码错误");
        }

        Map<String, Object> map = adminAccountService.login(
                adminLoginVo,
                Tool.getIpAddress(httpServletRequest)
        );

        if (map.get("status") != null) {

            if ((int) map.get("status") == 0) {

                throw new ControllerException("密码错误");
            }

            if ((int) map.get("status") == 1) {

                throw new ControllerException("账号异常");
            }

            if ((int) map.get("status") == 2) {

                throw new ControllerException("权限不足");
            }
        }


        AccountEntity accountEntity = (AccountEntity) map.get("accountEntity");

        AdminLoginPo adminLoginPo = new AdminLoginPo();

        adminLoginPo.setAccountId(accountEntity.getId());
        adminLoginPo.setAccountName(accountEntity.getAccountName());
        adminLoginPo.setAccountType(accountEntity.getAccountType());
        adminLoginPo.setEmail(accountEntity.getEmail());

        resultPo.setMessage("登录成功");
        resultPo.setStatus("SUCCESS");
        resultPo.setData(adminLoginPo);

        adminSession.setAccountName(adminLoginPo.getAccountName());
        adminSession.setAccountType(adminLoginPo.getAccountType());
        adminSession.setId(accountEntity.getId());

//        HttpSession session_old = SessionManagement.getInstance().getSession(String.valueOf(accountEntity.getId()));
//
//        sessionService.checkIsOnLine(session_old, session, String.valueOf(accountEntity.getId()));

        session.setAttribute("Admin", adminSession);

        return resultPo;
    }

    @NotAspect
    @RequestMapping(value = "/logout")
    public ResultPo logout(HttpSession httpSession) {

        httpSession.removeAttribute("Admin");
        //httpSession.invalidate();

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("退出成功");
        resultPo.setData("/admin/");

        return resultPo;
    }

    @NotAspect
    @RequestMapping(value = "/getAdminAccountMessage")
    public ResultPo getAdminAccountMessage(HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线,请前往登陆");
        }

        ResultPo resultPo = new ResultPo();
        AdminLoginPo adminLoginPo = new AdminLoginPo();

        adminLoginPo.setAccountId(adminSession.getId());
        adminLoginPo.setAccountName(adminSession.getAccountName());
        adminLoginPo.setAccountType(adminSession.getAccountType());

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");
        resultPo.setData(adminLoginPo);

        return resultPo;
    }

    //添加
    @RequestMapping(value = "/addAccount")
    public ResultPo addAccount(@RequestBody @Valid AddAccountVo addAccountVo, BindingResult bindingResult,
                               HttpServletRequest httpServletRequest, HttpSession httpSession) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();

        ret = adminAccountService.addAccount(addAccountVo, Tool.getIpAddress(httpServletRequest));

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("添加成功");

        if (ret == 0) {
            resultPo.setStatus("ERROR");
            resultPo.setMessage("添加失败");
        }

        return resultPo;
    }

    //获取列表
    @NotAspect
    @RequestMapping(value = "/getAdminAccountList")
    public BasePagingListPo getAdminAccountList(@RequestParam("type") String type, @RequestParam("accountType") String accountType
            , @RequestParam("pageCode") String pageCode, @RequestParam("pageSize") String pageSize
            , @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
            , @RequestParam("searchParam") String searchParam, HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线请重新登录");
        }

        if (type == null || accountType == null || pageCode == null
                || pageSize == null || startTime == null || endTime == null
                || type.length() == 0 || accountType.length() == 0 || pageCode.length() == 0
                || pageSize.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {

            throw new ControllerException("参数错误");
        }

        int pageCodeN = Integer.parseInt(pageCode);
        int pageSizeN = Integer.parseInt(pageSize);
        int typeN = Integer.parseInt(type);
        int startTimeN = Integer.parseInt(startTime);
        int endTimeN = Integer.parseInt(endTime);
        int accountTypeN = Integer.parseInt(accountType);

        if (accountTypeN != 0 && accountTypeN != 1) {
            throw new ControllerException("参数错误");
        }

        if (pageCodeN <= 0 || pageSizeN <= 0) {
            throw new ControllerException("参数错误");
        }

        SearchVo searchVo = new SearchVo();
        searchVo.setType(typeN);
        searchVo.setSearchParam(searchParam);
        searchVo.setStartTime(startTimeN);
        searchVo.setEndTime(endTimeN);

        BasePagingListPo listPo = new BasePagingListPo();

        Page<AccountEntity> accountEntities = null;


        //获取缓存
        if (searchParam != null && searchParam.length() > 0) {

            if (redisService.exists(searchParam)) {
                //listPo = JSON.parseObject(redisService.get(searchParam).toString(), BasePagingListPo.class);

                return (BasePagingListPo) redisService.get(searchParam);
            }

        }

        //普通用户
        if (accountTypeN == 0) {
            accountEntities = adminAccountService.getUserList(pageCodeN, pageSizeN, searchVo);
        }

        //管理员
        if (accountTypeN == 1) {
            accountEntities = adminAccountService.getAdminList(pageCodeN, pageSizeN, searchVo);
        }

        if (accountEntities != null) {

            List<AccountEntity> accountEntityList = accountEntities.getContent();

            if (accountEntityList.size() > 0) {

                List<AdminAccountPo> adminAccountListPoList = new ArrayList<>();

                for (AccountEntity accountEntity : accountEntityList) {

                    if (accountEntity.getId() == adminSession.getId()) {
                        continue;
                    }

                    AdminAccountPo adminAccountPo = new AdminAccountPo();

                    adminAccountPo.setAccountId(accountEntity.getId());
                    adminAccountPo.setAccountName(accountEntity.getAccountName());
                    adminAccountPo.setEmail(accountEntity.getEmail());
                    adminAccountPo.setAccountType(accountEntity.getAccountType());
                    adminAccountPo.setLastLoginIp(accountEntity.getLastLoginIp());
                    adminAccountPo.setLastLoginTime(accountEntity.getLastLoginTime());
                    adminAccountPo.setRegisterIp(accountEntity.getRegisterIp());
                    adminAccountPo.setRegisterTime(accountEntity.getRegisterTime());
                    adminAccountPo.setDeleteTag(accountEntity.getDeleteTag());

                    AccountInfoEntity accountInfoEntity = accountEntity.getAccountInfo();

                    if (accountInfoEntity != null) {
                        adminAccountPo.setAccountIntroduction(accountInfoEntity.getAccountIntroduction());
                        adminAccountPo.setAccountContacts(accountInfoEntity.getAccountContacts());
                        adminAccountPo.setAccountContactInformation(accountInfoEntity.getAccountContactInformation());
                        adminAccountPo.setAccountNotes(accountInfoEntity.getAccountNotes());
                    }

                    adminAccountListPoList.add(adminAccountPo);
                }

                listPo.setData(adminAccountListPoList);
                listPo.setPageTotal((int) accountEntities.getTotalElements());
            }
        }

        listPo.setStatus(200);
        listPo.setMessage("SUCCESS");

        //添加缓存
        if (searchParam != null && searchParam.length() > 0) {
            redisService.set(searchParam, listPo);
        }

        return listPo;
    }

    //获取信息
    @NotAspect
    @RequestMapping(value = "/getAdminAccountInfo")
    public ResultPo getAdminAccountInfo(HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");
        AccountEntity accountEntity = null;

        if (adminSession != null) {
            accountEntity = adminAccountService.getAccountInfoByAccountId(adminSession.getId());
        }

        ResultPo resultPo = new ResultPo();

        if (accountEntity != null) {

            AdminAccountInfoPo adminAccountInfoPo = new AdminAccountInfoPo();
            AccountInfoEntity accountInfoEntity = accountEntity.getAccountInfo();

            adminAccountInfoPo.setAccountId(accountEntity.getId());
            adminAccountInfoPo.setAccountEmail(accountEntity.getEmail());
            adminAccountInfoPo.setAccountType(accountEntity.getAccountType());
            adminAccountInfoPo.setAccountName(accountEntity.getAccountName());

            if (accountEntity != null) {
                adminAccountInfoPo.setAccountNotes(accountInfoEntity.getAccountNotes());
                adminAccountInfoPo.setAccountContacts(accountInfoEntity.getAccountContacts());
                adminAccountInfoPo.setAccountIntroduction(accountInfoEntity.getAccountIntroduction());
                adminAccountInfoPo.setAccountContactInformation(accountInfoEntity.getAccountContactInformation());
            }

            resultPo.setData(adminAccountInfoPo);
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");

        return resultPo;
    }

    //状态管理
    @RequestMapping(value = "/setAdminAccountStartUpOrDisabled")
    public ResultPo setAdminAccountStartUpOrDisabled(@RequestBody BaseVo baseVo, HttpSession httpSession) throws ControllerException {

        if (baseVo.getId() <= 0 || baseVo.getType() < 0) {
            throw new ControllerException("参数错误");
        }

        ResultPo resultPo = new ResultPo();

        boolean isSuccess = adminAccountService.setAdminAccountStartUpOrDisabled(baseVo.getId(), baseVo.getType());

        if (!isSuccess) {
            throw new ControllerException("参数错误");
        }

        resultPo.setMessage("操作成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    //权限管理
    //0限制 1提升
    @RequestMapping(value = "/adminAuthorityManager")
    public ResultPo adminAuthorityManager(@RequestBody BaseVo baseVo, HttpSession httpSession) throws ControllerException {

        if (baseVo.getId() <= 0 || baseVo.getType() < 0) {
            throw new ControllerException("参数错误");
        }

        ResultPo resultPo = new ResultPo();

        boolean isSuccess = adminAccountService.adminAuthorityManager(baseVo.getId(), baseVo.getType());

        if (!isSuccess) {
            throw new ControllerException("操作失败");
        }

        resultPo.setMessage("操作成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    //更新
    @RequestMapping(value = "/updateAdminAccountInfo")
    public ResultPo updateAdminAccountInfo(@RequestBody @Valid AdminUpdateAccountInfoVo adminUpdateAccountInfoVo
            , BindingResult bindingResult, HttpSession httpSession) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        if (adminUpdateAccountInfoVo.getAccountId() <= 0) {
            throw new ControllerException("参数错误");
        }

        boolean ret = adminAccountService.updateAdminAccountInfo(adminUpdateAccountInfoVo);

        if (!ret) {
            throw new ControllerException("用户不存在");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("修改成功");

        return resultPo;
    }

    //更改密码
    @RequestMapping(value = "/updateAdminAccountPassword")
    public ResultPo updateAdminAccountPassword(@RequestBody @Valid AdminUpdatePasswordVo adminUpdatePasswordVo
            , BindingResult bindingResult, HttpSession httpSession) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("NOT_LOGIN");
        }

        boolean ret = adminAccountService.updatePassword(adminUpdatePasswordVo, adminSession.getAccountName());

        ResultPo resultPo = new ResultPo();

        resultPo.setStatus("SUSSES");
        resultPo.setMessage("修改成功");

        if (!ret) {
            resultPo.setStatus("ERROR");
            resultPo.setMessage("密码错误");
        }

        return resultPo;
    }
}
