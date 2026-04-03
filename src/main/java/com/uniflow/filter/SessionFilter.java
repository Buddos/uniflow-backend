package com.uniflow.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "SessionFilter", urlPatterns = {"/servlet/*", "/api/*"})
public class SessionFilter implements Filter {
    
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/servlet/auth/login",
        "/api/auth/login",
        "/api/auth/register"
    );
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        if (!SessionUtil.isSessionValid(httpRequest)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Unauthorized. Please login.\"}");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::contains);
    }
    
    @Override
    public void destroy() {
    }
}