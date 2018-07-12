package net.sh.rgface.serive.admin;

import net.sh.rgface.entity.SystemParamEntity;
import net.sh.rgface.repository.*;
import net.sh.rgface.vo.system.SystemParamVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by DESTINY on 2018/5/5.
 */

@Service
public class AdminSystemService {

    @Value("${sh.face.recognition.cms}")
    private String CMS;

    @Value("${sh.face.recognition.version}")
    private String VERSION;

    @Value("${sh.face.recognition.author}")
    private String AUTHOR;

    @Value("${sh.face.recognition.homePage}")
    private String HOME_PAGE;

    @Value("${sh.face.recognition.server}")
    private String SERVER;

    @Value("${sh.face.recognition.dataBase}")
    private String DATA_BASE;

    @Value("${sh.face.recognition.maxUpload}")
    private String MAX_UPLOAD;

    @Resource
    private PersonnelRepository personnelRepository;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private DeviceRepository deviceRepository;

    @Resource
    private RecordingRepository recordingRepository;

    @Resource
    private SystemParamRepository systemParamRepository;

    public SystemParamEntity setSystemMessage(SystemParamVo systemParamVo) {

        SystemParamEntity systemParamEntity = getSystemParam();

        if (systemParamEntity == null) {
            systemParamEntity = new SystemParamEntity();
        }

        systemParamEntity.setCmsName(systemParamVo.getCmsName());
        systemParamEntity.setsVersion(systemParamVo.getVersion());
        systemParamEntity.setsAuthor(systemParamVo.getAuthor());
        systemParamEntity.setHomePage(systemParamVo.getHomePage());
        systemParamEntity.setsServer(systemParamVo.getServer());
        systemParamEntity.setsDataBase(systemParamVo.getDataBase());
        systemParamEntity.setMaxUpload(systemParamVo.getMaxUpload());
        systemParamEntity.setPowerBy(systemParamVo.getPowerby());
        systemParamEntity.setRecord(systemParamVo.getRecord());

        if (systemParamVo.getKeywords() != null) {
            systemParamEntity.setKeyWords(systemParamVo.getKeywords());
        }

        if (systemParamVo.getDescription() != null) {
            systemParamEntity.setDescription(systemParamVo.getDescription());
        }

        systemParamRepository.saveAndFlush(systemParamEntity);

        if (systemParamEntity.getId() == 0) {
            return null;
        }

        return systemParamEntity;
    }

    //统计
    public Map<String, Integer> getSystemTotal() {

        Map<String, Integer> countsMap = new HashMap<>();

        countsMap.put("adminAccountCount", accountRepository.getAdminAccountCounts()); //管理员
        countsMap.put("userCount", accountRepository.getAccountCounts()); //用户
        countsMap.put("personnelCount", personnelRepository.getPersonnelCounts()); //人员
        countsMap.put("deviceCount", deviceRepository.getDeviceCounts()); //设备
        countsMap.put("recordingCount", recordingRepository.getRecordingCounts()); //日志

        return countsMap;
    }

    public SystemParamEntity getSystemParam() {

        SystemParamEntity systemParamEntity = null;

        Optional<SystemParamEntity> systemParamEntityOptional = systemParamRepository.findById(1);
        if (systemParamEntityOptional.isPresent()) {
            systemParamEntity = systemParamEntityOptional.get();
        }

        return systemParamEntity;
    }

    public Map<String, String> getSystemMessage() {

        Map<String, String> systemMessageMap = new HashMap<>();

        SystemParamEntity systemParamEntity = null;
        Optional<SystemParamEntity> systemParamEntityOptional = systemParamRepository.findById(1);

        systemMessageMap.put("cms", CMS);
        systemMessageMap.put("version", VERSION);
        systemMessageMap.put("author", AUTHOR);
        systemMessageMap.put("homePage", HOME_PAGE);
        systemMessageMap.put("server", SERVER);
        systemMessageMap.put("dataBase", DATA_BASE);
        systemMessageMap.put("maxUpload", MAX_UPLOAD);

        if (systemParamEntityOptional.isPresent()) {
            systemParamEntity = systemParamEntityOptional.get();
        }

        if (systemParamEntity != null) {

            systemMessageMap.put("cms", systemParamEntity.getCmsName());
            systemMessageMap.put("version", systemParamEntity.getsVersion());
            systemMessageMap.put("author", systemParamEntity.getsAuthor());
            systemMessageMap.put("homePage", systemParamEntity.getHomePage());
            systemMessageMap.put("server", systemParamEntity.getsServer());
            systemMessageMap.put("dataBase", systemParamEntity.getsDataBase());
            systemMessageMap.put("maxUpload", systemParamEntity.getMaxUpload());
        }

        return systemMessageMap;
    }


}
