package com.uniflow.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionTrackingListener implements HttpSessionListener {
    
    private static final AtomicInteger activeSessions = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions.incrementAndGet();
        System.out.println("Session created: " + se.getSession().getId());
        System.out.println("Active sessions: " + activeSessions.get());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        String user = sessionUserMap.remove(sessionId);
        activeSessions.decrementAndGet();
        System.out.println("Session destroyed: " + sessionId + " for user: " + user);
        System.out.println("Active sessions: " + activeSessions.get());
    }
    
    public static void registerUserSession(String sessionId, String userEmail) {
        sessionUserMap.put(sessionId, userEmail);
    }
    
    public static void unregisterUserSession(String sessionId) {
        sessionUserMap.remove(sessionId);
    }
    
    public static int getActiveSessionCount() {
        return activeSessions.get();
    }
    
    public static ConcurrentHashMap<String, String> getActiveSessions() {
        return sessionUserMap;
    }
}