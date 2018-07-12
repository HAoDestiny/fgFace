package net.sh.rgface.controller.Base;

import lombok.extern.slf4j.Slf4j;
import net.sh.rgface.annotations.NotAspect;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.util.VerifyCodeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * Created by DESTINY on 2018/6/1.
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/verification", method = RequestMethod.GET)
public class VerificationController {

    @NotAspect
    @RequestMapping(value = "/getVerificationImg")
    public void getVerificationImg(@PathParam("v") String v
            , HttpServletResponse httpServletResponse, HttpSession httpSession) throws ControllerException, IOException {

        if (null != v && v.length() > 0) {

            String verifyCode = VerifyCodeUtil.outputVerifyImage(116, 36, httpServletResponse.getOutputStream(), 5);

            if (verifyCode == null || verifyCode.length() == 0) {
                throw new ControllerException("验证码获取失败");
            }

            if ("a".equals(v)) {
                httpSession.setAttribute("admin_verificationCode", verifyCode);
            }

            if ("p".equals(v)) {
                httpSession.setAttribute("personnel_verificationCode", verifyCode);
            }

            log.info("----------- session verification: " + verifyCode);
        }
    }

}
