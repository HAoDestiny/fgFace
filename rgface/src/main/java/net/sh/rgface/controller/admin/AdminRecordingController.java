package net.sh.rgface.controller.admin;

import net.sh.rgface.entity.*;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.BasePagingListPo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.admin.AdminDevicePo;
import net.sh.rgface.po.admin.AdminPersonnelPo;
import net.sh.rgface.po.admin.AdminRecordingListPo;
import net.sh.rgface.po.admin.AdminRecordingPo;
import net.sh.rgface.serive.admin.AdminRecordingService;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.vo.PagingListVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/21.
 */

@RestController
@RequestMapping(value = "/api/admin/recording", method = RequestMethod.POST)
public class AdminRecordingController {

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    @Value("${face.recognition.environmentPath}")
    private String ENVIRONMENT_PATH;

    @Resource
    private FaceService faceService;

    @Resource
    private AdminRecordingService adminRecordingService;

    @RequestMapping(value = "/getNewestRecordingList")
    public ResultPo getNewestRecordingList(@RequestBody PagingListVo pagingListVo) throws ControllerException {

        ResultPo resultPo = new ResultPo();

        List<RecordingEntity> recordingEntityList = adminRecordingService.getNewestRecordingList(pagingListVo.getPageSize());

        List<AdminRecordingListPo> adminRecordingListPoList = new ArrayList<>();

        if (recordingEntityList != null) {

            if (recordingEntityList.size() > 0) {

                for (RecordingEntity recordingEntity : recordingEntityList) {

                    AdminRecordingListPo adminRecordingListPo = new AdminRecordingListPo();

                    AdminRecordingPo adminRecordingPo = new AdminRecordingPo();
                    adminRecordingPo.setRecordingId(recordingEntity.getId());
                    adminRecordingPo.setCreateTime(recordingEntity.getCreateTime());

                    adminRecordingListPo.setAdminRecordingPo(adminRecordingPo);


                    PersonnelEntity personnelEntity = recordingEntity.getPersonnelByPersonnelId();
                    if (personnelEntity != null) {

                        //人员信息
                        AdminPersonnelPo adminPersonnelPo = new AdminPersonnelPo();
                        adminPersonnelPo.setPersonnelId(personnelEntity.getId());
                        adminPersonnelPo.setIcCard(personnelEntity.getIcCard());
                        adminPersonnelPo.setMobile(personnelEntity.getMobile());
                        adminPersonnelPo.setTruename(personnelEntity.getTruename());
                        adminPersonnelPo.setPersonnelGrade(personnelEntity.getPersonnelGrade());
                        adminPersonnelPo.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
                        adminPersonnelPo.setPersonnelCode(personnelEntity.getPersonnelCode());
                        adminPersonnelPo.setPersonnelType(personnelEntity.getPersonnelType());
                        adminPersonnelPo.setStudyType(personnelEntity.getStudyType());
                        adminPersonnelPo.setPassTag(personnelEntity.getPassTag());

                        adminRecordingListPo.setPersonnelPo(adminPersonnelPo);

                    }

                    DeviceEntity deviceEntity = recordingEntity.getDeviceByDeviceId();
                    if (deviceEntity != null) {

                        AdminDevicePo adminDevicePo = new AdminDevicePo();
                        adminDevicePo.setDeviceId(deviceEntity.getId());
                        adminDevicePo.setDeviceName(deviceEntity.getDeviceName());
                        adminDevicePo.setDeviceCode(deviceEntity.getDeviceCode());
                        adminDevicePo.setDeviceFirmwareVersion(deviceEntity.getDeviceFirmwareVersion());

                        adminRecordingListPo.setDevicePo(adminDevicePo);

                    }

                    adminRecordingListPoList.add(adminRecordingListPo);
                }
            }
        }

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");
        resultPo.setData(adminRecordingListPoList);

        return resultPo;

    }

