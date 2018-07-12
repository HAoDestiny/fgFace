package net.sh.rgface.serive.admin;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.pojo.AppConfig;
import com.ha.facecamera.configserver.pojo.Version;
import net.sh.rgface.config.ApplicationInitRunner;
import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.DeviceEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.admin.AdminDevicePo;
import net.sh.rgface.repository.AccountRepository;
import net.sh.rgface.repository.DeviceRepository;
import net.sh.rgface.vo.BaseVo;
import net.sh.rgface.vo.admin.device.DeviceVo;
import net.sh.rgface.vo.admin.device.UpdateDeviceStatusVo;
import net.sh.rgface.vo.search.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DESTINY on 2018/5/21.
 */

@Service
public class AdminDeviceService {

    @Resource
    private DeviceRepository deviceRepository;

    @Resource
    private AccountRepository accountRepository;

    //1 设备编号重复 2操作用户不存在 3添加失败 4成功
    public int addDevice(DeviceVo deviceVo, AdminSession adminSession) {

        DeviceEntity deviceEntity = deviceRepository.findByDeviceCode(deviceVo.getDeviceCode());

        if (deviceEntity != null) {
            return 1;
        }

        deviceEntity = new DeviceEntity();

        deviceEntity.setDeviceName(deviceVo.getDeviceName());
        deviceEntity.setDeviceCode(deviceVo.getDeviceCode());
        deviceEntity.setDataServerIp(deviceVo.getDataServerIp());
        deviceEntity.setDataServerPort(deviceVo.getDataServerPort() + "");
        deviceEntity.setConfigServerIp(deviceVo.getConfigServerIp());
        deviceEntity.setConfigServerPort(deviceVo.getConfigServerPort() + "");

        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        if (configServer != null && configServer.getCameraOnlineState(deviceVo.getDeviceCode())) {

            Version version = configServer.getVersion(deviceVo.getDeviceCode());

            if (version != null) {

                deviceEntity.setDeviceFirmwareVersion(version.getFirewareVersion()); //固件版本号
            }
        }

        deviceEntity.setContrastStatus(1); //默认不开启对比
        deviceEntity.setRepeatStatus(1); //默认不开启出去重复 间隔1秒 最大255 最小1
        deviceEntity.setRepeatTimeInterval(1);

        if (deviceVo.getDeviceNote() != null) {
            deviceEntity.setDeviceNote(deviceVo.getDeviceNote());
        }

        if (deviceVo.getDeviceType() != 0 && deviceVo.getDeviceType() != 1) {
            return 5;
        }

        deviceEntity.setDeviceType(deviceVo.getDeviceType()); //0进 1出

        if (adminSession.getId() > 0) {

            AccountEntity accountEntity = accountRepository.findById(adminSession.getId()).get();

            if (accountEntity == null) {
                return 2;
            }

            deviceEntity.setAccountId(accountEntity.getId());
        }

        deviceEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
        deviceEntity.setDeleteTag(0);

        deviceRepository.saveAndFlush(deviceEntity);

        if (deviceEntity.getId() == 0) {
            return 3;
        }

        return 4;
    }

    public List<AdminDevicePo> getDeviceList() {

        List<AdminDevicePo> adminDevicePos = new ArrayList<>();
        List<DeviceEntity> deviceEntities = deviceRepository.findAll();


        if (deviceEntities.size() > 0) {

            for (DeviceEntity deviceEntity : deviceEntities) {

                AdminDevicePo adminDevicePo = new AdminDevicePo();
                adminDevicePo.setDeviceName(deviceEntity.getDeviceName());
                adminDevicePo.setDeviceCode(deviceEntity.getDeviceCode());
                adminDevicePo.setDeviceType(deviceEntity.getDeviceType());

                adminDevicePos.add(adminDevicePo);
            }

        }

        return adminDevicePos;
    }

    public Page<DeviceEntity> getDeviceList(int pageCode, int pageSize, SearchVo searchVo) {

        Sort sort = new Sort(Sort.Direction.DESC, "create_time");
        Pageable pageable = PageRequest.of(pageCode - 1, pageSize, sort);

        Page<DeviceEntity> page = null;

        //全部
        if (searchVo.getType() == 0) {

            page = deviceRepository.findByDeleteTag(pageable);
        }

        //搜索
        if (searchVo.getType() == 1) {

            //searchParam
            if (searchVo.getSearchParam() != null && searchVo.getSearchParam().length() > 0 &&
                    (searchVo.getStartTime() == 0 || searchVo.getEndTime() == 0)) {

                page = deviceRepository.searchDeviceByNameOrCode("%" + searchVo.getSearchParam() + "%", pageable);
            }
        }

        return page;
    }

