package net.sh.rgface.controller.personnel;

import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.FaceReviewEntity;
import net.sh.rgface.entity.FileEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.session.PersonnelSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.personnel.PersonnelInfoPo;
import net.sh.rgface.serive.base.FileService;
import net.sh.rgface.serive.base.SessionService;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.serive.personnel.PersonnelCountService;
import net.sh.rgface.serive.personnel.PersonnelService;
import net.sh.rgface.util.SessionManagement;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.personnel.PersonnelVo;
import net.sh.rgface.vo.personnel.PersonnelLoginVo;
import net.sh.rgface.vo.personnel.PersonnelUpdatePasswordVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/14.
 */

@RestController
@RequestMapping(value = "/api/personnel", method = RequestMethod.POST)
public class PersonnelController {

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    @Resource
    private FileService fileService;

    @Resource
    private FaceService faceService;

    @Resource
    private SessionService sessionService;

    @Resource
    private PersonnelService personnelService;

    @RequestMapping(value = "/login")
    public ResultPo login(@RequestBody @Valid PersonnelLoginVo personnelLoginVo, BindingResult bindingResult,
                      HttpSession session, HttpServletRequest servletRequest) throws Exception {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        if ("".equals(Tool.getIpAddress(servletRequest))) {
            throw new ControllerException("ACTION_GET_SERVICE_IP_FAILURE");
        }

        String vCode = session.getAttribute("personnel_verificationCode").toString();
        if (!vCode.equalsIgnoreCase(personnelLoginVo.getCode())) {
            throw new ControllerException("验证码错误");
        }

        PersonnelEntity personnelEntity = personnelService.login(personnelLoginVo, Tool.getIpAddress(servletRequest));

        if (personnelEntity == null || personnelEntity.getId() == 0) {
            throw new ControllerException("密码错误");
        }

        ResultPo resultPo = new ResultPo();
        PersonnelSession personnelSession = new PersonnelSession();

        resultPo.setMessage("登录成功");
        resultPo.setStatus("SUCCESS");

        personnelSession.setIcCard(personnelEntity.getIcCard());
        personnelSession.setPersonnelId(personnelEntity.getId());
        personnelSession.setPersonnelName(personnelEntity.getTruename());

        session.setAttribute("Personnel", personnelSession);

        return resultPo;
    }

    @RequestMapping(value = "/logout")
    public ResultPo logout(HttpSession httpSession) {

        httpSession.removeAttribute("Personnel");
        //httpSession.invalidate();

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("退出成功");
        resultPo.setData("/personnel/");

        return resultPo;
    }

    @RequestMapping(value = "/getPersonnelInfo")
    public ResultPo getPersonnelInfo(HttpSession httpSession) throws ControllerException {

        PersonnelSession personnelSession = (PersonnelSession) httpSession.getAttribute("Personnel");

        if (personnelSession == null) {
            throw new ControllerException("已离线,请重新登录");
        }

        FilePo filePo;
        ResultPo resultPo = new ResultPo();
        PersonnelInfoPo personnelInfoPo = new PersonnelInfoPo();

        PersonnelEntity personnelEntity = null;
        if (personnelSession.getIcCard() != null && !"".equals(personnelSession.getIcCard())) {

            personnelEntity = personnelService.getPersonnelByIcCard(personnelSession.getIcCard());
        }

        if (personnelEntity != null) {
            personnelInfoPo.setPersonnelId(personnelEntity.getId());
            personnelInfoPo.setTruename(personnelEntity.getTruename());
            personnelInfoPo.setIcCard(personnelEntity.getIcCard());
            personnelInfoPo.setMobile(personnelEntity.getMobile());
            personnelInfoPo.setPersonnelGrade(personnelEntity.getPersonnelGrade());
            personnelInfoPo.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
            personnelInfoPo.setPersonnelCode(personnelEntity.getPersonnelCode());
            personnelInfoPo.setStudyType(personnelEntity.getStudyType());
            personnelInfoPo.setPersonnelType(personnelEntity.getPersonnelType());
            personnelInfoPo.setPersonnelStart(personnelEntity.getDeleteTag());
            personnelInfoPo.setSex(personnelEntity.getSex());

            FaceReviewEntity faceReviewEntity = personnelEntity.getFaceReviewByFaceReviewId();
            if (faceReviewEntity != null) {

                personnelInfoPo.setFaceReviewStatus(faceReviewEntity.getLssuedStatus());
            }
        }

        FileEntity fileEntity = fileService.findFileEntityById(personnelEntity.getFileId());
        if (fileEntity != null) {

            filePo = new FilePo();

            filePo.setFileHost(IMG_ROOT);
            filePo.setFileType(".jpg");
            filePo.setFileName(fileEntity.getFileUri());

            personnelInfoPo.setAvatarId(fileEntity.getId());
            personnelInfoPo.setAvatar(filePo);
        }

        FaceEntity faceEntity = faceService.findFaceEntityById(personnelEntity.getFaceId());
        if (faceEntity != null) {

            filePo = new FilePo();

            filePo.setFileHost(IMG_ROOT);
            filePo.setFileType(".jpg");
            filePo.setFileName(faceEntity.getFaceUri());

            personnelInfoPo.setFaceId(faceEntity.getId());
            personnelInfoPo.setFace(filePo);
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");
        resultPo.setData(personnelInfoPo);

        return resultPo;
    }

    @RequestMapping("/updatePersonnelInfo")
    public ResultPo updatePersonnelInfo(@RequestBody PersonnelVo personnelVo, BindingResult bindingResult
            , HttpSession httpSession) throws ControllerException {

        PersonnelSession personnelSession = (PersonnelSession) httpSession.getAttribute("Personnel");

        if (personnelSession == null) {
            throw new ControllerException("已离线,请登录");
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        boolean ret = personnelService.updatePersonnelInfo(personnelVo, personnelSession);

        if (!ret) {
            throw new ControllerException("账号不存在");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setMessage("修改成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    @RequestMapping(value = "/updatePersonnelPassword")
    public ResultPo updatePersonnelPassword(@RequestBody @Valid PersonnelUpdatePasswordVo personnelUpdatePasswordVo
            , BindingResult bindingResult, HttpSession httpSession) throws ControllerException {

        PersonnelSession personnelSession = (PersonnelSession) httpSession.getAttribute("Personnel");

        if (personnelSession == null) {
            throw new ControllerException("已离线,请重新登录");
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        boolean ret = personnelService.updatePersonnelPassword(personnelSession, personnelUpdatePasswordVo);

        if (!ret) {
            throw new ControllerException("密码错误");
        }

        ResultPo resultPo = new ResultPo();

        resultPo.setMessage("修改成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }
}
