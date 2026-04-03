package com.uniflow.service;

import com.uniflow.model.Equipment;
import com.uniflow.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EquipmentService {
    
    @Autowired
    private EquipmentRepository equipmentRepository;
    
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }
    
    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }
    
    public Equipment createEquipment(Equipment equipment) {
        // Generate QR code
        equipment.setQrCode(UUID.randomUUID().toString());
        equipment.setStatus("AVAILABLE");
        return equipmentRepository.save(equipment);
    }
    
    public Equipment updateEquipmentStatus(Long id, String status, String venue) {
        Equipment equipment = getEquipmentById(id);
        equipment.setStatus(status);
        equipment.setCurrentVenue(venue);
        equipment.setUpdatedAt(LocalDateTime.now());
        return equipmentRepository.save(equipment);
    }
    
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatus("AVAILABLE");
    }
    
    public List<Equipment> getEquipmentByDepartment(String department) {
        return equipmentRepository.findByHomeDepartment(department);
    }
    
    public Map<String, Object> getEquipmentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", equipmentRepository.count());
        stats.put("available", equipmentRepository.findByStatus("AVAILABLE").size());
        stats.put("inUse", equipmentRepository.countEquipmentInUse());
        stats.put("maintenance", equipmentRepository.findByStatus("UNDER_MAINTENANCE").size());
        return stats;
    }
}