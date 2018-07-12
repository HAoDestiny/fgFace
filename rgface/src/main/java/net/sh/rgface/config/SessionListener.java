package net.sh.rgface.config;

import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.util.SessionManagement;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by DESTINY on 2018/6/23.
 */

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    public static SessionManagement sessionManagement = SessionManagement.getInstance();

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

        System.out.println("------------ sessionCreated, sessionId: " + httpSessionEvent.getSession().getId());

        sessionManagement.addSession(httpSessionEvent.getSession().getId(), httpSessionEvent.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        System.out.println("------------ sessionDestroyed sessionId: " + httpSessionEvent.getSession().getId());

        System.out.println("------------ MapSize: Start= " + sessionManagement.getSessionHashMap().size());

        AdminSession adminSession = (AdminSession) httpSessionEvent.getSession().getAttribute("Admin");

        if (null != adminSession) {
            sessionManagement.removeSession(String.valueOf(adminSession.getId()));
            httpSessionEvent.getSession().removeAttribute("Admin");
        }

        sessionManagement.removeSession(httpSessionEvent.getSession().getId());

        System.out.println("------------ MapSize: End= " + sessionManagement.getSessionHashMap().size());
    }

}
