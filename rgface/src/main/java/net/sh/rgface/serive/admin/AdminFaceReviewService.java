package net.sh.rgface.serive.admin;

import com.ha.facecamera.configserver.Constants;
import com.ha.facecamera.configserver.pojo.FaceToUpload;
import net.sh.rgface.entity.FaceEntity;
import net.sh.rgface.entity.FaceReviewEntity;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminDevicePo;
import net.sh.rgface.po.admin.AdminFacePo;
import net.sh.rgface.repository.FaceReviewRepository;
import net.sh.rgface.serive.face.FaceService;
import net.sh.rgface.serive.personnel.PersonnelService;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.admin.face.AdminFaceVo;
import net.sh.rgface.vo.admin.faceReview.FaceReviewVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/6/22.
 */

@Service
public class AdminFaceReviewService {

    @Value("${face.recognition.faceRoot}")
    private String FACE_ROOT_PATH;

    @Resource
    private FaceService faceService;

    @Resource
    private PersonnelService personnelService;

    @Resource
    private FaceReviewRepository faceReviewRepository;

    //资料审核 0记录不存在 1操作成功
    public int faceReview(FaceReviewVo faceReviewVo) {

        FaceReviewEntity faceReviewEntity = faceReviewRepository.findById(faceReviewVo.getFaceReviewId());

        if (faceReviewEntity == null) {
            return 0;
        }

        //通过
        if (faceReviewVo.getStatus() == 1) {
            faceReviewEntity.setStatus(1);
        }

        //拒绝
        if (faceReviewVo.getStatus() == 2) {
            faceReviewEntity.setStatus(2);
        }

        faceReviewRepository.saveAndFlush(faceReviewEntity);

        return 1;
    }

    //下发
    public int lssuedFace(FaceReviewVo faceReviewVo) {

        int isSuc = 0;
        FaceEntity faceEntity = null;
        FaceReviewEntity faceReviewEntity = null;
        PersonnelEntity personnelEntity = personnelService.getPersonnelById(faceReviewVo.getPersonnelId());

        if (personnelEntity != null) {

            faceReviewEntity = personnelEntity.getFaceReviewByFaceReviewId();

            //业务异常
            if (faceReviewEntity == null) {
                return 6;
            }

            //授权成功
            if (faceReviewVo.getStatus() == 1) {

                //人脸存在 - 修改 1=默认值
                if (personnelEntity.getFaceId() > 1) {

                    faceEntity = faceService.findFaceEntityById(personnelEntity.getFaceId());

                    if (faceEntity != null) {
                        isSuc = faceService.updateFaceMessage(personnelEntity, faceEntity.getFaceUri(), faceReviewVo.getDeviceId(), 5000);
                    }

                    //更新审核记录
                    if (isSuc == 1) {
                        faceReviewEntity.setLssuedStatus(1); //下发成功
                    }

                    if (isSuc == 2) {
                        faceReviewEntity.setStatus(2); //通过失败
                        faceReviewEntity.setLssuedStatus(2); //授权失败
                    }

                }

                //添加人脸
                else {

                    isSuc = faceService.addFace(
                            FACE_ROOT_PATH + faceReviewEntity.getFaceReviewUri(),
                            personnelEntity,
                            faceReviewVo.getDeviceId()
                    );

                    //更新审核记录状态
                    if (isSuc == 1) {
                        //添加faceEntity
                        faceEntity = new FaceEntity();
                        faceEntity.setFaceUri(faceReviewEntity.getFaceReviewUri());
                        faceEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
                        faceEntity.setDeleteTag(0);

                        faceService.saveFaceEntity(faceEntity);

                        if (faceEntity.getId() == 0) {
                            return 5;
                        }

                        faceReviewEntity.setLssuedStatus(1);
                        personnelEntity.setFaceId(faceEntity.getId());

                        personnelService.save(personnelEntity);
                    }

                    if (isSuc == 2) {
                        faceReviewEntity.setLssuedStatus(2);
                    }
                }

            }

            //重新审核
            if (faceReviewVo.getStatus() == 2) {

                faceReviewEntity.setStatus(0);
                faceReviewEntity.setLssuedStatus(0);
            }

            //撤销授权
            if (faceReviewVo.getStatus() == 3) {

                AdminFaceVo adminFaceVo = new AdminFaceVo();
                adminFaceVo.setDeviceCode(faceReviewVo.getDeviceId());
                adminFaceVo.setFaceCode(personnelEntity.getIcCard());

                isSuc = faceService.removeFace(adminFaceVo);

                if (isSuc != 1) {
                    return 8;
                }

                faceReviewEntity.setLssuedStatus(0);

                personnelEntity.setFaceId(1);

                personnelService.save(personnelEntity);
            }

            faceReviewRepository.saveAndFlush(faceReviewEntity);
        }

        return isSuc;
    }

    public Page<FaceReviewEntity> getFaceReviewList(int pageCode, int pageSize, int type, SearchVo searchVo) {

        Page<FaceReviewEntity> page = null;
        Pageable pageable = null;

        //全部
        if (searchVo.getType() == 0) {
            pageable = PageRequest.of(pageCode - 1, pageSize, new Sort(Sort.Direction.DESC, "create_time"));

            //资料审核list
            if (type == 1) {
                page = faceReviewRepository.findByStatus(0, pageable);
            }

            //下发list
            if (type == 2) {
                page = faceReviewRepository.findByStatus(1, pageable);
            }
        }

        //搜索
        if (searchVo.getType() == 1) {

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                //资料审核list
                if (type == 1) {
                    page = faceReviewRepository.searchFaceReviewByTruenameOrIcCardOrCode(
                            "%" + searchVo.getSearchParam() + "%",
                            0,
                            pageable
                    );
                }

                //下发list
                if (type == 2) {
                    page = faceReviewRepository.searchFaceReviewByTruenameOrIcCardOrCode(
                            "%" + searchVo.getSearchParam() + "%",
                            1,
                            pageable
                    );
                }
            }

            //searchParam Or searchTime
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0)) {

                //资料审核list
                if (type == 1) {
                    page = faceReviewRepository.searchFaceReviewBySearchAll(
                            "%" + searchVo.getSearchParam() + "%",
                            searchVo.getStartTime(),
                            searchVo.getEndTime(),
                            0,
                            pageable
                    );
                }

                //下发list
                if (type == 2) {
                    page = faceReviewRepository.searchFaceReviewBySearchAll(
                            "%" + searchVo.getSearchParam() + "%",
                            searchVo.getStartTime(),
                            searchVo.getEndTime(),
                            1,
                            pageable
                    );
                }
            }

            //searchTime
            if ((searchVo.getSearchParam() == null || searchVo.getSearchParam().length() == 0) &&
                    searchVo.getStartTime() != 0 && searchVo.getEndTime() != 0) {

                pageable = PageRequest.of(pageCode - 1, pageSize, new Sort(Sort.Direction.DESC, "create_time"));

                //资料审核list
                if (type == 1) {
                    page = faceReviewRepository.searchFaceReviewByCreateTime(
                            searchVo.getStartTime(),
                            searchVo.getEndTime(),
                            0,
                            pageable
                    );
                }

                //下发list
                if (type == 2) {
                    page = faceReviewRepository.searchFaceReviewByCreateTime(
                            searchVo.getStartTime(),
                            searchVo.getEndTime(),
                            1,
                            pageable
                    );
                }
            }
        }

        return page;
    }
}
