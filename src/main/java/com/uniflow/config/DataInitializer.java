package com.uniflow.config;

import com.uniflow.model.*;
import com.uniflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting UniFlow Data Initialization...");
        
        // Initialize Users
        initializeUsers();
        
        // Initialize Venues (PST, NPL, B, ED buildings)
        initializeVenues();
        
        // Initialize Course Units
        initializeCourseUnits();
        
        System.out.println("✅ Data initialization completed!");
        System.out.println("📊 Users created: " + userRepository.count());
        System.out.println("🏢 Venues created: " + venueRepository.count());
        System.out.println("📚 Course Units created: " + courseUnitRepository.count());
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            // Admin User
            User admin = new User();
            admin.setEmail("admin@uniflow.edu");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Administrator");
            admin.setRole("ADMIN");
            admin.setDepartment(null);
            admin.setIsActive(true);
            admin.setPhoneNumber("+254700000001");
            userRepository.save(admin);
            System.out.println("✓ Created Admin user");
            
            // Head of Department - Computer Science
            User hod = new User();
            hod.setEmail("hod@cs.edu");
            hod.setPassword(passwordEncoder.encode("hod123"));
            hod.setName("Dr. John Smith");
            hod.setRole("COD");
            hod.setDepartment("Computer Science");
            hod.setIsActive(true);
            hod.setPhoneNumber("+254700000002");
            userRepository.save(hod);
            System.out.println("✓ Created HOD user");
            
            // Head of Department - Mathematics
            User hodMath = new User();
            hodMath.setEmail("hod@math.edu");
            hodMath.setPassword(passwordEncoder.encode("hod123"));
            hodMath.setName("Prof. Sarah Johnson");
            hodMath.setRole("COD");
            hodMath.setDepartment("Mathematics");
            hodMath.setIsActive(true);
            hodMath.setPhoneNumber("+254700000003");
            userRepository.save(hodMath);
            System.out.println("✓ Created HOD Mathematics user");
            
            // Lecturer - Computer Science
            User lecturer = new User();
            lecturer.setEmail("lecturer@cs.edu");
            lecturer.setPassword(passwordEncoder.encode("lecturer123"));
            lecturer.setName("Prof. Jane Doe");
            lecturer.setRole("LECTURER");
            lecturer.setDepartment("Computer Science");
            lecturer.setIsActive(true);
            lecturer.setPhoneNumber("+254700000004");
            userRepository.save(lecturer);
            System.out.println("✓ Created Lecturer user");
            
            // Additional Lecturer - Mathematics
            User lecturerMath = new User();
            lecturerMath.setEmail("lecturer@math.edu");
            lecturerMath.setPassword(passwordEncoder.encode("lecturer123"));
            lecturerMath.setName("Dr. Robert Taylor");
            lecturerMath.setRole("LECTURER");
            lecturerMath.setDepartment("Mathematics");
            lecturerMath.setIsActive(true);
            lecturerMath.setPhoneNumber("+254700000005");
            userRepository.save(lecturerMath);
            System.out.println("✓ Created Mathematics Lecturer user");
        } else {
            System.out.println("ℹ️ Users already exist, skipping initialization");
        }
    }
    
    private void initializeVenues() {
        if (venueRepository.count() == 0) {
            // PST venues (1-5)
            String[] pstNames = {"PST1", "PST2", "PST3", "PST4", "PST5"};
            int[] pstCapacities = {160, 170, 180, 190, 200};
            for (int i = 0; i < pstNames.length; i++) {
                Venue venue = new Venue();
                venue.setName(pstNames[i]);
                venue.setCapacity(pstCapacities[i]);
                venue.setBuilding("PST Building");
                venue.setFloor(i < 2 ? 1 : 2);
                venue.setEquipmentHome("PST Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(i >= 2);
                venue.setStatus("AVAILABLE");
                venue.setDescription("PST Lecture Hall " + (i + 1));
                venueRepository.save(venue);
            }
            System.out.println("✓ Created PST venues (5 halls)");
            
            // NPL venues (1-6)
            String[] nplNames = {"NPL1", "NPL2", "NPL3", "NPL4", "NPL5", "NPL6"};
            int[] nplCapacities = {215, 230, 245, 260, 275, 290};
            for (int i = 0; i < nplNames.length; i++) {
                Venue venue = new Venue();
                venue.setName(nplNames[i]);
                venue.setCapacity(nplCapacities[i]);
                venue.setBuilding("NPL Building");
                venue.setFloor((i / 2) + 1);
                venue.setEquipmentHome("NPL Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(true);
                venue.setStatus("AVAILABLE");
                venue.setDescription("NPL Lecture Hall " + (i + 1));
                venueRepository.save(venue);
            }
            System.out.println("✓ Created NPL venues (6 halls)");
            
            // B venues (1-5)
            String[] bNames = {"B1", "B2", "B3", "B4", "B5"};
            int[] bCapacities = {105, 110, 115, 120, 125};
            for (int i = 0; i < bNames.length; i++) {
                Venue venue = new Venue();
                venue.setName(bNames[i]);
                venue.setCapacity(bCapacities[i]);
                venue.setBuilding("B Building");
                venue.setFloor(i < 2 ? 1 : (i < 4 ? 2 : 3));
                venue.setEquipmentHome("B Equipment Room");
                venue.setHasProjector(i >= 2);
                venue.setHasWhiteboard(true);
                venue.setHasAC(false);
                venue.setStatus("AVAILABLE");
                venue.setDescription("B Building Hall " + (i + 1));
                venueRepository.save(venue);
            }
            System.out.println("✓ Created B venues (5 halls)");
            
            // ED venues (1, 4, 5)
            int[] edNumbers = {1, 4, 5};
            int[] edCapacities = {80, 85, 90};
            for (int i = 0; i < edNumbers.length; i++) {
                Venue venue = new Venue();
                venue.setName("ED" + edNumbers[i]);
                venue.setCapacity(edCapacities[i]);
                venue.setBuilding("ED Building");
                venue.setFloor(i == 0 ? 1 : 2);
                venue.setEquipmentHome("ED Equipment Room");
                venue.setHasProjector(true);
                venue.setHasWhiteboard(true);
                venue.setHasAC(false);
                venue.setStatus("AVAILABLE");
                venue.setDescription("ED Building Hall ED" + edNumbers[i]);
                venueRepository.save(venue);
            }
            System.out.println("✓ Created ED venues (3 halls)");
            
            System.out.println("✓ Total venues created: " + venueRepository.count());
        } else {
            System.out.println("ℹ️ Venues already exist, skipping initialization");
        }
    }
    
    private void initializeCourseUnits() {
        if (courseUnitRepository.count() == 0) {
            // Computer Science Courses
            CourseUnit cs101 = new CourseUnit();
            cs101.setCode("CS101");
            cs101.setName("Introduction to Programming");
            cs101.setDepartment("Computer Science");
            cs101.setCredits(3);
            cs101.setIsCore(true);
            cs101.setDescription("Fundamentals of programming using Python. Topics include variables, control structures, functions, and basic data structures.");
            courseUnitRepository.save(cs101);
            
            CourseUnit cs201 = new CourseUnit();
            cs201.setCode("CS201");
            cs201.setName("Data Structures and Algorithms");
            cs201.setDepartment("Computer Science");
            cs201.setCredits(4);
            cs201.setIsCore(true);
            cs201.setDescription("Advanced data structures (stacks, queues, trees, graphs) and algorithmic design techniques.");
            courseUnitRepository.save(cs201);
            
            CourseUnit cs301 = new CourseUnit();
            cs301.setCode("CS301");
            cs301.setName("Database Management Systems");
            cs301.setDepartment("Computer Science");
            cs301.setCredits(3);
            cs301.setIsCore(true);
            cs301.setDescription("Relational databases, SQL, database design, and transaction management.");
            courseUnitRepository.save(cs301);
            
            CourseUnit cs401 = new CourseUnit();
            cs401.setCode("CS401");
            cs401.setName("Web Development");
            cs401.setDepartment("Computer Science");
            cs401.setCredits(3);
            cs401.setIsCore(false);
            cs401.setDescription("Modern web development using HTML, CSS, JavaScript, and frameworks.");
            courseUnitRepository.save(cs401);
            
            // Mathematics Courses
            CourseUnit math101 = new CourseUnit();
            math101.setCode("MATH101");
            math101.setName("Calculus I");
            math101.setDepartment("Mathematics");
            math101.setCredits(3);
            math101.setIsCore(true);
            math101.setDescription("Limits, derivatives, and integrals of single-variable functions.");
            courseUnitRepository.save(math101);
            
            CourseUnit math102 = new CourseUnit();
            math102.setCode("MATH102");
            math102.setName("Calculus II");
            math102.setDepartment("Mathematics");
            math102.setCredits(3);
            math102.setIsCore(true);
            math102.setDescription("Advanced calculus techniques, series, and multivariable calculus.");
            courseUnitRepository.save(math102);
            
            CourseUnit math201 = new CourseUnit();
            math201.setCode("MATH201");
            math201.setName("Linear Algebra");
            math201.setDepartment("Mathematics");
            math201.setCredits(3);
            math201.setIsCore(false);
            math201.setDescription("Vector spaces, matrices, determinants, and linear transformations.");
            courseUnitRepository.save(math201);
            
            // Physics Courses
            CourseUnit phys101 = new CourseUnit();
            phys101.setCode("PHYS101");
            phys101.setName("Physics I");
            phys101.setDepartment("Physics");
            phys101.setCredits(4);
            phys101.setIsCore(true);
            phys101.setDescription("Mechanics, thermodynamics, and waves.");
            courseUnitRepository.save(phys101);
            
            // Engineering Courses
            CourseUnit eng101 = new CourseUnit();
            eng101.setCode("ENG101");
            eng101.setName("Technical Writing");
            eng101.setDepartment("Engineering");
            eng101.setCredits(2);
            eng101.setIsCore(false);
            eng101.setDescription("Technical communication, report writing, and documentation.");
            courseUnitRepository.save(eng101);
            
            System.out.println("✓ Created course units: " + courseUnitRepository.count());
        } else {
            System.out.println("ℹ️ Course units already exist, skipping initialization");
        }
    }
}