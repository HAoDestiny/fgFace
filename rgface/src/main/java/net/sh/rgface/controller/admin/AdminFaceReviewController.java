package net.sh.rgface.controller.admin;

import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.entity.FaceReviewEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminFaceReviewListPo;
import net.sh.rgface.po.admin.AdminFaceReviewPo;
import net.sh.rgface.po.admin.AdminPersonnelPo;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.serive.admin.AdminFaceReviewService;
import net.sh.rgface.vo.admin.faceReview.FaceReviewVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTINY on 2018/6/21.
 */

@RestController
@RequestMapping(value = "/api/admin/faceReview", method = RequestMethod.POST)
public class AdminFaceReviewController {

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    @Resource
    private AdminFaceReviewService adminFaceReviewService;

    //资料审核
    @RequestMapping(value = "/faceReview")
    public ResultPo faceReview(@RequestBody FaceReviewVo faceReviewVo) throws ControllerException {

        if (faceReviewVo.getType() < 0 || faceReviewVo.getFaceReviewId() < 0) {
            throw new ControllerException("参数错误");
        }

        int res = adminFaceReviewService.faceReview(faceReviewVo);

        if (res == 0) {
            throw new ControllerException("记录不存在");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("操作成功");

        return resultPo;
    }

    //人员授权
    @RequestMapping(value = "/lssuedFace")
    public ResultPo lssuedFace(@RequestBody FaceReviewVo faceReviewVo) throws ControllerException {

        if (faceReviewVo.getType() < 0 || faceReviewVo.getPersonnelId() < 0 || faceReviewVo.getPersonnelId() < 0) {
            throw new ControllerException("参数错误");
        }

        if (faceReviewVo.getStatus() != 2 && (faceReviewVo.getDeviceId() == null || (faceReviewVo.getDeviceId() != null && faceReviewVo.getDeviceId().length() == 0))) {
            throw new ControllerException("请选择操作设备");
        }

        ResultPo resultPo = new ResultPo();
        int res = adminFaceReviewService.lssuedFace(faceReviewVo);

        if (res == -1) {
            throw new ControllerException("设备不在线");
        }

        if (res == 2) {
            throw new ControllerException("授权失败,请选择清晰的人脸图片重新上传");
        }

        if (res == 3) {
            throw new ControllerException("授权失败,人脸已存在");
        }

        if (res == 4) {
            throw new ControllerException("授权失败,人脸记录不存在");
        }

        if (res == 5) {
            throw new ControllerException("人脸图片保存失败");
        }

        if (res == 6) {
            throw new ControllerException("系统异常");
        }

        if (res == 7) {
            throw new ControllerException("记录不存在");
        }

        if (res == 8) {
            throw new ControllerException("撤销授权失败");
        }

        resultPo.setMessage("操作成功");
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }

    @NotAspect
    @RequestMapping(value = "getFaceReviewList")
    public BasePagingListPo getFaceReviewList(@RequestParam("type") String type, @RequestParam("actionType") String actionType
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
        List<AdminFaceReviewListPo> adminFaceReviewListPos = null;

        int listType = 0;
        if ("faceReview".equals(actionType)) {
            listType = 1; //资料审核
        }

        if ("lssued".equals(actionType)) {
            listType = 2; //下发
        }

        Page<FaceReviewEntity> faceReviewEntityPage = adminFaceReviewService.getFaceReviewList(
                Integer.parseInt(pageCode),
                Integer.parseInt(pageSize),
                listType,
                searchVo
        );

        if (faceReviewEntityPage != null) {

            List<FaceReviewEntity> faceReviewEntityList = faceReviewEntityPage.getContent();

            if (faceReviewEntityList.size() > 0) {

                adminFaceReviewListPos = new ArrayList<>();

                for (FaceReviewEntity faceReviewEntity : faceReviewEntityList) {

                    AdminFaceReviewPo adminFaceReviewPo = new AdminFaceReviewPo();
                    AdminPersonnelPo adminPersonnelPo = new AdminPersonnelPo();
                    AdminFaceReviewListPo adminFaceReviewListPo = new AdminFaceReviewListPo();

                    adminFaceReviewPo.setId(faceReviewEntity.getId());
                    adminFaceReviewPo.setStatus(faceReviewEntity.getStatus());
                    adminFaceReviewPo.setLssuedStatus(faceReviewEntity.getLssuedStatus());
                    adminFaceReviewPo.setCreateTime(faceReviewEntity.getCreateTime());
                    adminFaceReviewPo.setDeleteTag(faceReviewEntity.getDeleteTag());

                    PersonnelEntity personnelEntity = faceReviewEntity.getPersonnelByPersonnelId();
                    if (personnelEntity != null) {

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

                    }

                    FilePo filePo = new FilePo();
                    filePo.setFileHost(IMG_ROOT);
                    filePo.setFileName(faceReviewEntity.getFaceReviewUri());
                    filePo.setFileType(".jpg");

                    adminFaceReviewListPo.setFilePo(filePo);
                    adminFaceReviewListPo.setAdminFaceReviewPo(adminFaceReviewPo);
                    adminFaceReviewListPo.setAdminPersonnelPo(adminPersonnelPo);

                    adminFaceReviewListPos.add(adminFaceReviewListPo);
                }

                listPo.setData(adminFaceReviewListPos);

            }

            listPo.setStatus(200);
            listPo.setMessage("获取成功");
            listPo.setPageTotal(faceReviewEntityPage.getTotalPages());
        }

        return listPo;
    }
}
