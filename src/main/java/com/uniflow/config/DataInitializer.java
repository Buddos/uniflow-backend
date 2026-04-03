package com.uniflow.config;

import com.uniflow.model.*;
import com.uniflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VenueRepository venueRepository;
    
    @Autowired
    private CourseUnitRepository courseUnitRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize Users
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@uniflow.edu");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Administrator");
            admin.setRole("ADMIN");
            admin.setIsActive(true);
            userRepository.save(admin);
            
            User hod = new User();
            hod.setEmail("hod@cs.edu");
            hod.setPassword(passwordEncoder.encode("hod123"));
            hod.setName("Dr. John Smith");
            hod.setRole("COD");
            hod.setDepartment("Computer Science");
            hod.setIsActive(true);
            userRepository.save(hod);
            
            User lecturer = new User();
            lecturer.setEmail("lecturer@cs.edu");
            lecturer.setPassword(passwordEncoder.encode("lecturer123"));
            lecturer.setName("Prof. Jane Doe");
            lecturer.setRole("LECTURER");
            lecturer.setDepartment("Computer Science");
            lecturer.setIsActive(true);
            userRepository.save(lecturer);
        }
        
        // Initialize Venues (PST, NPL, B, ED buildings)
        if (venueRepository.count() == 0) {
            // PST venues
            for (int i = 1; i <= 5; i++) {
                Venue venue = new Venue();
                venue.setName("PST" + i);
                venue.setCapacity(150 + (i * 10));
                venue.setBuilding("PST Building");
                venue.setFloor(1);
                venue.setEquipmentHome("PST Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(i > 3);
                venueRepository.save(venue);
            }
            
            // NPL venues
            for (int i = 1; i <= 6; i++) {
                Venue venue = new Venue();
                venue.setName("NPL" + i);
                venue.setCapacity(200 + (i * 15));
                venue.setBuilding("NPL Building");
                venue.setFloor(2);
                venue.setEquipmentHome("NPL Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(true);
                venueRepository.save(venue);
            }
            
            // B venues
            for (int i = 1; i <= 5; i++) {
                Venue venue = new Venue();
                venue.setName("B" + i);
                venue.setCapacity(100 + (i * 5));
                venue.setBuilding("B Building");
                venue.setFloor(1);
                venue.setEquipmentHome("B Equipment Room");
                venue.setHasProjector(i > 2);
                venue.setHasWhiteboard(true);
                venue.setHasAC(false);
                venueRepository.save(venue);
            }
            
            // ED venues
            int[] edNumbers = {1, 4, 5};
            for (int num : edNumbers) {
                Venue venue = new Venue();
                venue.setName("ED" + num);
                venue.setCapacity(80);
                venue.setBuilding("ED Building");
                venue.setFloor(1);
                venue.setEquipmentHome("ED Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(false);
                venueRepository.save(venue);
            }
        }
        
        // Initialize Course Units
        if (courseUnitRepository.count() == 0) {
            CourseUnit cs101 = new CourseUnit();
            cs101.setCode("CS101");
            cs101.setName("Introduction to Programming");
            cs101.setDepartment("Computer Science");
            cs101.setCredits(3);
            cs101.setIsCore(true);
            courseUnitRepository.save(cs101);
            
            CourseUnit math101 = new CourseUnit();
            math101.setCode("MATH101");
            math101.setName("Calculus I");
            math101.setDepartment("Mathematics");
            math101.setCredits(3);
            math101.setIsCore(true);
            courseUnitRepository.save(math101);
        }
    }
}