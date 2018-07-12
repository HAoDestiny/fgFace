package net.sh.rgface.controller.admin;

import com.ha.facecamera.configserver.pojo.FaceFromDownload;
import com.ha.facecamera.configserver.pojo.FacePage;
import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminFaceListPo;
import net.sh.rgface.po.admin.AdminFacePo;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.serive.personnel.PersonnelService;
import net.sh.rgface.vo.admin.face.AdminBatchCopyToDeviceVo;
import net.sh.rgface.vo.admin.face.AdminBatchRemoveFaceVo;
import net.sh.rgface.vo.admin.face.AdminFaceListVo;
import net.sh.rgface.vo.admin.face.AdminFaceVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/30.
 */

@RestController
@RequestMapping(value = "/api/admin/face", method = RequestMethod.POST)
public class AdminFaceController {

    @Value("${face.recognition.imagesProtocol}")
    private String ROOT;

    @Resource
    private FaceService faceService;

    @Resource
    private PersonnelService personnelService;

    @NotAspect
    @RequestMapping(value = "/getDeviceFaceList")
    public ResultPo getDeviceFaceList(@RequestBody @Valid AdminFaceListVo adminFaceListVo, BindingResult bindingResult) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        if (adminFaceListVo.getPageCode() <= 0 || adminFaceListVo.getPageSize() <= 0) {

            throw new ControllerException("参数错误");
        }

        FacePage facePage = faceService.getFaceList(adminFaceListVo);

        if (facePage == null) {
            throw new ControllerException("设备不在线");
        }

        FaceFromDownload[] faceFromDownloads = facePage.getFaces();
        AdminFaceListPo adminFaceListPo = new AdminFaceListPo();
        List<AdminFacePo> adminFacePos = new ArrayList<>();
        ResultPo resultPo = new ResultPo();

        if (faceFromDownloads != null && faceFromDownloads.length > 0) {

            for (FaceFromDownload faceFromDownload : faceFromDownloads) {

                FilePo filePo = new FilePo();
                AdminFacePo adminFacePo = new AdminFacePo();

                adminFacePo.setFaceId(faceFromDownload.getId());
                adminFacePo.setFaceName(faceFromDownload.getName());
                adminFacePo.setFaceRole(faceFromDownload.getRole());
                adminFacePo.setFaceWiegandNo(faceFromDownload.getWiegandNo());

                PersonnelEntity personnelEntity = personnelService.getPersonnelByIcCard(faceFromDownload.getId());

                if (personnelEntity != null) {

                    FaceEntity faceEntity = faceService.findFaceEntityById(personnelEntity.getFaceId());
                    if (faceEntity != null) {

                        filePo.setFileName(faceEntity.getFaceUri());
                        filePo.setFileHost(ROOT);
                        filePo.setFileType("jpg");

                        adminFacePo.setFacePo(filePo);
                    }
                }

                adminFacePos.add(adminFacePo);
            }

            adminFaceListPo.setAdminFacePo(adminFacePos);
        }

        adminFaceListPo.setPageCode(adminFaceListVo.getPageCode());
        adminFaceListPo.setPageSize(adminFaceListVo.getPageSize());
        adminFaceListPo.setEntriesTotal(facePage.getTotal());
        adminFaceListPo.setDeviceCode(adminFaceListVo.getDeviceCode());

        resultPo.setData(adminFaceListPo);

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("获取成功");

        return resultPo;
    }

    //单个删除
    @RequestMapping(value = "/removeDeviceFace")
    public ResultPo removeDeviceFace(@RequestBody @Valid AdminFaceVo adminFaceVo, BindingResult bindingResult) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        int ret = faceService.removeFace(adminFaceVo);

        if (ret == -1) {
            throw new ControllerException("设备已离线");
        }

        if (ret == 2) {
            throw new ControllerException("删除失败");
        }

        ResultPo resultPo = new ResultPo();

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("删除成功");

        return resultPo;
    }

    //批量删除
    @RequestMapping(value = "/batchRemoveDeviceFace")
    public ResultPo batchRemoveDeviceFace(@RequestBody @Valid AdminBatchRemoveFaceVo adminBatchRemoveFaceVo,
                                      BindingResult bindingResult) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();
        List<String> faceIdList = faceService.batchRemoveFace(adminBatchRemoveFaceVo);

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("操作成功");
        resultPo.setData(faceIdList);

        return resultPo;
    }

    //批量复制
    @RequestMapping(value = "/batchCopyToDevice")
    public ResultPo batchCopyToDevice(@RequestBody @Valid AdminBatchCopyToDeviceVo adminBatchCopyToDeviceVo,
                                  BindingResult bindingResult) throws ControllerException {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();
        Map<String, Object> map = faceService.batchCopyToDevice(adminBatchCopyToDeviceVo);

        resultPo.setStatus("SUCCESS");
        resultPo.setData(map);
        resultPo.setMessage("操作成功");

        return resultPo;
    }
}
