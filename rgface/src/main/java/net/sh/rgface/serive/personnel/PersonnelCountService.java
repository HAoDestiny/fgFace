package net.sh.rgface.serive.personnel;

import net.sh.rgface.entity.PersonnelCountEntity;
import net.sh.rgface.repository.PersonnelCountRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by DESTINY on 2018/6/14.
 */

@Service
public class PersonnelCountService {

    @Resource
    private PersonnelCountRepository personnelCountRepository;

    public PersonnelCountEntity findById(int id) {

        return personnelCountRepository.findById(id).get();
    }

    public boolean save(PersonnelCountEntity personnelCountEntity) {

        personnelCountRepository.saveAndFlush(personnelCountEntity);

        if (personnelCountEntity.getId() == 0) {
            return false;
        }

        return true;
    }

    public int getGoOutCount() {
       return personnelCountRepository.getGoOutCount();
    }

    public void addPersonnelCouuntByGoOut() {
        personnelCountRepository.addPersonnelCouuntByGoOut();
    }

    public void lessPersonnelCouuntByGoOut() {
        personnelCountRepository.lessPersonnelCouuntByGoOut();
    }

    public void addPersonnelCouuntByLeave() {
        personnelCountRepository.addPersonnelCouuntByLeave();
    }

    public void lessPersonnelCouuntByLeave() {
        personnelCountRepository.lessPersonnelCouuntByLeave();
    }

    public void addPersonnelCouuntByNormal() {
        personnelCountRepository.addPersonnelCouuntByNormal();
    }

    public void lessPersonnelCouuntByNormal() {
        personnelCountRepository.lessPersonnelCouuntByNormal();
    }

    public void addPersonnelCouuntByOther() {
        personnelCountRepository.addPersonnelCouuntByOther();
    }

    public void lessPersonnelCouuntByOther() {
        personnelCountRepository.lessPersonnelCouuntByOther();
    }

}
