package com.uniflow.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidCapacity(Integer capacity) {
        return capacity != null && capacity >= 10 && capacity <= 1000;
    }
    
    public static boolean isValidTimeRange(LocalTime start, LocalTime end) {
        return start != null && end != null && start.isBefore(end);
    }
}