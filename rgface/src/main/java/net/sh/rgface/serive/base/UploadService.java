package net.sh.rgface.serive.base;

import com.ha.facecamera.configserver.Constants;
import com.ha.facecamera.configserver.pojo.FaceToUpload;
import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.FaceReviewEntity;
import net.sh.rgface.entity.FileEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.file.FileServicePo;
import net.sh.rgface.repository.FaceRepository;
import net.sh.rgface.repository.FaceReviewRepository;
import net.sh.rgface.repository.FileRepository;
import net.sh.rgface.repository.PersonnelRepository;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by DESTINY on 2018/5/9.
 */

@Service
public class UploadService {

    @Value("${face.recognition.filePath}")
    private String FILE_ROOT;

    @Value("${face.recognition.facePath}")
    private String FACE_ROOT;

    @Value("${face.recognition.faceRoot}")
    private String FACE_ROOT_PATH;

    @Resource
    private PersonnelRepository personnelRepository;

    @Resource
    private FaceService faceService;

    @Resource
    private FileService fileService;

    @Resource
    private FileRepository fileRepository;

    @Resource
    private FaceRepository faceRepository;

    @Resource
    private FaceReviewRepository faceReviewRepository;

    public FileServicePo uploadFile(MultipartFile file, String fileName, int type, int personnelId) {

        FileEntity fileEntity = null;
        FaceEntity faceEntity = null;
        PersonnelEntity personnelEntity = null;
        //FaceService faceService = new FaceService();
        int isSuc;
        FileUtil fileUtil = new FileUtil();
        FileServicePo fileServicePo = new FileServicePo();


        try {
            //头像
            if (type == 1) {

                Files.copy(file.getInputStream(), Paths.get(FILE_ROOT, fileName));

                fileEntity = new FileEntity();
                fileEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
                fileEntity.setFileUri(fileName);
                fileEntity.setDeleteTag(0);

                fileRepository.saveAndFlush(fileEntity);

                fileServicePo.setObject(fileEntity);
            }

            //人脸
            if (type == 2) {

                //人员信息编辑 添加人脸库 审核
                if (personnelId > 0) {
                    personnelEntity = personnelRepository.findById(personnelId);

                    if (personnelEntity != null) {

                        FaceReviewEntity faceReviewEntity = personnelEntity.getFaceReviewByFaceReviewId();

                        //添加审核 -- 已存在记录
                        if (faceReviewEntity != null) {

                            Files.copy(file.getInputStream(), Paths.get(FACE_ROOT, fileName));

                            faceReviewEntity.setFaceReviewUri(fileName);
                            faceReviewEntity.setStatus(0); //审核
                        }

                        else {

                            Files.copy(file.getInputStream(), Paths.get(FACE_ROOT, fileName));

                            faceReviewEntity = new FaceReviewEntity();
                            faceReviewEntity.setPersonnelId(personnelEntity.getId());
                            faceReviewEntity.setFaceReviewUri(fileName);
                            faceReviewEntity.setStatus(0);
                            faceReviewEntity.setLssuedStatus(0);
                            faceReviewEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
                            faceReviewEntity.setDeleteTag(0);
                        }

                        faceReviewRepository.saveAndFlush(faceReviewEntity);
                    }
                }

                //后台管理员 添加人员
                fileServicePo.setResultType(0);

                fileServicePo.setObject(faceEntity);
            }

            return fileServicePo;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
