package com.uniflow.filter;

import com.uniflow.util.CookieUtil;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CookieFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Check for remember me cookie
        String rememberMe = CookieUtil.getCookieValue(httpRequest, "REMEMBER_ME");
        if (rememberMe != null && httpRequest.getSession(false) == null) {
            // Auto-login logic would go here
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
    }
}