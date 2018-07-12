package net.sh.rgface.serive.face;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.Constants;
import com.ha.facecamera.configserver.pojo.FacePage;
import com.ha.facecamera.configserver.pojo.FaceToUpload;
import com.ha.facecamera.configserver.pojo.ListFaceCriteria;
import net.sh.rgface.config.ApplicationInitRunner;
import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.repository.FaceRepository;
import net.sh.rgface.repository.PersonnelRepository;
import net.sh.rgface.serive.admin.AdminDeviceService;
import net.sh.rgface.serive.admin.AdminPersonnelService;
import net.sh.rgface.vo.admin.face.AdminBatchCopyToDeviceVo;
import net.sh.rgface.vo.admin.face.AdminBatchRemoveFaceVo;
import net.sh.rgface.vo.admin.face.AdminFaceListVo;
import net.sh.rgface.vo.admin.face.AdminFaceVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/17.
 */

@Service
public class FaceService {

    @Resource
    private AdminDeviceService adminDeviceService;

    @Resource
    private PersonnelRepository personnelRepository;

    @Resource
    private FaceRepository faceRepository;

    @Value("${face.recognition.faceRoot}")
    private String FACE_ROOT_PATH;

    //-1设备不存在(离线) 1添加成功 2添加人脸库失败 3人脸已存在
    public int addFace(String filePath, PersonnelEntity personnelEntity, String deviceId) {

        String errorMsg;
        String[] imgPathS = new String[]{filePath};

        FaceToUpload faceToUpload = new FaceToUpload();
        faceToUpload.setId(personnelEntity.getIcCard());
        faceToUpload.setName(personnelEntity.getTruename());
        faceToUpload.setJpgFilePath(imgPathS);
        faceToUpload.setRole(1); //角色 0普通人员 1白名单 2黑名单
        faceToUpload.setWiegandNo(0);
        faceToUpload.setExpireDate(Constants.LONGLIVE); //时限

        //添加人脸
        boolean isAddSuc = ApplicationInitRunner.getConfigServer().addFace(deviceId, faceToUpload, 5000); //设备编号
        System.out.println(faceToUpload.getJpgFilePath()[0]);

        if (!isAddSuc) {

            errorMsg = ApplicationInitRunner.getConfigServer().getLastErrorMsg(deviceId);

            System.out.println(" ----------------------------- camera Error_Msg On addFace: " + ApplicationInitRunner.getConfigServer().getLastErrorMsg(deviceId));

            if ("device not found".equals(errorMsg)) {
                return -1;
            }

            if (("wrong ack: 21").equals(errorMsg)) {
                return 3;
            }

            return 2;
        }

        return 1;
    }

    //修改人脸信息 -1设备不存在(已离线) 1修改成功 2修改失败 4记录不存在
    public int updateFaceMessage(PersonnelEntity personnelEntity, String faceUri, String deviceId, long timeOut) {

        String errorMsg;
        FaceToUpload faceToUpload = new FaceToUpload();

        faceToUpload.setId(personnelEntity.getIcCard());
        faceToUpload.setName(personnelEntity.getTruename());
        faceToUpload.setJpgFilePath(new String[]{FACE_ROOT_PATH + faceUri});
        faceToUpload.setRole(1); //角色 0普通人员 1白名单 2黑名单
        faceToUpload.setWiegandNo(0);
        faceToUpload.setExpireDate(Constants.LONGLIVE); //时限

        boolean isAddSuc = ApplicationInitRunner.getConfigServer().modifyFace(deviceId, faceToUpload, timeOut);

        if (!isAddSuc) {

            errorMsg = ApplicationInitRunner.getConfigServer().getLastErrorMsg(deviceId);

            System.out.println(" ----------------------------- camera Error_Msg On updateFaceMessage: " + errorMsg);

            if (ApplicationInitRunner.getConfigServer().getLastErrorMsg(deviceId).equals("device not found")) {
                return -1;
            }

            if (("wrong ack: 22").equals(errorMsg)) {
                return 4;
            }

            return 2;
        }

        return 1;
    }

