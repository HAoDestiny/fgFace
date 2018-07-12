package net.sh.rgface.serive.admin;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.Constants;
import com.ha.facecamera.configserver.pojo.FaceToUpload;
import net.sh.rgface.config.ApplicationInitRunner;
import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.FileEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.repository.AccountRepository;
import net.sh.rgface.repository.FaceRepository;
import net.sh.rgface.repository.FileRepository;
import net.sh.rgface.repository.PersonnelRepository;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.vo.BaseVo;
import net.sh.rgface.vo.personnel.PersonnelVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by DESTINY on 2018/5/9.
 */

@Service
public class AdminPersonnelService {

    @Resource
    private FileRepository fileRepository;

    @Resource
    private FaceRepository faceRepository;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private PersonnelRepository personnelRepository;

    @Resource
    private FaceService faceService;

    @Value("${face.recognition.faceRoot}")
    private String FACE_ROOT_PATH;

    //0所属账户不存在 1人员类型索引错误 2人员就读类型索引错误 3请选择头像上传 4请选择人脸上传 5操作成功
    public int addPersonnel(PersonnelVo personnelVo) {

        AccountEntity accountEntity = accountRepository.findByAccountName(personnelVo.getPersonnelByAccountName());

        if (accountEntity == null) {
            return 0;
        }

        PersonnelEntity personnelEntity = new PersonnelEntity();

        if (personnelVo.getPersonnelType() == 0 || personnelVo.getPersonnelType() == 1
                || personnelVo.getPersonnelType() == 2 || personnelVo.getPersonnelType() == 3) {

            personnelEntity.setPersonnelType(personnelVo.getPersonnelType());

        } else {
            return 1;
        }

        if (personnelVo.getStudyType() == 0 || personnelVo.getStudyType() == 1 || personnelVo.getStudyType() == 2) {
            personnelEntity.setStudyType(personnelVo.getStudyType());

        } else {
            return 2;
        }

        if (personnelVo.getFileId() <= 0) {
            return 3;
        }

        FileEntity fileEntity = fileRepository.findById(personnelVo.getFileId()).get();
        if (fileEntity == null) {
            return 3;
        }

        if (personnelVo.getFaceId() <= 0) {
            return 4;
        }

        FaceEntity faceEntity = faceRepository.findById(personnelVo.getFaceId());
        if (faceEntity == null) {
            return 4;
        }

        personnelEntity.setMobile(personnelVo.getMobile());
        personnelEntity.setIcCard(personnelVo.getIcCard());
        personnelEntity.setPassword(personnelVo.getPassword());
        personnelEntity.setTruename(personnelVo.getTruename());
        personnelEntity.setSex(personnelVo.getSex());
        personnelEntity.setStudyType(personnelVo.getStudyType());
        personnelEntity.setPersonnelClassCode(personnelVo.getPersonnelClassCode());
        personnelEntity.setPersonnelCode(personnelVo.getPersonnelCode());
        personnelEntity.setPersonnelGrade(personnelVo.getPersonnelGrade());
        personnelEntity.setPersonnelType(personnelVo.getPersonnelType());
        personnelEntity.setPersonnelStatus(0); //状态
        personnelEntity.setAccountId(accountEntity.getId()); //所属账户
        personnelEntity.setFileId(fileEntity.getId());
        personnelEntity.setFaceId(faceEntity.getId());
        personnelEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
        personnelEntity.setDeleteTag(personnelVo.getPersonnelStart());

        personnelRepository.saveAndFlush(personnelEntity);

        return 5;
    }

