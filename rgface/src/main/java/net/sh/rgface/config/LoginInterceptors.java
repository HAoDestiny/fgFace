package net.sh.rgface.config;

import lombok.extern.slf4j.Slf4j;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.entity.session.PersonnelSession;
import net.sh.rgface.entity.session.UserSeesion;
import net.sh.rgface.exception.ControllerException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by DESTINY on 2018/4/12.
 */

@Slf4j
public class LoginInterceptors implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException, ControllerException {

       log.warn(request.getRequestURI());

        //0 页面请求 1 api请求
        int requestType = 0;

        //0 其他动作 1 登录动作 2 寻找密码 3 上传
        int actionType = 0;

        if (request.getRequestURL().indexOf("/api/") > 1) {
            requestType = 1;
        }

        if (request.getRequestURL().indexOf("login") > 1) {
            actionType = 1;
        }

        if (request.getRequestURL().indexOf("upload") > 1) {
            actionType = 3;
        }

//        //验证码
//        if (request.getRequestURL().indexOf("verification") > 1) {
//            return true;
//        }

        //公共接口
        if (request.getRequestURL().indexOf("/api/base/") > 1) {
            return true;
        }

        AdminSession adminSession = (AdminSession) request.getSession().getAttribute("Admin");

        if (request.getRequestURL().indexOf("/admin/") > 1) {

            if (adminSession == null) {

                if (requestType == 1 && actionType == 1) {
                    return true;
                }

                if (requestType == 1 && actionType == 0) {
                    throw new ControllerException("尚未登录");
                }

                response.sendRedirect("/admin/");
                return false;
            }

            if (requestType == 1 && actionType == 0) {

                //api-授权
//                if (adminSession.getAccountType() != 2) {

//                    if (request.getRequestURL().indexOf("admin/account/getAdminAccountMessage") > 1) {
//                        return true;
//                    }

//                    if (request.getRequestURL().indexOf("admin/account/getAdminAccountList") > 1) {
//                        return true;
//                    }

//                    if (request.getRequestURL().indexOf("admin/account/getAdminAccountInfo") > 1) {
//                        return true;
//                    }

//                    if (request.getRequestURL().indexOf("admin/account/updateAdminAccountPassword") > 1) {
//                        return true;
//                    }

//                    if (request.getRequestURL().indexOf("admin/personnel/getPersonnelList") > 1) {
//                        return true;
//                    }

//                    throw new ControllerException("权限不足,请联系超级管理员获取权限");
//                }

            }

            return true;
        }

        //用户
        if (request.getRequestURL().indexOf("/sh/") > 1) {

            UserSeesion userSeesion = (UserSeesion) request.getSession().getAttribute("User");
            if (userSeesion == null) {

                if (requestType == 0 && actionType != 0) {
                    request.getSession().setAttribute("User", "");
                    return true;
                }

                if (requestType == 1 && actionType == 0) {

                    throw new ControllerException("尚未登录");
                }

                if (requestType == 0) {

                    response.sendRedirect("/sh/");

                    return true;
                }
            }
        }

        //人员
        PersonnelSession personnelSession = (PersonnelSession) request.getSession().getAttribute("Personnel");
        if (request.getRequestURL().indexOf("/personnel/") > 1) {

            if (personnelSession == null) {
                if (requestType == 0 && actionType != 0) {
                    request.getSession().setAttribute("Personnel", "");
                    return true;
                }

                if (requestType == 1 && actionType == 0) {

                    throw new ControllerException("尚未登录");
                }

                if (requestType == 0) {

                    response.sendRedirect("/personnel/");

                    return true;
                }
            }

        }

        //上传操作
        if (requestType == 1 && actionType == 3) {

            String param = request.getParameter("adr");

            if (param == null) {

                throw new ControllerException("ACTION_ILLEGAL");
            }

            //人员操作
            if ("pr".equals(param)) {

                if (personnelSession == null) {
                    throw new ControllerException("尚未登陆,请前往登陆");
                }

            }

            if ("ad".equals(param)) {

                if (adminSession == null) {
                    throw new ControllerException("尚未登陆,请前往登陆");
                }

                if (adminSession.getAccountType() != 2) {
                    throw new ControllerException("权限不足,请联系超级管理员获取权限");
                }
            }

            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
