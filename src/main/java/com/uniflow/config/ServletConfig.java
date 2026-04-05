package com.uniflow.config;

import com.uniflow.servlet.*;
import com.uniflow.filter.SessionFilter;
import com.uniflow.filter.CookieFilter;
import com.uniflow.filter.CORSFilter;
import com.uniflow.listener.SessionTrackingListener;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class ServletConfig {
    
    @Autowired
    private AuthServlet authServlet;
    
    @Autowired
    private VenueServlet venueServlet;
    
    @Autowired
    private TimetableServlet timetableServlet;
    
    @Autowired
    private RequestServlet requestServlet;
    
    @Autowired
    private TripServlet tripServlet;
    
    @Autowired
    private BookingServlet bookingServlet;
    
    @Autowired
    private EquipmentServlet equipmentServlet;
    
    @Autowired
    private DashboardServlet dashboardServlet;
    
    @Autowired
    private SessionFilter sessionFilter;
    
    @Autowired
    private CookieFilter cookieFilter;
    
    @Autowired
    private CORSFilter corsFilter;
    
    @Bean
    public ServletRegistrationBean<AuthServlet> registerAuthServlet() {
        ServletRegistrationBean<AuthServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(authServlet);
        registrationBean.addUrlMappings("/api/auth/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<VenueServlet> registerVenueServlet() {
        ServletRegistrationBean<VenueServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(venueServlet);
        registrationBean.addUrlMappings("/api/venues/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<TimetableServlet> registerTimetableServlet() {
        ServletRegistrationBean<TimetableServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(timetableServlet);
        registrationBean.addUrlMappings("/api/timetable/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<RequestServlet> registerRequestServlet() {
        ServletRegistrationBean<RequestServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(requestServlet);
        registrationBean.addUrlMappings("/api/requests/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<TripServlet> registerTripServlet() {
        ServletRegistrationBean<TripServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(tripServlet);
        registrationBean.addUrlMappings("/api/trips/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<BookingServlet> registerBookingServlet() {
        ServletRegistrationBean<BookingServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(bookingServlet);
        registrationBean.addUrlMappings("/api/bookings/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<EquipmentServlet> registerEquipmentServlet() {
        ServletRegistrationBean<EquipmentServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(equipmentServlet);
        registrationBean.addUrlMappings("/api/equipment/*");
        return registrationBean;
    }
    
    @Bean
    public ServletRegistrationBean<DashboardServlet> registerDashboardServlet() {
        ServletRegistrationBean<DashboardServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(dashboardServlet);
        registrationBean.addUrlMappings("/api/dashboard/*");
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<SessionFilter> registerSessionFilter() {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sessionFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<CookieFilter> registerCookieFilter() {
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cookieFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<CORSFilter> registerCorsFilter() {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
    
    @Bean
    public ServletListenerRegistrationBean<SessionTrackingListener> registerSessionListener() {
        ServletListenerRegistrationBean<SessionTrackingListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new SessionTrackingListener());
        return registrationBean;
    }
}