    @RequestMapping(value = "/getRecordingList")
    public BasePagingListPo getRecordingList(@RequestParam("type") String type
            , @RequestParam("pageCode") String pageCode, @RequestParam("pageSize") String pageSize
            , @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
            , @RequestParam("searchParam") String searchParam) throws ControllerException {

        int typeN = Integer.parseInt(type);
        int startTimeN = Integer.parseInt(startTime);
        int endTimeN = Integer.parseInt(endTime);

        if (typeN == 1 && searchParam == null && startTimeN == 0 && endTimeN == 0) {

            throw new ControllerException("请输入搜索内容");
        }

        if (pageCode == null || pageSize == null || Integer.parseInt(pageCode) <= 0 || Integer.parseInt(pageSize) <= 0) {
            throw new ControllerException("参数错误");
        }

        SearchVo searchVo = new SearchVo();
        searchVo.setType(typeN);
        searchVo.setSearchParam(searchParam);
        searchVo.setStartTime(startTimeN);
        searchVo.setEndTime(endTimeN);

        BasePagingListPo pagingListPo = new BasePagingListPo();
        Page<RecordingEntity> recordingEntityPage = adminRecordingService.getRecordingList(
                Integer.parseInt(pageCode),
                Integer.parseInt(pageSize),
                searchVo
        );


        List<AdminRecordingListPo> adminRecordingListPoList = new ArrayList<>();
        if (recordingEntityPage != null) {

            List<RecordingEntity> recordingEntityList = recordingEntityPage.getContent();
            if (recordingEntityList.size() > 0) {

                for (RecordingEntity recordingEntity : recordingEntityList) {

                    AdminRecordingListPo adminRecordingListPo = new AdminRecordingListPo();

                    AdminRecordingPo adminRecordingPo = new AdminRecordingPo();
                    adminRecordingPo.setRecordingId(recordingEntity.getId());
                    adminRecordingPo.setCreateTime(recordingEntity.getCreateTime());

                    PersonnelEntity personnelEntity = recordingEntity.getPersonnelByPersonnelId();
                    if (personnelEntity != null) {

                        //人脸
                        FaceEntity faceEntity = faceService.findFaceEntityById(personnelEntity.getFaceId());
                        if (faceEntity != null) {
                            FilePo filePo = new FilePo();
                            filePo.setFileName(faceEntity.getFaceUri());
                            filePo.setFileHost(IMG_ROOT);
                            filePo.setFileType("jpg");

                            adminRecordingListPo.setFacePo(filePo);
                        }

                        //实时环境图
                        EnvironmentImgEntity environmentImgEntity = recordingEntity.getEnvironmentImgByEnvironmentImgId();
                        if (environmentImgEntity != null) {
                            FilePo filePo = new FilePo();
                            filePo.setFileName(environmentImgEntity.getEnvironmentUri());
                            filePo.setFileHost(IMG_ROOT);
                            filePo.setFileType("jpg");

                            adminRecordingListPo.setEnvironmentImgPo(filePo);
                        }

                        //人员信息
                        AdminPersonnelPo adminPersonnelPo = new AdminPersonnelPo();
                        adminPersonnelPo.setPersonnelId(personnelEntity.getId());
                        adminPersonnelPo.setIcCard(personnelEntity.getIcCard());
                        adminPersonnelPo.setMobile(personnelEntity.getMobile());
                        adminPersonnelPo.setTruename(personnelEntity.getTruename());
                        adminPersonnelPo.setPersonnelGrade(personnelEntity.getPersonnelGrade());
                        adminPersonnelPo.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
                        adminPersonnelPo.setPersonnelCode(personnelEntity.getPersonnelCode());
                        adminPersonnelPo.setPersonnelType(personnelEntity.getPersonnelType());
                        adminPersonnelPo.setStudyType(personnelEntity.getStudyType());
                        adminPersonnelPo.setPassTag(personnelEntity.getPassTag());

                        adminRecordingListPo.setPersonnelPo(adminPersonnelPo);

                    }

                    //设备信息
                    DeviceEntity deviceEntity = recordingEntity.getDeviceByDeviceId();
                    if (deviceEntity != null) {
                        AdminDevicePo adminDevicePo = new AdminDevicePo();
                        adminDevicePo.setDeviceId(deviceEntity.getId());
                        adminDevicePo.setDeviceName(deviceEntity.getDeviceName());
                        adminDevicePo.setDeviceCode(deviceEntity.getDeviceCode());
                        adminDevicePo.setDeviceFirmwareVersion(deviceEntity.getDeviceFirmwareVersion());

                        adminRecordingListPo.setDevicePo(adminDevicePo);
                    }

                    adminRecordingListPo.setAdminRecordingPo(adminRecordingPo);

                    adminRecordingListPoList.add(adminRecordingListPo);
                }
            }

            pagingListPo.setPageTotal((int) recordingEntityPage.getTotalElements());
        }

        pagingListPo.setStatus(200);
        pagingListPo.setMessage("获取成功");
        pagingListPo.setData(adminRecordingListPoList);

        return pagingListPo;
    }


    @RequestMapping(value = "/search")
    public void search(@RequestBody SearchVo searchVo, @RequestParam("pageCode") String pageCode,
                       @RequestParam("pageSize") String pageSize) throws ControllerException {

        if (pageCode == null || pageSize == null || Integer.parseInt(pageCode) <= 0 || Integer.parseInt(pageSize) <= 0) {
            throw new ControllerException("参数错误");
        }

        //adminRecordingService.search(pageCode, pageSize, searchVo);

    }
}
