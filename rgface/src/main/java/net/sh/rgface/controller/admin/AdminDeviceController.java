package net.sh.rgface.controller.admin;

import com.ha.facecamera.configserver.ConfigServer;
import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.config.ApplicationInitRunner;
import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.AccountInfoEntity;
import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.admin.AdminAccountInfoPo;
import net.sh.rgface.po.admin.AdminDevicePo;
import net.sh.rgface.serive.admin.AdminAccountService;
import net.sh.rgface.serive.admin.AdminDeviceService;
import net.sh.rgface.vo.BaseVo;
import net.sh.rgface.vo.admin.device.DeviceVo;
import net.sh.rgface.vo.admin.device.UpdateDeviceStatusVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/21.
 */

@RestController
@RequestMapping(value = "/api/admin/device", method = RequestMethod.POST)
public class AdminDeviceController {

    @Resource
    private AdminDeviceService adminDeviceService;

    @Resource
    private AdminAccountService adminAccountService;

    @RequestMapping(value = "/addDevice")
    public ResultPo addDevice(@RequestBody @Valid DeviceVo deviceVo, BindingResult bindingResult
            , HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线,请前往登录");
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        if (deviceVo.getConfigServerPort() <= 0 || deviceVo.getConfigServerPort() <= 0) {
            throw new ControllerException("请输入正确的端口号");
        }

        int ret = adminDeviceService.addDevice(deviceVo, adminSession);

        ResultPo resultPo = new ResultPo();

        if (ret == 1) {
            throw new ControllerException("设备编号已存在");
        }

        if (ret == 2) {
            throw new ControllerException("账号不存在");
        }

        if (ret == 3) {
            throw new ControllerException("添加失败");
        }

        if (ret == 5) {
            throw new ControllerException("参数错误");
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("添加成功");

        return resultPo;
    }

    //获取列表
    @NotAspect
    @RequestMapping(value = "/getDeviceList")
    public BasePagingListPo getDeviceList(@RequestParam("type") String type
            , @RequestParam("pageCode") String pageCode, @RequestParam("pageSize") String pageSize
            , @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
            , @RequestParam("searchParam") String searchParam, HttpSession httpSession)
            throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线请重新登录");
        }

        if (type == null || pageCode == null
                || pageSize == null || startTime == null || endTime == null
                || type.length() == 0 || pageCode.length() == 0
                || pageSize.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {

            throw new ControllerException("参数错误");
        }

        int pageCodeN = Integer.parseInt(pageCode);
        int pageSizeN = Integer.parseInt(pageSize);
        int typeN = Integer.parseInt(type);
        int startTimeN = Integer.parseInt(startTime);
        int endTimeN = Integer.parseInt(endTime);

        if (pageCodeN <= 0 || pageSizeN <= 0) {
            throw new ControllerException("参数错误");
        }

        SearchVo searchVo = new SearchVo();
        searchVo.setType(typeN);
        searchVo.setSearchParam(searchParam);
        searchVo.setStartTime(startTimeN);
        searchVo.setEndTime(endTimeN);

        BasePagingListPo pagingListPo = new BasePagingListPo();

        Page<DeviceEntity> deviceEntityPage = adminDeviceService.getDeviceList(pageCodeN, pageSizeN, searchVo);

        ConfigServer configServer = null;
        List<AdminDevicePo> deviceListPoList = null;

        if (deviceEntityPage != null) {

            List<DeviceEntity> deviceEntities = deviceEntityPage.getContent();
            deviceListPoList = new ArrayList<>();

            if (deviceEntities.size() > 0) {

                for (DeviceEntity deviceEntity : deviceEntities) {

                    AdminDevicePo adminDevicePo = new AdminDevicePo();

                    adminDevicePo.setDeviceId(deviceEntity.getId());
                    adminDevicePo.setDeviceName(deviceEntity.getDeviceName());
                    adminDevicePo.setDeviceCode(deviceEntity.getDeviceCode());
                    adminDevicePo.setDataServerIp(deviceEntity.getDataServerIp());
                    adminDevicePo.setDataServerPort(deviceEntity.getDataServerPort());
                    adminDevicePo.setConfigServerIp(deviceEntity.getConfigServerIp());
                    adminDevicePo.setConfigServerPort(deviceEntity.getConfigServerPort());
                    adminDevicePo.setDeviceType(deviceEntity.getDeviceType());

                    adminDevicePo.setDeviceFirmwareVersion(deviceEntity.getDeviceFirmwareVersion());

                    adminDevicePo.setDeviceNote(deviceEntity.getDeviceNote());
                    adminDevicePo.setCreateTime(deviceEntity.getCreateTime());
                    adminDevicePo.setDeleteTag(deviceEntity.getDeleteTag());
                    adminDevicePo.setContrastStatus(deviceEntity.getContrastStatus());
                    adminDevicePo.setContrastLike(deviceEntity.getContrastLike());
                    adminDevicePo.setRepeatStatus(deviceEntity.getRepeatStatus());
                    adminDevicePo.setRepeatTimeInterval(deviceEntity.getRepeatTimeInterval());

                    configServer = ApplicationInitRunner.getConfigServer();

                    if (configServer != null && configServer.getCameraOnlineState(deviceEntity.getDeviceCode())) {

                        adminDevicePo.setDeviceStatus(1);

                    }

                    AccountEntity accountEntity = adminAccountService.getAccountInfoByAccountId(deviceEntity.getAccountId());

                    if (accountEntity != null) {
                        AdminAccountInfoPo adminAccountInfoPo = new AdminAccountInfoPo();

                        AccountInfoEntity accountInfoEntity = accountEntity.getAccountInfo();

                        adminAccountInfoPo.setAccountId(accountEntity.getId());
                        adminAccountInfoPo.setAccountName(accountEntity.getAccountName());
                        adminAccountInfoPo.setAccountContacts(accountInfoEntity.getAccountContacts());
                        adminAccountInfoPo.setAccountContactInformation(accountInfoEntity.getAccountContactInformation());
                        adminAccountInfoPo.setAccountType(accountEntity.getAccountType());
                        adminAccountInfoPo.setAccountIntroduction(accountInfoEntity.getAccountIntroduction());

                        adminDevicePo.setAccountInfo(adminAccountInfoPo);
                    }

                    deviceListPoList.add(adminDevicePo);
                }

            }

            pagingListPo.setPageTotal((int) deviceEntityPage.getTotalElements());
        }

        pagingListPo.setStatus(200);
        pagingListPo.setMessage("获取成功");
        pagingListPo.setData(deviceListPoList);

        return pagingListPo;
    }

