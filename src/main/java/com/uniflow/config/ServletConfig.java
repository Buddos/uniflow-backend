package com.uniflow.config;

import com.uniflow.servlet.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.uniflow.filter.SessionFilter;
import com.uniflow.filter.CookieFilter;
import com.uniflow.filter.CORSFilter;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import com.uniflow.listener.SessionTrackingListener;

@Configuration
public class ServletConfig {
    
    @Bean
    public ServletRegistrationBean<AuthServlet> authServlet() {
        ServletRegistrationBean<AuthServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new AuthServlet());
        registrationBean.addUrlMappings("/servlet/auth/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<VenueServlet> venueServlet() {
        ServletRegistrationBean<VenueServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new VenueServlet());
        registrationBean.addUrlMappings("/servlet/venues/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<TimetableServlet> timetableServlet() {
        ServletRegistrationBean<TimetableServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new TimetableServlet());
        registrationBean.addUrlMappings("/servlet/timetable/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<RequestServlet> requestServlet() {
        ServletRegistrationBean<RequestServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new RequestServlet());
        registrationBean.addUrlMappings("/servlet/requests/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<TripServlet> tripServlet() {
        ServletRegistrationBean<TripServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new TripServlet());
        registrationBean.addUrlMappings("/servlet/trips/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<BookingServlet> bookingServlet() {
        ServletRegistrationBean<BookingServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new BookingServlet());
        registrationBean.addUrlMappings("/servlet/bookings/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<EquipmentServlet> equipmentServlet() {
        ServletRegistrationBean<EquipmentServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new EquipmentServlet());
        registrationBean.addUrlMappings("/servlet/equipment/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<DashboardServlet> dashboardServlet() {
        ServletRegistrationBean<DashboardServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new DashboardServlet());
        registrationBean.addUrlMappings("/servlet/dashboard/*");
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilter() {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionFilter());
        registrationBean.addUrlPatterns("/servlet/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<CookieFilter> cookieFilter() {
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CookieFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilter() {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
    
    @Bean
    public ServletListenerRegistrationBean<SessionTrackingListener> sessionListener() {
        ServletListenerRegistrationBean<SessionTrackingListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new SessionTrackingListener());
        return registrationBean;
    }
}