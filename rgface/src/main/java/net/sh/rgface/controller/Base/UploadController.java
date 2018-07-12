package net.sh.rgface.controller.Base;

import net.sh.rgface.entity.*;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.po.base.UploadFilePo;
import net.sh.rgface.po.file.FilePo;
import net.sh.rgface.po.file.FileServicePo;
import net.sh.rgface.serive.base.UploadService;
import net.sh.rgface.util.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/5/11.
 */

@RestController
@RequestMapping(value = "/api/upload")
public class UploadController {

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    @Resource
    private UploadService uploadService;

    @RequestMapping("/uploadPersonnelFile")
    public ResultPo uploadPersonnelAvatar(@RequestParam("file") MultipartFile file
            , @RequestParam("type") String type, @RequestParam("personnelId") String personnelId) throws ControllerException {

        if (file.isEmpty()) {
            throw new ControllerException("请选择上传图片");
        }

        if (type == null || "".equals(type)) {
            throw new ControllerException("图片类型错误");
        }

        if (personnelId == null || "".equals(personnelId)) {
            throw new ControllerException("参数错误");
        }

//        if (deviceId == null || "".equals(deviceId)) {
//            throw new ControllerException("请选择设备");
//        }

        String fileType = ".jpg";
        String randomName = Tool.getRandomString(32);
        String fileName = randomName + fileType;

        FileEntity fileEntity = null;
        FaceEntity faceEntity = null;
        FileServicePo fileServicePo = null;

        FilePo filePo = new FilePo();
        ResultPo resultPo = new ResultPo();

        UploadFilePo uploadFilePo = new UploadFilePo();

        //头像
        if (Integer.parseInt(type) == 1) {

            fileServicePo = uploadService.uploadFile(file, fileName, Integer.parseInt(type), Integer.parseInt(personnelId));

            if (fileServicePo == null) {
                throw new ControllerException("头像保存失败");
            }

            fileEntity = (FileEntity) fileServicePo.getObject();

            if (fileEntity == null) {
                throw new ControllerException("头像保存失败");
            }

            filePo.setFileName(fileEntity.getFileUri());

            uploadFilePo.setFileId(fileEntity.getId());

            resultPo.setMessage("上传成功");
        }

        //人脸
        if (Integer.parseInt(type) == 2) {

            fileServicePo = uploadService.uploadFile(file, fileName, Integer.parseInt(type), Integer.parseInt(personnelId));

//            if (fileServicePo == null) {
//                throw new ControllerException("人脸保存失败");
//            }
//
//            if (fileServicePo.getResultType() == 1) {
//                throw new ControllerException("图片没有检测到人脸,请重新上传");
//            }
//
//            if (fileServicePo.getResultType() == 2) {
//                throw new ControllerException("图片检测到多个人脸,请重新上传");
//            }
//
//            if (fileServicePo.getResultType() == 3) {
//                throw new ControllerException("图片保存失败");
//            }

            if (fileServicePo.getResultType() == 0) {

//                faceEntity = (FaceEntity) fileServicePo.getObject();
//
//                filePo.setFileName(faceEntity.getFaceUri());
//
//                uploadFilePo.setFileId(faceEntity.getId());

                resultPo.setMessage("提交成功,请等待管理员审核");
            }
        }

        filePo.setFileHost(IMG_ROOT);
        filePo.setFileType(fileType);

        uploadFilePo.setFilePo(filePo);

        resultPo.setStatus("SUCCESS");
        resultPo.setData(uploadFilePo);

        return resultPo;
    }
}
