package net.sh.rgface.controller.admin;

import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.entity.SystemParamEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.admin.SystemMessagePo;
import net.sh.rgface.po.admin.SystemPo;
import net.sh.rgface.po.admin.SystemTotalPo;
import net.sh.rgface.serive.admin.AdminSystemService;
import net.sh.rgface.vo.system.SystemParamVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/5.
 */

@RestController
@RequestMapping(value = "/api/admin/system", method = RequestMethod.POST)
public class AdminSystemController {

    @Resource
    private AdminSystemService adminSystemService;

    @NotAspect
    @RequestMapping(value = "/getSystemParams")
    public ResultPo getSystemParams(HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("尚未登录");
        }

        SystemMessagePo systemMessagePo = new SystemMessagePo();
        ResultPo resultPo = new ResultPo();
        SystemParamEntity systemParamEntity = adminSystemService.getSystemParam();

        if (systemParamEntity != null) {

            systemMessagePo.setCms(systemParamEntity.getCmsName());
            systemMessagePo.setHomePage(systemParamEntity.getHomePage());
            systemMessagePo.setAuthor(systemParamEntity.getsAuthor());
            systemMessagePo.setVersion(systemParamEntity.getsVersion());
            systemMessagePo.setServer(systemParamEntity.getsServer());
            systemMessagePo.setDataBase(systemParamEntity.getsDataBase());
            systemMessagePo.setMaxUpload(systemParamEntity.getMaxUpload());
            systemMessagePo.setPowerby(systemParamEntity.getPowerBy());
            systemMessagePo.setDescription(systemParamEntity.getDescription());
            systemMessagePo.setRecord(systemParamEntity.getRecord());

            resultPo.setData(systemMessagePo);
        }

        if (adminSession.getAccountType() == 2) {
            systemMessagePo.setAccountType("超级管理员");
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");
        resultPo.setData(systemMessagePo);

        return resultPo;
    }

    @NotAspect
    @RequestMapping(value = "/getSystemTotalOrMsg")
    public ResultPo getSystemTotalOrMsg(HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("尚未登录");
        }

        ResultPo resultPo = new ResultPo();
        SystemPo systemPo = new SystemPo();
        Map<String, Integer> systemServiceCount = adminSystemService.getSystemTotal();
        Map<String, String> systemServiceMessage = adminSystemService.getSystemMessage();

        if (systemServiceCount != null) {
            SystemTotalPo systemTotalPo = new SystemTotalPo();
            systemTotalPo.setAdminAccountCount(systemServiceCount.get("adminAccountCount"));
            systemTotalPo.setPersonnelCount(systemServiceCount.get("personnelCount"));
            systemTotalPo.setUserCount(systemServiceCount.get("userCount"));
            systemTotalPo.setDeviceCount(systemServiceCount.get("deviceCount"));
            systemTotalPo.setRecordingCount(systemServiceCount.get("recordingCount"));

            systemPo.setSystemTotal(systemTotalPo);
        }

        if (systemServiceMessage != null) {
            SystemMessagePo systemMessagePo = new SystemMessagePo();
            systemMessagePo.setCms(systemServiceMessage.get("cms"));
            systemMessagePo.setVersion(systemServiceMessage.get("version"));
            systemMessagePo.setAuthor(systemServiceMessage.get("author"));
            systemMessagePo.setHomePage(systemServiceMessage.get("homePage"));
            systemMessagePo.setServer(systemServiceMessage.get("server"));
            systemMessagePo.setDataBase(systemServiceMessage.get("dataBase"));
            systemMessagePo.setMaxUpload(systemServiceMessage.get("maxUpload"));

            if (adminSession.getAccountType() == 2) {
                systemMessagePo.setAccountType("超级管理员");
            }

            systemPo.setSystemMessage(systemMessagePo);
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");
        resultPo.setData(systemPo);

        return resultPo;
    }

    @RequestMapping(value = "/uploadSystemParam")
    public ResultPo uploadSystemParam(@RequestBody @Valid SystemParamVo systemParamVo, BindingResult bindingResult
            , HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();

        SystemParamEntity systemParamEntity = adminSystemService.setSystemMessage(systemParamVo);

        if (null == systemParamEntity) {
            throw new ControllerException("操作失败");
        }

        SystemMessagePo systemMessagePo = new SystemMessagePo();
        systemMessagePo.setCms(systemParamEntity.getCmsName());
        systemMessagePo.setVersion(systemParamEntity.getsVersion());
        systemMessagePo.setAuthor(systemParamEntity.getsAuthor());
        systemMessagePo.setHomePage(systemParamEntity.getHomePage());
        systemMessagePo.setServer(systemParamEntity.getsServer());
        systemMessagePo.setDataBase(systemParamEntity.getsDataBase());
        systemMessagePo.setMaxUpload(systemParamEntity.getMaxUpload());
        systemMessagePo.setRecord(systemParamEntity.getRecord());
        systemMessagePo.setPowerby(systemParamEntity.getPowerBy());
        systemMessagePo.setDescription(systemParamEntity.getDescription());
        systemMessagePo.setKeywords(systemParamEntity.getKeyWords());

        if (adminSession.getAccountType() == 2) {
            systemMessagePo.setAccountType("超级管理员");
        }

        resultPo.setMessage("操作成功");
        resultPo.setStatus("SUCCESS");
        resultPo.setData(systemMessagePo);

        return resultPo;
    }

}
