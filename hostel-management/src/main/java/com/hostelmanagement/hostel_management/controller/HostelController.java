package com.hostelmanagement.hostel_management.controller;

import com.hostelmanagement.hostel_management.entity.Hostel;
import com.hostelmanagement.hostel_management.service.HostelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
public class HostelController {

    private final HostelService hostelService;

    public HostelController(
            HostelService hostelService) {

        this.hostelService = hostelService;
    }

    @GetMapping
    public ResponseEntity<List<Hostel>> getAllHostels() {

        return ResponseEntity.ok(
                hostelService.getAllHostels()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hostel> getHostelById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                hostelService.getHostelById(id)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hostel>> searchHostels(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                hostelService.searchHostels(keyword)
        );
    }
}