    public FacePage getFaceList(AdminFaceListVo adminFaceListVo) {

        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        if (!configServer.getCameraOnlineState(adminFaceListVo.getDeviceCode())) {

            return null;
        }

        ListFaceCriteria listFaceCriteria = new ListFaceCriteria();
        listFaceCriteria.setGetImageData(true); //获取图片数据
        listFaceCriteria.setGetFeatureData(true); //获取人脸特征
        listFaceCriteria.setPageNo(adminFaceListVo.getPageCode()); //当前页
        listFaceCriteria.setPageSize(adminFaceListVo.getPageSize());

        FacePage facePage = configServer.listFace(adminFaceListVo.getDeviceCode(), listFaceCriteria, 5000);

        return facePage;
    }

    //-1设备已离线 1成功 2删除失败
    public int removeFace(AdminFaceVo adminFaceVo) {

        String errorMsg;
        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        if (!configServer.getCameraOnlineState(adminFaceVo.getDeviceCode())) {

            return -1;
        }

        boolean ret = configServer.deleteFace(adminFaceVo.getDeviceCode(), adminFaceVo.getFaceCode(), 5000);

        if (!ret) {

            errorMsg = ApplicationInitRunner.getConfigServer().getLastErrorMsg(adminFaceVo.getDeviceCode());

            System.out.println(" ----------------------------- camera Error_Msg On removeFace: " + errorMsg);

            if ("device not found".equals(errorMsg)) {
                return -1;
            }

            if (("wrong ack: 21").equals(errorMsg)) {
                return 3;
            }

            return 2;
        }

        return 1;
    }

    //批量移除
    public List<String> batchRemoveFace(AdminBatchRemoveFaceVo adminBatchRemoveFaceVo) throws ControllerException {

        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        if (!configServer.getCameraOnlineState(adminBatchRemoveFaceVo.getDeviceCode())) {

            throw new ControllerException("设备已离线");
        }

        boolean ret;

        List<String> faceList = null;
        ArrayList<String> faceIdList = adminBatchRemoveFaceVo.getFaceIdList();

        if (faceIdList != null && faceIdList.size() > 0) {

            faceList = new ArrayList<>();

            //占位
            faceList.add("removeBegin");

            for (String faceId : faceIdList) {

                ret = configServer.deleteFace(adminBatchRemoveFaceVo.getDeviceCode(), faceId, 5000);

                if (!ret) {
                    faceList.add(faceId);
                }
            }

        }

        return faceList;
    }

    //复制
    public Map<String, Object> batchCopyToDevice(AdminBatchCopyToDeviceVo adminBatchCopyToDeviceVo) throws ControllerException {

        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        if (!configServer.getCameraOnlineState(adminBatchCopyToDeviceVo.getToDeviceCode())) {

            throw new ControllerException("复制对象设备已离线");
        }

        int res = 0;

        List<String> errorFaceList = null;
        List<String> repeatFaceList = null;
        Map<String, Object> map = new HashMap<>();

        ArrayList<String> faceIdList = adminBatchCopyToDeviceVo.getFaceIdList();

        if (faceIdList != null && faceIdList.size() > 0) {

            errorFaceList = new ArrayList<>();
            repeatFaceList = new ArrayList<>();

            for (String faceId : adminBatchCopyToDeviceVo.getFaceIdList()) {

                PersonnelEntity personnelEntity = personnelRepository.findByIcCard(faceId);

                if (personnelEntity == null) {
                    errorFaceList.add(faceId); //操作失败
                    continue;
                }

                FaceEntity faceEntity = faceRepository.findById(personnelEntity.getFaceId());
                if (faceEntity != null) {

                    res = addFace(FACE_ROOT_PATH + faceEntity.getFaceUri(),
                            personnelEntity,
                            adminBatchCopyToDeviceVo.getToDeviceCode()
                    );

                    if (res == 0 || res == 2 ) {
                        errorFaceList.add(faceId); //操作失败
                    }

                    if (res == 3) {
                        repeatFaceList.add(faceId);
                    }

                    map.put("errorFaceList", errorFaceList);
                    map.put("repeatFaceList", repeatFaceList);
                }
            }
        }

        return map;
    }

    public FaceEntity findFaceEntityById(int id) {

        return faceRepository.findById(id);
    }

    public void saveFaceEntity(FaceEntity faceEntity) {
        faceRepository.saveAndFlush(faceEntity);
    }
}
