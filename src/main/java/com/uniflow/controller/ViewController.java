package com.uniflow.controller;

import com.uniflow.model.CourseUnit;
import com.uniflow.model.CourseUnitRequest;
import com.uniflow.model.Equipment;
import com.uniflow.model.TimetableEntry;
import com.uniflow.repository.CourseUnitRepository;
import com.uniflow.service.EquipmentService;
import com.uniflow.service.RequestService;
import com.uniflow.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private CourseUnitRepository courseUnitRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private RequestService requestService;

    @GetMapping({"/", "/dashboard"})
    public String showIndex(Model model) {
        model.addAttribute("title", "UniFlow - Dashboard Home");
        return "index";
    }

    @GetMapping("/courseunit")
    public String showCourseUnits(Model model) {
        List<CourseUnit> units = courseUnitRepository.findAll();
        model.addAttribute("units", units);
        model.addAttribute("title", "Course Units - UniFlow");
        return "courseunit";
    }

    @GetMapping("/equipment")
    public String showEquipment(Model model) {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("title", "Equipment Management - UniFlow");
        return "equipment";
    }

    @GetMapping("/timetable")
    public String showTimetable(Model model) {
        List<TimetableEntry> entries = timetableService.getAllTimetableEntries();
        model.addAttribute("entries", entries);
        model.addAttribute("title", "Timetable Schedule - UniFlow");
        return "timetable";
    }

    @GetMapping("/courserequest")
    public String showCourseRequests(Model model) {
        List<CourseUnitRequest> requests = requestService.getAllRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Course Unit Requests - UniFlow");
        return "courserequest";
    }
}
