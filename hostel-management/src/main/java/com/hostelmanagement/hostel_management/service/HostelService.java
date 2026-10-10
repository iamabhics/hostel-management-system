package com.hostelmanagement.hostel_management.service;

import com.hostelmanagement.hostel_management.dto.HostelDTO;
import com.hostelmanagement.hostel_management.entity.Hostel;
import com.hostelmanagement.hostel_management.repository.HostelRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;

    public HostelService(
            HostelRepository hostelRepository) {

        this.hostelRepository = hostelRepository;
    }

    public List<Hostel> getAllHostels() {

        return hostelRepository.findAll();
    }

    public Hostel getHostelById(Long id) {

        return hostelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Hostel not found"));
    }

    public List<Hostel> searchHostels(String keyword) {

        return hostelRepository
                .findByNameContainingIgnoreCaseOrAreaContainingIgnoreCaseOrCityContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                );
    }

    public Hostel createHostel(HostelDTO dto) {

        Hostel hostel = new Hostel();

        hostel.setName(dto.getName());
        hostel.setDescription(dto.getDescription());
        hostel.setAddress(dto.getAddress());
        hostel.setArea(dto.getArea());
        hostel.setCity(dto.getCity());
        hostel.setMonthlyRent(dto.getMonthlyRent());
        hostel.setContactNumber(dto.getContactNumber());

        return hostelRepository.save(hostel);
    }
}