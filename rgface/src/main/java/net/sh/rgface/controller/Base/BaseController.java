package net.sh.rgface.controller.Base;

import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.admin.AdminDeviceService;
import net.sh.rgface.util.RSAEncrypt;
import net.sh.rgface.util.RSAPrivateKeyManager;
import net.sh.rgface.vo.personnel.RSAVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Map;

/**
 * Created by DESTINY on 2018/6/15.
 */

@RestController
@RequestMapping(value = "/api/base", method = RequestMethod.POST)
public class BaseController {

    @Resource
    private AdminDeviceService adminDeviceService;

    @NotAspect
    @RequestMapping(value = "/getDeviceNameList")
    public ResultPo getDeviceNameList() {

        ResultPo resultPo = new ResultPo();

        resultPo.setStatus("SUCCESS");
        resultPo.setData(adminDeviceService.getDeviceList());
        resultPo.setMessage("获取成功");

        return resultPo;
    }

    @NotAspect
    @RequestMapping(value = "/getPublicKey")
    public ResultPo getPublicKey(@RequestBody RSAVo rsaVo) throws Exception {

        if (null == rsaVo.getMch_id() || rsaVo.getMch_id().length() == 0) {
            throw new ControllerException("ACTION_EXCEPTION PARAM_ERROR");
        }

        Map<String, Object> keyMap = RSAEncrypt.genKeyPair();

        if ( null == keyMap) {
            throw new ControllerException("ACTION_EXCEPTION GET_PUBLIC_KEY FAIL");
        }

        String RSAPublicKey = RSAEncrypt.getPublicKey(keyMap);
        String RSAPrivateKey = RSAEncrypt.getPrivateKey(keyMap);

        RSAPrivateKeyManager.getInstance().setPrivateKey(rsaVo.getMch_id(), RSAPrivateKey);

        for (String key : RSAPrivateKeyManager.getInstance().getPrivateKeyMap().keySet()) {
            System.out.println("私钥： " + key + ", value: " + RSAPrivateKeyManager.getInstance().getPrivateKeyMap().get(key));
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setMessage("获取成功");
        resultPo.setStatus("SUCCESS");
        resultPo.setData(RSAPublicKey);

        return resultPo;
    }
}