    public Page<PersonnelEntity> getPersonnelList(int pageCode, int pageSize, SearchVo searchVo) {

        Sort sort = new Sort(Sort.Direction.DESC, "create_time");

        Pageable pageable = PageRequest.of(pageCode - 1, pageSize, sort);

        Page<PersonnelEntity> page = null;

        //全部
        if (searchVo.getType() == 0) {

            page = personnelRepository.findByDeleteTag(pageable);
        }

        //搜索
        if (searchVo.getType() == 1) {

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = personnelRepository.searchPersonnelByTruenameOrIcCardOrCode("%" + searchVo.getSearchParam() + "%", pageable);
            }

            //searchParam Or searchTime
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0)) {
                page = personnelRepository.searchPersonnelBySearchAll(
                        "%" + searchVo.getSearchParam() + "%",
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }

            //searchTime
            if ((searchVo.getSearchParam() == null || searchVo.getSearchParam().length() == 0) &&
                    searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0) {

                pageable = PageRequest.of(pageCode - 1, pageSize, new Sort(Sort.Direction.DESC, "create_time"));

                page = personnelRepository.searchPersonnelByCreateTime(
                        searchVo.getStartTime(),
                        searchVo.getEndTime(),
                        pageable
                );
            }
        }

        return page;
    }

    public boolean updatePersonnelInfo(PersonnelVo personnelVo) {

        PersonnelEntity personnelEntity = null;
        if (personnelVo.getPersonnelId() > 0) {
            personnelEntity = personnelRepository.findByIcCard(personnelVo.getIcCard());
        }

        if (personnelEntity == null) {
            return false;
        }

        if (personnelVo.getFileId() >= 1) {
            personnelEntity.setFileId(personnelVo.getFileId());
        }

        if (personnelVo.getFaceId() >= 1) {
            personnelEntity.setFaceId(personnelVo.getFaceId());
        }

        personnelEntity.setSex(personnelVo.getSex());
        personnelEntity.setIcCard(personnelVo.getIcCard());
        personnelEntity.setMobile(personnelVo.getMobile());
        personnelEntity.setTruename(personnelVo.getTruename());
        personnelEntity.setStudyType(personnelVo.getStudyType());
        personnelEntity.setPersonnelType(personnelVo.getPersonnelType());
        personnelEntity.setPersonnelGrade(personnelVo.getPersonnelGrade());
        personnelEntity.setPersonnelCode(personnelEntity.getPersonnelCode());
        personnelEntity.setPersonnelClassCode(personnelVo.getPersonnelClassCode());

        personnelRepository.saveAndFlush(personnelEntity);

        return true;
    }

    //0更新人员状态 1更新人员人脸识别状态
    public boolean updatePersonnelStatusOrPassTag(BaseVo baseVo, int type) throws ControllerException {

        PersonnelEntity personnelEntity = personnelRepository.findById(baseVo.getId());

        if (personnelEntity == null) {
            return false;
        }

        if (type == 0) {

            //正常
            if (baseVo.getType() == 0) {
                personnelRepository.updatePersonnelDeleteTag(baseVo.getId(), 0);
            }

            //禁用
            if (baseVo.getType() == 1) {
                personnelRepository.updatePersonnelDeleteTag(baseVo.getId(), 1);
            }
        }

        if (type == 1) {

            FaceEntity faceEntity = faceRepository.findById(personnelEntity.getFaceId());

            if (faceEntity != null) {

                FaceToUpload faceToUpload = new FaceToUpload();
                faceToUpload.setName(personnelEntity.getTruename());
                faceToUpload.setId(personnelEntity.getIcCard());
                faceToUpload.setJpgFilePath(new String[]{FACE_ROOT_PATH + faceEntity.getFaceUri()}); //imgPath
                faceToUpload.setWiegandNo(0); //卡
                faceToUpload.setExpireDate(Constants.LONGLIVE); //时限

                //启用识别  白名单
                if (baseVo.getType() == 0) {
                    personnelRepository.updatePersonnelPassTag(baseVo.getId(), 0);

                    faceToUpload.setRole(1);
                }

                //禁用识别  黑名单
                if (baseVo.getType() == 1) {
                    personnelRepository.updatePersonnelPassTag(baseVo.getId(), 1);


                    faceToUpload.setRole(2);
                }

                //修改人脸信息
                int ret = faceService.updateFaceMessage(
                        personnelEntity,
                        faceEntity.getFaceUri(),
                        "20180523",
                        5000
                );

                if (ret == 0) {

                    throw new ControllerException("设备已离线");
                }

                if (ret == 2) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<PersonnelEntity> getPersonnelList() {
        return personnelRepository.findAll();
    }

    public PersonnelEntity savePersonnelEntity(PersonnelEntity personnelEntity) {
        return personnelRepository.saveAndFlush(personnelEntity);
    }

    public PersonnelEntity findPersonnelEntityByIcCard(String icCard) {
       return personnelRepository.findByIcCard(icCard);
    }
}
