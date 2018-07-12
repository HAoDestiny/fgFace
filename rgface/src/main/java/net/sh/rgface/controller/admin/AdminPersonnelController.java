package net.sh.rgface.controller.admin;

import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.entity.*;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminAccountInfoPo;
import net.sh.rgface.po.admin.AdminPersonnelListPo;
import net.sh.rgface.po.admin.AdminPersonnelPo;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.serive.base.FileService;
import net.sh.rgface.serive.base.UploadService;
import net.sh.rgface.serive.admin.AdminPersonnelService;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.vo.BaseVo;
import net.sh.rgface.vo.personnel.PersonnelVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/9.
 */

@RestController
@RequestMapping(value = "/api/admin/personnel", method = RequestMethod.POST)
public class AdminPersonnelController {

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    @Resource
    private UploadService uploadService;

    @Resource
    private FaceService faceService;

    @Resource
    private FileService fileService;

    @Resource
    private AdminPersonnelService adminPersonnelService;

    @RequestMapping(value = "/addPersonnel")
    public ResultPo addPersonnel(@RequestBody PersonnelVo personnelVo, BindingResult bindingResult, HttpSession httpSession)
            throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线请重新登录");
        }

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        personnelVo.setPersonnelByAccountName(adminSession.getAccountName());

        int ret = adminPersonnelService.addPersonnel(personnelVo);

        ResultPo resultPo = new ResultPo();
        if (ret == 0) {

            throw new ControllerException("所属账户不存在");
        }

        else if (ret == 1) {

            throw new ControllerException("人员类型索引错误");
        }

        else if (ret == 2) {

            throw new ControllerException("人员就读类型索引错误");
        }

        else if (ret == 3) {

            throw new ControllerException("请管理员手动添加默认头像图片");
        }

        else if (ret == 4) {

            throw new ControllerException("请管理员手动添加默认人脸图片");
        }

        else if (ret == 5) {
            resultPo.setStatus("SUCCESS");
            resultPo.setMessage("添加成功");
        }

        else {
            throw new ControllerException("SERVICE_EXCEPTION");
        }

        return resultPo;
    }

    @RequestMapping(value = "/getPersonnelList")
    public BasePagingListPo getPersonnelList(@RequestParam("type") String type
            , @RequestParam("pageCode") String pageCode, @RequestParam("pageSize") String pageSize
            , @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
            , @RequestParam("searchParam") String searchParam, HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        if (adminSession == null) {
            throw new ControllerException("已离线请重新登录");
        }

        int typeN = Integer.parseInt(type);
        int startTimeN = Integer.parseInt(startTime);
        int endTimeN = Integer.parseInt(endTime);

        if (pageCode == null || pageSize == null || Integer.parseInt(pageCode) <= 0 || Integer.parseInt(pageSize) <= 0) {
            throw new ControllerException("参数错误");
        }

        if (typeN == 1 && searchParam == null && startTimeN == 0 && endTimeN == 0) {

            throw new ControllerException("请输入搜索内容");
        }

        SearchVo searchVo = new SearchVo();
        searchVo.setType(typeN);
        searchVo.setSearchParam(searchParam);
        searchVo.setStartTime(startTimeN);
        searchVo.setEndTime(endTimeN);

        BasePagingListPo listPo = new BasePagingListPo();

        Page<PersonnelEntity> personnelEntityPage = adminPersonnelService.getPersonnelList(
                Integer.parseInt(pageCode),
                Integer.parseInt(pageSize),
                searchVo
        );

        List<AdminPersonnelListPo> adminPersonnelListPos = new ArrayList<>();
        if (personnelEntityPage != null) {

            List<PersonnelEntity> personnelEntities = personnelEntityPage.getContent();

            if (personnelEntities.size() > 0) {
                for (PersonnelEntity personnelEntity : personnelEntities) {

                    Map<String, Object> map = new HashMap<>();

                    AdminAccountInfoPo adminAccountInfoPo = null;
                    AdminPersonnelPo adminPersonnelPo = new AdminPersonnelPo();
                    AdminPersonnelListPo adminPersonnelListPo = new AdminPersonnelListPo();

                    adminPersonnelPo.setPersonnelId(personnelEntity.getId());
                    adminPersonnelPo.setTruename(personnelEntity.getTruename());
                    adminPersonnelPo.setMobile(personnelEntity.getMobile());
                    adminPersonnelPo.setIcCard(personnelEntity.getIcCard());
                    adminPersonnelPo.setSex(personnelEntity.getSex());
                    adminPersonnelPo.setPassTag(personnelEntity.getPassTag());
                    adminPersonnelPo.setStudyType(personnelEntity.getStudyType());
                    adminPersonnelPo.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
                    adminPersonnelPo.setPersonnelCode(personnelEntity.getPersonnelCode());
                    adminPersonnelPo.setPersonnelGrade(personnelEntity.getPersonnelGrade());
                    adminPersonnelPo.setPersonnelStart(personnelEntity.getDeleteTag());
                    adminPersonnelPo.setPersonnelType(personnelEntity.getPersonnelType());
                    adminPersonnelPo.setCreateTime(personnelEntity.getCreateTime());

                    AccountEntity accountEntity = personnelEntity.getAccountByAccountId();
                    if (accountEntity != null) {

                        adminPersonnelPo.setPersonnelByAccountName(accountEntity.getAccountName());

                        AccountInfoEntity accountInfoEntity = accountEntity.getAccountInfo();
                        if (accountInfoEntity != null) {

                            adminAccountInfoPo = new AdminAccountInfoPo();
                            adminAccountInfoPo.setAccountId(accountInfoEntity.getAccountId());
                            adminAccountInfoPo.setAccountName(accountEntity.getAccountName());
                            adminAccountInfoPo.setAccountEmail(accountEntity.getEmail());
                            adminAccountInfoPo.setAccountType(accountEntity.getAccountType());
                            adminAccountInfoPo.setAccountContacts(accountInfoEntity.getAccountContacts());
                            adminAccountInfoPo.setAccountIntroduction(accountInfoEntity.getAccountIntroduction());
                            adminAccountInfoPo.setAccountContactInformation(accountInfoEntity.getAccountContactInformation());
                            adminAccountInfoPo.setAccountNotes(accountInfoEntity.getAccountNotes());
                        }
                    }

                    FileEntity fileEntity = fileService.findFileEntityById(personnelEntity.getFileId());
                    if (fileEntity != null) {
                        FilePo filePo = new FilePo();
                        filePo.setFileHost(IMG_ROOT);
                        filePo.setFileName(fileEntity.getFileUri());
                        filePo.setFileType(".jpg");

                        adminPersonnelPo.setFileId(fileEntity.getId());

                        map.put("fileId", fileEntity.getId());
                        map.put("file", filePo);
                    }

                    FaceEntity faceEntity = faceService.findFaceEntityById(personnelEntity.getFaceId());
                    if (faceEntity != null) {
                        FilePo filePo = new FilePo();
                        filePo.setFileHost(IMG_ROOT);
                        filePo.setFileName(faceEntity.getFaceUri());
                        filePo.setFileType(".jpg");

                        adminPersonnelPo.setFaceId(faceEntity.getId());

                        map.put("faceId", faceEntity.getId());
                        map.put("face", filePo);
                    }

                    adminPersonnelListPo.setPtoPo(map);
                    adminPersonnelListPo.setAccountInfoPo(adminAccountInfoPo);
                    adminPersonnelListPo.setPersonnelPo(adminPersonnelPo);

                    adminPersonnelListPos.add(adminPersonnelListPo);
                }
            }

            listPo.setPageTotal((int) personnelEntityPage.getTotalElements());
        }

        listPo.setMessage("获取成功");
        listPo.setStatus(200);
        listPo.setData(adminPersonnelListPos);

        return listPo;
    }

    @RequestMapping(value = "/updatePersonnel")
    public ResultPo updatePersonnel(@RequestBody PersonnelVo personnelVo, BindingResult bindingResult)
            throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        boolean ret = adminPersonnelService.updatePersonnelInfo(personnelVo);

        if (!ret) {
            throw new ControllerException("账号不存在");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setMessage("修改成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    @RequestMapping(value = "/updatePersonnelStatus")
    public ResultPo updatePersonnelStatus(@RequestBody BaseVo baseVo) throws ControllerException {

        if (baseVo.getType() < 0 || baseVo.getId() <= 0) {
            throw new ControllerException("参数错误");
        }

        boolean ret = adminPersonnelService.updatePersonnelStatusOrPassTag(baseVo, 0);

        if (!ret) {
            throw new ControllerException("参数错误");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("修改成功");

        return resultPo;
    }

    @RequestMapping(value = "/updatePersonnelFaceRecognition")
    public ResultPo updatePersonnelFaceRecognition(@RequestBody BaseVo baseVo) throws ControllerException {

        if (baseVo.getType() < 0 || baseVo.getId() <= 0) {
            throw new ControllerException("参数错误");
        }

        boolean ret = adminPersonnelService.updatePersonnelStatusOrPassTag(baseVo, 1);

        if (!ret) {
            throw new ControllerException("修改失败");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("修改成功");

        return resultPo;
    }
}