    //更改设备状态
    @RequestMapping(value = "/updateDeviceStatus")
    public ResultPo updateDevice(@RequestBody BaseVo baseVo) throws ControllerException {

        if (baseVo.getType() < 0 || baseVo.getId() <= 0) {
            throw new ControllerException("参数错误");
        }

        boolean ret = adminDeviceService.updateDeviceStatus(baseVo);

        if (!ret) {
            throw new ControllerException("设备不存在");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setMessage("修改成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    //更新设备基本信息
    @RequestMapping(value = "/updateDevice")
    public ResultPo updateDevice(@RequestBody @Valid DeviceVo deviceVo, BindingResult bindingResult) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        int ret = adminDeviceService.updateDevice(deviceVo);

        if (ret == 0) {
            throw new ControllerException("设备不存或者已离线");
        }

        if (ret == 1) {
            throw new ControllerException("参数错误");
        }

        ResultPo resultPo = new ResultPo();

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("修改成功");

        return resultPo;
    }

    //更新设备参数
    @RequestMapping(value = "/updateDeviceParam")
    public ResultPo updateDeviceParam(@RequestBody UpdateDeviceStatusVo updateDeviceStatusVo) throws ControllerException {

        if (updateDeviceStatusVo.getDeviceCode() == null
                || "".equals(updateDeviceStatusVo.getDeviceCode())
                || (updateDeviceStatusVo.getType() != 1 //去除重复
                && updateDeviceStatusVo.getType() != 0 //对比开关
                && updateDeviceStatusVo.getType() != 2 //设置重复时间
                && updateDeviceStatusVo.getType() != 3) //设置相似度
                || updateDeviceStatusVo.getStatus() == -1) {

            throw new ControllerException("参数错误");
        }

        if (updateDeviceStatusVo.getType() == 2 && (updateDeviceStatusVo.getRepeatTimeInterval() < 1 || updateDeviceStatusVo.getRepeatTimeInterval() > 255)) {
            throw new ControllerException("参数错误");
        }

        if (updateDeviceStatusVo.getType() == 3 && (updateDeviceStatusVo.getContrastLike() < 10 || updateDeviceStatusVo.getContrastLike() > 100)) {
            throw new ControllerException("参数错误");
        }

        int res = adminDeviceService.updateDeviceStatus(updateDeviceStatusVo);

        if (res == 0) {
            throw new ControllerException("设备不存在");
        }

        if (res == 1) {
            throw new ControllerException("设备已离线");
        }

        if (res == 2) {
            throw new ControllerException("设备去除重复功能未开启");
        }

        if (res == 3) {
            throw new ControllerException("设备对比开关功能未开启");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("修改成功");

        return resultPo;
    }
}
