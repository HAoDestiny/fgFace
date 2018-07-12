package net.sh.rgface.controller.user;

import net.sh.rgface.entity.AccountEntity;
import net.sh.rgface.entity.session.UserSeesion;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.base.SessionService;
import net.sh.rgface.serive.personnel.PersonnelCountService;
import net.sh.rgface.serive.user.UserService;
import net.sh.rgface.util.SessionManagement;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.UserVo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by DESTINY on 2018/4/12.
 */

@RestController
@RequestMapping(value = "/api/user", method = RequestMethod.POST)
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SessionService sessionService;

    @Resource
    private PersonnelCountService personnelCountService;

    @RequestMapping(value = "/login")
    public ResultPo login(@RequestBody @Valid UserVo userVo, BindingResult bindingResult,
                          HttpSession session, HttpServletRequest httpServletRequest) throws ControllerException {

        if (bindingResult.hasErrors()) {
           List<ObjectError> errorList = bindingResult.getAllErrors();
           throw new ControllerException(errorList.get(0).getDefaultMessage() + "不能为空");
        }

        ResultPo resultPo = new ResultPo();

        AccountEntity accountEntity = userService.login(userVo, Tool.getIpAddress(httpServletRequest));

        UserSeesion userSeesion = new UserSeesion();
        userSeesion.setUid(accountEntity.getId());
        userSeesion.setUsername(accountEntity.getAccountName());

        HttpSession session_old = SessionManagement.getInstance().getSession(String.valueOf(accountEntity.getId()));

        sessionService.checkIsOnLine(session_old, session, String.valueOf(accountEntity.getId()));

        session.setAttribute("User", userSeesion);

        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("登录成功");

        return resultPo;
    }


    @RequestMapping(value = "/getGoOutCount")
    public ResultPo getGoOutCount() {

        ResultPo resultPo = new ResultPo();

        resultPo.setMessage("获取成功");
        resultPo.setData(personnelCountService.getGoOutCount());
        resultPo.setStatus("SUCCESS");

        return resultPo;
    }
}
