package net.sh.rgface.serive.base;

import net.sh.rgface.entity.FileEntity;
import net.sh.rgface.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/4/18.
 */

@Service
public class FileService {

    @Value("${face.recognition.filePath}")
    private String FILE_ROOT;

    @Value("${face.recognition.facePath}")
    private String FACE_ROOT;

    @Value("${face.recognition.imagesProtocol}")
    private String IMG_ROOT;

    public String getFACE_ROOT() {
        return FACE_ROOT;
    }

    public String getIMG_ROOT() {
        return IMG_ROOT;
    }


    @Resource
    private FileRepository fileRepository;

    public FileEntity findFileEntityById(int id) {

        return fileRepository.findById(id).get();
    }
}
