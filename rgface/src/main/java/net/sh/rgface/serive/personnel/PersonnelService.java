package net.sh.rgface.serive.personnel;

import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.session.PersonnelSession;
import net.sh.rgface.repository.PersonnelRepository;
import net.sh.rgface.util.Base64Util;
import net.sh.rgface.util.RSAEncrypt;
import net.sh.rgface.util.RSAPrivateKeyManager;
import net.sh.rgface.vo.personnel.PersonnelVo;
import net.sh.rgface.vo.personnel.PersonnelLoginVo;
import net.sh.rgface.vo.personnel.PersonnelUpdatePasswordVo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/5/14.
 */

@Service
public class PersonnelService {

    @Resource
    private PersonnelRepository personnelRepository;

    public PersonnelEntity login(PersonnelLoginVo personnelLoginVo, String loginIp) throws Exception {

        //明文密码
        byte[] lawsPassword;
        String privateKey = RSAPrivateKeyManager.getInstance().getPrivateKey("hkPNn9kXoLurGY67");

        System.out.println("私钥：" + privateKey);

        try {
            lawsPassword = RSAEncrypt.decryptByPrivateKey(Base64Util.decode(personnelLoginVo.getPassword()), privateKey);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        PersonnelEntity personnelEntity = null;
        if (lawsPassword != null) {

            personnelEntity = personnelRepository.findByIcCardAndPasswordAndDeleteTag(
                    personnelLoginVo.getPersonnelIcCard(),
                    new String(lawsPassword),
                    0
            );
        }

        if (personnelEntity == null) {
            return null;
        }

        personnelEntity.setLastLoginIp(loginIp);
        personnelEntity.setLastLoginTime((int) (System.currentTimeMillis() / 1000));

        personnelRepository.saveAndFlush(personnelEntity);

        return personnelEntity;
    }

    public boolean updatePersonnelInfo(PersonnelVo personnelVo, PersonnelSession personnelSession) {

        PersonnelEntity personnelEntity = personnelRepository.findById(personnelSession.getPersonnelId());

        if (personnelEntity == null) {
            return false;
        }

        //1为默认头像
        if (personnelVo.getFileId() > 1) {
            personnelEntity.setFileId(personnelVo.getFileId());
        }

        if (personnelVo.getFaceId() > 1) {
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

    public boolean updatePersonnelPassword(PersonnelSession personnelSession
            , PersonnelUpdatePasswordVo personnelUpdatePasswordVo) {

        PersonnelEntity personnelEntity = personnelRepository.findByIcCardAndPassword(
                personnelSession.getIcCard(),
                personnelUpdatePasswordVo.getOldPassword()
        );

        if (personnelEntity == null) {
            return false;
        }

        personnelRepository.updatePersonnelPasswordByIcCard(
                personnelSession.getIcCard(),
                personnelUpdatePasswordVo.getPassword()
        );

        return true;
    }

    public PersonnelEntity getPersonnelById(int personnelId) {

        return personnelRepository.findById(personnelId);
    }

    public PersonnelEntity getPersonnelByIcCard(String personnelIcCard) {

        return personnelRepository.findByIcCard(personnelIcCard);
    }

    public void save(PersonnelEntity personnelEntity) {
        if (personnelEntity != null) {
            personnelRepository.saveAndFlush(personnelEntity);
        }
    }
}