    public int updateDevice(DeviceVo deviceVo) throws ControllerException {

        DeviceEntity deviceEntity = deviceRepository.findByDeviceCode(deviceVo.getDeviceCode());

        if (deviceEntity == null) {
            return 0;
        }

        ConfigServer configServer = ApplicationInitRunner.getConfigServer();

        //设备离线
        if (!configServer.getCameraOnlineState(deviceEntity.getDeviceCode())) {
            return 0;
        }

        deviceEntity.setDeviceName(deviceVo.getDeviceName());
        deviceEntity.setDeviceCode(deviceVo.getDeviceCode());
        deviceEntity.setDataServerIp(deviceVo.getDataServerIp());
        deviceEntity.setDataServerPort(deviceVo.getDataServerPort() + "");
        deviceEntity.setConfigServerIp(deviceVo.getConfigServerIp());
        deviceEntity.setConfigServerPort(deviceVo.getConfigServerPort() + "");

        if (deviceVo.getDeviceNote() != null) {
            deviceEntity.setDeviceNote(deviceVo.getDeviceNote());
        }

        if (deviceVo.getDeviceType() != 0 && deviceVo.getDeviceType() != 1) {
            return 1;
        }

        deviceEntity.setDeviceType(deviceVo.getDeviceType());

        AppConfig appConfig = ApplicationInitRunner.getConfigServer().getAppConfigByDeviceNo(deviceVo.getDeviceCode());

        appConfig.setDeviceNo(deviceVo.getDeviceCode());
        appConfig.setAddrName(deviceVo.getDeviceName());

        //外网配置
        appConfig.setCloundConfigEnable(true);
        appConfig.setCloundConfigIp(deviceVo.getConfigServerIp());
        appConfig.setCloundConfigPort(deviceVo.getConfigServerPort());

//        //上传方式 0关闭 1TCP 2FTP 3HTTP
        appConfig.setDataUploadMethod((short) 1);
        appConfig.setDataUploadServer(deviceVo.getDataServerIp());
        appConfig.setDataUploadPort(deviceVo.getDataServerPort());
//        appConfig.setCompareSwitch(true);
//        appConfig.setEnsureThreshold(80);

        Version version = ApplicationInitRunner.getConfigServer().getVersion(deviceVo.getDeviceCode());

        if (version != null) {

            if (version.getFirewareVersion() != null && !"".equals(version.getFirewareVersion())) {
                deviceEntity.setDeviceFirmwareVersion(version.getFirewareVersion());
            }

        }

        deviceConfig(deviceVo.getDeviceCode(), appConfig);

        deviceRepository.saveAndFlush(deviceEntity);

        return 2;
    }

    public boolean updateDeviceStatus(BaseVo baseVo) {

        DeviceEntity deviceEntity = deviceRepository.findById(baseVo.getId()).get();

        if (deviceEntity == null) {
            return false;
        }

        if (baseVo.getType() == 0) {
            deviceRepository.updateDeviceDeleteTag(baseVo.getId(), 0);
        }

        if (baseVo.getType() == 1) {
            deviceRepository.updateDeviceDeleteTag(baseVo.getId(), 1);
        }

        return true;
    }

    //0设备不存在 1设备以离线 2去除重复未开启 3对比功能未开启 4操作成功
    public int updateDeviceStatus(UpdateDeviceStatusVo updateDeviceStatusVo) throws ControllerException {

        DeviceEntity deviceEntity = findDeviceEntityByDeviceCode(updateDeviceStatusVo.getDeviceCode());

        if (deviceEntity == null) {
            return 0;
        }

        if (!ApplicationInitRunner.getConfigServer().getCameraOnlineState(updateDeviceStatusVo.getDeviceCode())) {
            return 1;
        }

        AppConfig appConfig = ApplicationInitRunner.getConfigServer().getAppConfigByDeviceNo(updateDeviceStatusVo.getDeviceCode());

        //对比开关
        if (updateDeviceStatusVo.getType() == 0) {

            //开
            if (updateDeviceStatusVo.getStatus() == 0) {
                appConfig.setCompareSwitch(true);
                appConfig.setEnsureThreshold(deviceEntity.getContrastLike());

                deviceEntity.setContrastStatus(0);
            }

            //关
            if (updateDeviceStatusVo.getStatus() == 1) {
                appConfig.setCompareSwitch(false);

                deviceEntity.setContrastStatus(1);
            }
        }

        //去除重复
        if (updateDeviceStatusVo.getType() == 1) {

            //开
            if (updateDeviceStatusVo.getStatus() == 0) {
                appConfig.setRepeatFilter(true);

                deviceEntity.setRepeatStatus(0);
            }

            //关
            if (updateDeviceStatusVo.getStatus() == 1) {
                appConfig.setRepeatFilter(false);

                deviceEntity.setRepeatStatus(1);
            }
        }

        //设置去除重复间隔
        if (updateDeviceStatusVo.getType() == 2 && updateDeviceStatusVo.getStatus() == 2) {

            if (!appConfig.isRepeatFilter()) {
                return 2;
            }

            appConfig.setRepeatFilterTime(updateDeviceStatusVo.getRepeatTimeInterval());
            deviceEntity.setRepeatTimeInterval(updateDeviceStatusVo.getRepeatTimeInterval());
        }

        //相似度
        if (updateDeviceStatusVo.getType() == 3 && updateDeviceStatusVo.getStatus() == 3) {

            if (!appConfig.isCompareSwitch()) {
                return 3;
            }

            appConfig.setEnsureThreshold(updateDeviceStatusVo.getContrastLike());

            deviceEntity.setContrastLike(updateDeviceStatusVo.getContrastLike());
        }

        deviceConfig(updateDeviceStatusVo.getDeviceCode(), appConfig);

        deviceRepository.saveAndFlush(deviceEntity);

        return 4;
    }

    public DeviceEntity findDeviceEntityByDeviceCode(String deviceCode) {
        return deviceRepository.findByDeviceCode(deviceCode);
    }

    private boolean deviceConfig(String deviceCode, AppConfig appConfig) throws ControllerException {

        boolean ret = ApplicationInitRunner.getConfigServer().setAppConfig(deviceCode, appConfig, 50000);

        if (!ret) {
            System.out.println("更新设备参数失败 错误信息： " + ApplicationInitRunner.getConfigServer().getLastErrorMsg(deviceCode) + " ,Error Code："
                    + ApplicationInitRunner.getConfigServer().getLastErrorCode(deviceCode));

            throw new ControllerException("更新失败");
        }

        return ret;
    }

}
