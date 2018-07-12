package net.sh.rgface.serive.admin;

import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.EnvironmentImgEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.RecordingEntity;
import net.sh.rgface.repository.DeviceRepository;
import net.sh.rgface.repository.EnvironmentImgRepository;
import net.sh.rgface.repository.PersonnelRepository;
import net.sh.rgface.repository.RecordingRepository;
import net.sh.rgface.serive.base.ESSearchService;
import net.sh.rgface.util.FileUtil;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by DESTINY on 2018/5/21.
 */

@Service
public class AdminRecordingService {

    @Value("${face.recognition.environmentPath}")
    private String ENVIRONMENT_PATH;

    @Resource
    private ESSearchService<RecordingEntity> esSearchService;

    @Resource
    private DeviceRepository deviceRepository;

    @Resource
    private RecordingRepository recordingRepository;

    @Resource
    private PersonnelRepository personnelRepository;

    @Resource
    private EnvironmentImgRepository environmentImgRepository;

    public void addRecording(int personnelId, String deviceCode, byte[] environmentImgData) {

        RecordingEntity recordingEntity = new RecordingEntity();

        String imgName = Tool.getRandomString(32);
        try {

            FileUtil.byteToImageFile(environmentImgData, ENVIRONMENT_PATH + imgName + ".jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }

        EnvironmentImgEntity environmentImgEntity = new EnvironmentImgEntity();
        environmentImgEntity.setEnvironmentUri(imgName + ".jpg");
        environmentImgEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
        environmentImgEntity.setDeleteTag(0);

        environmentImgRepository.saveAndFlush(environmentImgEntity);
        if (environmentImgEntity.getId() != 0) {
            recordingEntity.setEnvironmentId(environmentImgEntity.getId());
        }

        PersonnelEntity personnelEntity = personnelRepository.findById(personnelId);
        if (personnelEntity != null) {

            recordingEntity.setPersonnelId(personnelEntity.getId());
        }

        DeviceEntity deviceEntity = deviceRepository.findByDeviceCode(deviceCode);
        if (deviceEntity != null) {
            recordingEntity.setDeviceId(deviceEntity.getId());
        }

        recordingEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
        recordingEntity.setDeleteTag(0);

        recordingRepository.saveAndFlush(recordingEntity);

        //搜索引擎
        //esSearchService.saveEntity(recordingEntity, "recording_entity", "recording");
    }

    //获取最新记录
    public List<RecordingEntity> getNewestRecordingList(int pageSize) {

        if (pageSize <=0 ) {
            return null;
        }

        return recordingRepository.getNewestRecordingByTime(pageSize);
    }

    //获取列表
    public Page<RecordingEntity> getRecordingList(int pageCode, int pageSize, SearchVo searchVo) {

        Pageable pageable = null;

        Page<RecordingEntity> page = null;

        //正常获取列表
        if (searchVo.getType() == 0) {

            pageable = PageRequest.of(pageCode - 1, pageSize, new Sort(Sort.Direction.DESC, "create_time"));

            page = recordingRepository.findAll(0, pageable);
        }

        //搜索
        if (searchVo.getType() == 1) {

            pageable = PageRequest.of(pageCode - 1, pageSize);

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = recordingRepository.searchRecordingByTruenameOrIcCardOrCode("%" + searchVo.getSearchParam() + "%", pageable);
            }

            //searchParam Or searchTime
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0)) {

                page = recordingRepository.searchRecordingBySearchAll(
                        "%" + searchVo.getSearchParam() + "%",
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }

            //searchTime
            if ((searchVo.getSearchParam() == null || searchVo.getSearchParam().length() == 0) &&
                    searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0) {

                page = recordingRepository.searchRecordingByCreateTime(
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }
        }

        return page;
    }

}
