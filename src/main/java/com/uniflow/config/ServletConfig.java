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
    
    // Servlet Bean Definitions
    @Bean
    public AuthenticationServlet authenticationServlet() {
        return new AuthenticationServlet();
    }

    @Bean
    public VenueServlet venueServlet() {
        return new VenueServlet();
    }

    @Bean
    public TimetableServlet timetableServlet() {
        return new TimetableServlet();
    }

    @Bean
    public RequestServlet requestServlet() {
        return new RequestServlet();
    }

    @Bean
    public TripServlet tripServlet() {
        return new TripServlet();
    }

    @Bean
    public BookingServlet bookingServlet() {
        return new BookingServlet();
    }

    @Bean
    public EquipmentServlet equipmentServlet() {
        return new EquipmentServlet();
    }

    @Bean
    public DashboardServlet dashboardServlet() {
        return new DashboardServlet();
    }

    @Bean
    public ServletRegistrationBean<AuthenticationServlet> registerAuthServlet(AuthenticationServlet servlet) {
        ServletRegistrationBean<AuthenticationServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/auth/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<VenueServlet> registerVenueServlet(VenueServlet servlet) {
        ServletRegistrationBean<VenueServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/venues/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<TimetableServlet> registerTimetableServlet(TimetableServlet servlet) {
        ServletRegistrationBean<TimetableServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/timetable/*");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<RequestServlet> registerRequestServlet(RequestServlet servlet) {
        ServletRegistrationBean<RequestServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/requests/*");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<TripServlet> registerTripServlet(TripServlet servlet) {
        ServletRegistrationBean<TripServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/trips/*");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<BookingServlet> registerBookingServlet(BookingServlet servlet) {
        ServletRegistrationBean<BookingServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/bookings/*");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<EquipmentServlet> registerEquipmentServlet(EquipmentServlet servlet) {
        ServletRegistrationBean<EquipmentServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/equipment/*");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<DashboardServlet> registerDashboardServlet(DashboardServlet servlet) {
        ServletRegistrationBean<DashboardServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(servlet);
        registrationBean.addUrlMappings("/api/dashboard/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SessionFilter> registerSessionFilter(SessionFilter filter) {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CookieFilter> registerCookieFilter(CookieFilter filter) {
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> registerCorsFilter(CORSFilter filter) {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
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