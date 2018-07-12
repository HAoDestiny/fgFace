package net.sh.rgface.serive.base;

import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.util.SessionManagement;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Created by DESTINY on 2018/7/2.
 */

@Service
public class SessionService {

    public void checkIsOnLine(HttpSession session_old, HttpSession session_new, String key) throws ControllerException {

        if (session_old != null) {

            //强制过期
            session_old.invalidate();

            SessionManagement.getInstance().removeSession(key);

            //更新 -- 将sessionId 更换为 adminSessionId
            SessionManagement.getInstance().removeSession(session_new.getId());
            SessionManagement.getInstance().addSession(key, session_new);
        }

        //未登录
        else {

            HttpSession session_admin = SessionManagement.getInstance().getSession(session_new.getId());

            if (session_admin == null) {
                throw new ControllerException("业务异常");
            }

            //替换id - session
            SessionManagement.getInstance().addSession(key, session_admin);
            SessionManagement.getInstance().removeSession(session_new.getId());
        }
    }

}
