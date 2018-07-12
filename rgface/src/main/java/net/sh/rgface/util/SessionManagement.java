package net.sh.rgface.util;

import net.sh.rgface.exception.ControllerException;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by DESTINY on 2018/6/23.
 */
public class SessionManagement {

    private static SessionManagement sessionManagement;
    private HashMap<String, HttpSession> sessionHashMap;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private SessionManagement() {
        sessionHashMap = new HashMap<>();
    }

    public static SessionManagement getInstance() {
        if (sessionManagement == null) {
            sessionManagement = new SessionManagement();
        }
        return sessionManagement;
    }

    public void addSession(String id, HttpSession httpSession) {

        readWriteLock.writeLock().lock();
        try {

            if (httpSession != null && id != null && id.length() > 0) {
                sessionHashMap.put(id, httpSession);
            }

        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void removeSession(String id) {

        readWriteLock.writeLock().lock();
        try {

            if (id != null && id.length() > 0) {
                sessionHashMap.remove(id);
            }

        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public HttpSession getSession(String sessionId) {

        readWriteLock.readLock().lock();
        try {
            if (sessionId == null) {
                return null;
            }

            return sessionHashMap.get(sessionId);

        } finally {
            readWriteLock.readLock().unlock();
        }

    }

    public HashMap<String, HttpSession> getSessionHashMap() {
        return sessionHashMap;
    }

    public void setSessionHashMap(HashMap<String, HttpSession> sessionHashMap) {
        this.sessionHashMap = sessionHashMap;
    }
}
