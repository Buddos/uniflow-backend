package com.uniflow.util;

import com.uniflow.listener.SessionTrackingListener;
import com.uniflow.model.User;
import jakarta.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    
    public static final String USER_ATTR = "user";
    public static final String USER_ROLE_ATTR = "userRole";
    public static final String USER_ID_ATTR = "userId";
    public static final String DEPARTMENT_ATTR = "department";
    public static final String LAST_ACTIVITY_ATTR = "lastActivity";
    
    public static void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTR, user);
        session.setAttribute(USER_ROLE_ATTR, user.getRole());
        session.setAttribute(USER_ID_ATTR, user.getId());
        session.setAttribute(DEPARTMENT_ATTR, user.getDepartment());
        session.setAttribute(LAST_ACTIVITY_ATTR, System.currentTimeMillis());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
        
        SessionTrackingListener.registerUserSession(session.getId(), user.getEmail());
    }
    
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(USER_ATTR);
        }
        return null;
    }
    
    public static String getUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(USER_ROLE_ATTR);
        }
        return null;
    }
    
    public static Long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute(USER_ID_ATTR);
        }
        return null;
    }
    
    public static String getDepartment(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(DEPARTMENT_ATTR);
        }
        return null;
    }
    
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionTrackingListener.unregisterUserSession(session.getId());
            session.invalidate();
        }
    }
    
    public static boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_ATTR);
        if (lastActivity == null) {
            return false;
        }
        
        long inactivityTime = System.currentTimeMillis() - lastActivity;
        if (inactivityTime > 30 * 60 * 1000) { // 30 minutes
            invalidateSession(request);
            return false;
        }
        
        session.setAttribute(LAST_ACTIVITY_ATTR, System.currentTimeMillis());
        return true;
    }
}